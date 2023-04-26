#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <FS.h>
#include <ArduinoJson.h>
#include <PubSubClient.h>
#include <threads.h>

const char* ssid = "HC-LIGHT-001";
const char* pass = "homecould";//设备配网二维码 HC-LIGHT-001|HC-LIGHT-202300001
//设备编码
const char* hcid = "HC-LIGHT-202300001";
//静态地址、网关、子网掩码
const IPAddress local_IP(192,168,0,41);
const IPAddress gateway(192,168, 1, 1);
const IPAddress subnet(255,255,255, 0);
//存储WiFi、MQTT信息
const char* filePath = "/resouce/info.json";
//配网状态标志 (默认false)
bool config_status = false;
// Web服务器对象
ESP8266WebServer webserver(80);

WiFiClient wifiClient;
PubSubClient mqttClient(wifiClient);

typedef struct{
  String ssid;
  String pass;
  String mqttServer;
  String controlTopic;
  String statusTopic;
  String feedbackTopic;
}ResouceInfo;

//配网资源信息
ResouceInfo resous;

//配置请求回调函数
void handleConfig() {
  //WiFi ssid
  String ssid = webserver.arg("ssid");
  //WiFi 密码
  String pass = webserver.arg("pass");
  //Mqtt 服务器地址
  String mqttServer = webserver.arg("mqttServer");
  //Mqtt 控制话题
  String controlTopic = webserver.arg("controlTopic");
  //Mqtt 状态反馈话题
  String statusTopic = webserver.arg("statusTopic");
  //Mqtt 控制反馈话题
  String feedbackTopic = webserver.arg("feedbackTopic");

  if (ssid.length()>0&&pass.length()>0&&mqttServer.length()>0&&controlTopic.length()>0&&statusTopic.length()>0&&feedbackTopic.length()>0){
    Serial.println(ssid);
    Serial.println(pass);
    Serial.println(mqttServer);
    Serial.println(controlTopic);
    Serial.println(statusTopic);
    Serial.println(feedbackTopic);
    if(wifiStaInit(ssid,pass) && MqttInit(mqttServer,controlTopic,statusTopic,feedbackTopic)){
      //WiFi MQTT 连接成功！ 退出配网模式 并保存信息到闪存文件中
      saveResouceInfo(ssid,pass,mqttServer,controlTopic,statusTopic,feedbackTopic);
      webserver.send(200, "application/json", "{\"message\":\"ok\"}");
      //延时5s返回连接成功消息
      delay(5000);
      //配网状态改为 true
      config_status = true;
      //配网状态提示灯关闭

    }else{
      webserver.send(200, "application/json", "{\"message\":\"Fail\"}");
      Serial.println("config fail");
    }
  }
  webserver.send(200, "application/json", "{\"message\":\"argError\"}");
}
//AP模式 配网初始化
void wifiApInit(){
  //无线AP+STA模式 用于配网环境
  WiFi.mode(WIFI_AP_STA);
  WiFi.softAPConfig(local_IP, gateway, subnet);
  WiFi.softAP(ssid, pass);
}
// WiFi 连接初始化
bool wifiStaInit(String ssid,String pass){
  ///无线AP+STA模式 用于配网环境
  WiFi.mode(WIFI_AP_STA);
  WiFi.begin(ssid,pass);
  //等待连接 最长等待时间0.5min
  for(int i=0;i<60;i++){
    if(WiFi.status()==WL_CONNECTED){
      //WiFi连接成功
      Serial.println("wifi success!");
      return true;
    }
    delay(1000);
  }
  Serial.println("wifi Fail!");
  //WiFi连接失败
  return false;
}

//将WIFI、MQTT信息保存到闪存文件中
void saveResouceInfo(String ssid,String pass,String mqttServer,String controlTopic,String statusTopic,String feedbackTopic){
  File file = SPIFFS.open(filePath,"w");
  String str = "{\"ssid\":\""+ssid+"\",\"pass\":\""+pass+"\",\"mqttServer\":\""+mqttServer+"\",\"controlTopic\":\""+controlTopic+"\",\"statusTopic\":\""+statusTopic+"\",\"feedbackTopic\":\""+feedbackTopic+"\"}";
  Serial.println(str);
  file.print(str);
  file.close();
}
// 读取并解析 资源信息
ResouceInfo ReadResouceInfo(){
  File file = SPIFFS.open(filePath,"r");
  ResouceInfo resous;
  String info = "";
  for(int i=0;i<file.size();i++){
    //注意：此处不要直接强转为String 强转为char会将ASCII转化为char
    info+=(char)file.read();
  }
  file.close();
  Serial.print("fileInfo: ");
  Serial.println(info);
  size_t capacity = JSON_OBJECT_SIZE(6) + 120;
  DynamicJsonDocument doc(capacity);
  deserializeJson(doc,info);
  resous.ssid = doc["ssid"].as<String>();
  resous.pass = doc["pass"].as<String>();
  resous.mqttServer = doc["mqttServer"].as<String>();
  resous.controlTopic = doc["controlTopic"].as<String>();
  resous.statusTopic = doc["statusTopic"].as<String>();
  resous.feedbackTopic = doc["feedbackTopic"].as<String>();
  return resous;
}
//MQTT 服务初始化
boolean MqttInit(String mqttServer,String controlTopic,String statusTopic,String feedbackTopic){
    Serial.println("MQTT Init...");
    mqttClient.setServer(mqttServer.c_str(),1883);
    //遗嘱消息
    String willMsg = "{\"status\":\"outline\",\"on_off\":\"unknown\"}";
    //连接Mqtt服务器
    char Msg[willMsg.length()+1];
    strcpy(Msg,willMsg.c_str());
    Serial.println("Mqtt connect...");
    if(mqttClient.connect(hid,statusTopic.c_str(),1,false,Msg)){
        Serial.println("Mqtt connect success...");
        //订阅控制消息消息主题
        if(mqttClient.subscribe(controlTopic.c_str(),1)){
            Serial.println("Subscribe controlTopic success...");
            String onlineMsg = "{\"status\":\"online\",\"on_off\":\"off\"}";
            char msg[onlineMsg.length()+1];
            strcpy(msg,onlineMsg.c_str());
            mqttClient.setCallback(ContrlCallback);
            //发送上线消息
            mqttClient.publish(statusTopic.c_str(),msg);
        }else{
          return false;
        }
    }else{
        return false;
    }
    Serial.println("MQTT Init success...");
    return true;
}

//控制回调函数
void ContrlCallback(char* topic, byte* payload, unsigned int length){
    String msg = "";
    for(int i = 0;i<length;i++){
      msg += (char)payload[i];
    }
    size_t capacity = JSON_OBJECT_SIZE(6) + 120;
    DynamicJsonDocument doc(capacity);
    deserializeJson(doc,msg);
    String message = doc["message"].as<String>();
    boolean on_off = doc["on_off"].as<boolean>();
    !on_off?digitalWrite(LED_BUILTIN, HIGH):digitalWrite(LED_BUILTIN, LOW);
    Serial.println(msg);
    Serial.println("Callback...");
}
//上电或初始化
void setup(){
  Serial.begin(9600);
  if(SPIFFS.begin()){Serial.println("SPIFFS Started.");}
  //  SPIFFS.format();
  if(SPIFFS.exists(filePath)){
    //读取到文件 调用文件中的信息 初始化连接
    Serial.println("File Founded");
    resous = ReadResouceInfo();
    //WiFi 连接
    wifiStaInit(resous.ssid,resous.pass);
    //Mqtt 初始化
    MqttInit(resous.mqttServer,resous.controlTopic,resous.statusTopic,resous.feedbackTopic);
  }else{
    //未读取到文件 开启配网模式
    Serial.println("File Not Found");
    Serial.println("Enter distribution network mode");
    //开启配网模式提示灯

    wifiApInit();
    webserver.begin();//开启Web服务器 用于获取配网信息
    webserver.on("/config", handleConfig);
    while(!config_status){
      webserver.handleClient();
      // String ssid = webserver.arg("ssid");
      // String pass = webserver.arg("pass");
      // if (ssid.length()>0&&pass.length()>0){
      //   Serial.println(ssid);
      //   Serial.println(pass);
      //   if(wifiStaInit(ssid,pass)){
      //     //WiFi连接成功！ 退出配网模式 并保存信息到闪存文件中
      //     saveResouceInfo(ssid,pass);
      //     break;
      //   }
      // }
      // wifiApInit();
    }
    webserver.close();
  }
  // delay(2000);
  // 重启
  // ESP.restart();
  WiFi.mode(WIFI_STA);
  Serial.println("config success!");
  pinMode(LED_BUILTIN, OUTPUT);     // 设置板上LED引脚为输出模式
  digitalWrite(LED_BUILTIN, HIGH);
}

//运行时
void loop() {
  mqttClient.loop();
}