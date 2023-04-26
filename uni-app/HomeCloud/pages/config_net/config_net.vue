<template>
	<view>
		<view style="height: 170rpx;background-color: #fff;">
			<image src="../../static/black.png" style="margin-top: 95rpx;margin-left: 38rpx;width: 45rpx;height: 45rpx;"
				@click="back"></image>
		</view>
		<view style="flex-direction:column;">
			<view style="margin-top: 20rpx;">
				<text style="font-size: 60rpx;font-weight: 500;margin-left: 45rpx;">选择Wi-Fi</text>
			</view>
			<text style="font-size: 27rpx;font-weight: 500;margin-left: 45rpx;" >该设备只支持使用2.4GHz Wi-Fi连接使用</text>
		</view>
		<view style="margin-top: 130rpx;">
			<view style="padding-left: 45rpx; padding-right: 45rpx;margin-top: 50rpx;">
				<uni-easyinput :disabled='true' :placeholder="SSIDfill" placeholderStyle="color:#101010"
					suffixIcon="custom" @iconClick="iconClick"></uni-easyinput>
			</view>
			<view style="padding-left: 45rpx; padding-right: 45rpx;margin-top: 50rpx;">
				<uni-easyinput type="password" v-model="password" @input="passInput"></uni-easyinput>
			</view>
		</view>
		<view style="margin-top: 30rpx;padding-left: 70rpx;">
			<text :style="{'font-size':25+'rpx','font-weight':500,'color':fontcolor}">Wi-Fi密码必须至少为8个字符</text>
		</view>
		<view style="position: absolute;bottom: 50rpx;width: 100%;text-align: center;">
			<button class="en-button"  @click="Next" :style="{'color': nextfontcolor,'background-color':nextbackgroundcolor}">确 认</button>
		</view>
		<uni-popup ref="popup" type="bottom">
			<view class="popup-nav">
				<view class="nav-title">
					<text>选择WiFi</text>
				</view>
				<view>
					<scroll-view scroll-y="true" style="background-color:#fff;height: 700rpx; flex-direction:column;"
						refresher-enabled="true" @refresherrefresh="PullRefresh" :refresher-triggered="triggered">
						<view style="padding-bottom: 40rpx;">
							<view style="margin-top: 30rpx;margin-left: 50rpx;">
								<text style="font-size: 25rpx;font-weight: 430;">可添加设备的Wi-Fi(2.4GHz)</text>
							</view>
							<view style="height: 10rpx;"></view>
							<view v-for="(wifiInfo,index) in wifiList" :key="index"
								style="margin-top: 50rpx;display: flex;flex-direction:row">
								<view
									style="height: 30rpx;width: 100%;margin-top: 20rpx; margin-left: 45rpx;font-size: 32rpx;font-weight: 500;"
									@click="checkWiFi(wifiInfo.SSID)">
									{{wifiInfo.SSID}}
								</view>
								<view>
									<image :src="wifiInfo.icon"
										style="height: 40rpx; width: 40rpx;margin-right: 45rpx;margin-top: 16rpx;">
									</image>
								</view>
							</view>
						</view>
					</scroll-view>
				</view>
			</view>
		</uni-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				wifiList: [],
				//下拉刷新状态
				triggered: false,
				//SSID 框填充
				SSIDfill: "Wi-Fi名称",
				//密码
				password: '',
				//密码提示
				fontcolor: "#5e5e5e",
				//确认按钮背景色
				nextbackgroundcolor: "#d5e8f4",
				//确认按钮字体颜色S
				nextfontcolor: "#959595"
			}
		},
		methods: {
			open: function() {
				//this.$refs.popup.open();
			},
			back: function() {
				uni.navigateBack();
			},
			iconClick() {
				this.$refs.popup.open();
			},
			checkWiFi(ssid) {
				this.SSIDfill = ssid;
				this.$refs.popup.close();
			},
			passInput(pass) {
				if(pass.length<8){
					this.nextbackgroundcolor = "#d5e8f4";
					this.nextfontcolor = "#959595";
				}else{
					this.nextbackgroundcolor = "#79aaf4";
					this.nextfontcolor = "#ffffff";
				}
				this.password = pass;
			},
			Next() {
				if (this.password.length < 8&&this.password.length!=0) {
					this.fontcolor = 'red';
				}
			},
			getWifiList() {
				//uni.hideLoading(); //关闭加载框
				let tmpList = []
				// 主窗体  
				var MainActivity = plus.android.runtimeMainActivity()
				// 上下文    
				var Context = plus.android.importClass('android.content.Context')
				// 导入WIFI管理 和 WIFI 信息 的class    
				plus.android.importClass("android.net.wifi.WifiManager")
				plus.android.importClass("android.net.wifi.WifiInfo")
				plus.android.importClass("android.net.wifi.ScanResult")
				plus.android.importClass("java.util.ArrayList")
				// 获取 WIFI 管理实例
				var wifiManager = MainActivity.getSystemService(Context.WIFI_SERVICE)
				var resultList = wifiManager.getScanResults();
				for (var i = 0; i < resultList.size(); i++) {
					if (resultList.get(i).plusGetAttribute('SSID').length > 0) {
						let obj = {
							//SSID
							SSID: resultList.get(i).plusGetAttribute('SSID'),
							//BSSID
							BSSID: resultList.get(i).plusGetAttribute("BSSID"),
							//信号质量
							level: resultList.get(i).plusGetAttribute("level"),
							//频段
							frequency: resultList.get(i).plusGetAttribute("frequency"),
							//安全信息
							capabilities: resultList.get(i).plusGetAttribute("capabilities")
						}
						tmpList.push(obj);
					}
				}
				tmpList = tmpList.filter(function(item, index, self) {
					return self.indexOf(item) == index;
				});
				this.wifiList = tmpList // 存放wifi列表 
				this.setWifiIcon();
			},
			setWifiIcon() {
				let arr = [];
				for (let s of this.wifiList) {
					if (s.level >= -63) {
						s.icon = "../../static/wifi1.png";
					} else if (s.level < -63 && s.level >= -78) {
						s.icon = "../../static/wifi2.png";
					} else if (s.level < -78 && s.level >= -85) {
						s.icon = "../../static/wifi3.png";
					} else if (s.level < -85 && s.level >= -89) {
						s.icon = "../../static/wifi4.png";
					} else {
						continue;
					}
					arr.push(s);
				}
				arr.sort((a, b) => {
					return b.level - a.level;
				})
				this.wifiList = arr;
			},
			PullRefresh() {
				this.triggered = true;
				let that = this;
				uni.getLocation({ //授权定位后才能获取wifi
					type: 'wgs84',
					success: function(res) {
						that.getWifiList();
					},
					fail: function(error) {
						console.log((error, 'xx'))
						uni.hideLoading(); //关闭加载框  
						uni.showToast({
							title: '获取WiFi失败!',
							icon: 'none'
						})
					},
				});
				let t = setTimeout(function() {
					clearTimeout(t);
					that.triggered = false;
				}, 3000)
			}
		},
		onLoad() {

		},
		//进入页面时扫描WiFi
		onReady() {
			let that = this;
			let t = setTimeout(function() {
				that.$refs.popup.open();
				clearTimeout(t);
			}, 500);
			uni.getLocation({ //授权定位后才能获取wifi
				type: 'wgs84',
				success: function(res) {
					that.getWifiList();
					// //uni.hideLoading(); //关闭加载框  
					// let tmpList = []
					// // 主窗体  
					// var MainActivity = plus.android.runtimeMainActivity()
					// // 上下文    
					// var Context = plus.android.importClass('android.content.Context')
					// // 导入WIFI管理 和 WIFI 信息 的class    
					// plus.android.importClass("android.net.wifi.WifiManager")
					// plus.android.importClass("android.net.wifi.WifiInfo")
					// plus.android.importClass("android.net.wifi.ScanResult")
					// plus.android.importClass("java.util.ArrayList")
					// // 获取 WIFI 管理实例
					// var wifiManager = MainActivity.getSystemService(Context.WIFI_SERVICE)
					// var resultList = wifiManager.getScanResults();
					// for (var i = 0; i < resultList.size(); i++) {
					// 	if (resultList.get(i).plusGetAttribute('SSID').length > 0) {
					// 		let obj = {
					// 			//SSID
					// 			SSID: resultList.get(i).plusGetAttribute('SSID'),
					// 			//BSSID
					// 			BSSID: resultList.get(i).plusGetAttribute("BSSID"),
					// 			//信号质量
					// 			level: resultList.get(i).plusGetAttribute("level"),
					// 			//频段
					// 			frequency: resultList.get(i).plusGetAttribute("frequency"),
					// 			//安全信息
					// 			capabilities: resultList.get(i).plusGetAttribute("capabilities")
					// 		}
					// 		tmpList.push(obj);
					// 	}
					// }
					// tmpList = tmpList.filter(function(item, index, self) {
					// 	return self.indexOf(item) == index;
					// });
					// that.wifiList = tmpList // 存放wifi列表 
					// that.setWifiIcon();
					//uni.hideLoading(); //关闭加载框  
					// that.testList.push(that.wfName) //test1  
				},
				fail: function(error) {
					console.log((error, 'xx'))
					uni.hideLoading(); //关闭加载框  
					uni.showToast({
						title: '获取WiFi失败!',
						icon: 'none'
					})
				},
			});
		}
	}
</script>

<style lang="scss">
	.popup-nav {
		height: 800rpx;
		background-color: #fff;
		border-top-left-radius: 50rpx;
		border-top-right-radius: 50rpx;

		.nav-title {
			border-top-left-radius: 50rpx;
			border-top-right-radius: 50rpx;
			height: 100rpx;
			background-color: #fff;
			width: 100%;
			font-weight: bold;
			font-size: 30rpx;
			text-align: center;
			line-height: 100rpx;
			background-color: #fff;
		}
	}

	.en-button {
		border-radius: 40rpx;
		font-size: 35rpx;
		color: #2b2b2b;
		font-weight: bolder;
		background-color: #ace5ff;
		box-shadow: 0px 1px 3px 2px rgba($color: #a5a4a4, $alpha: 0.2);
		height: 80rpx;
		width: 670rpx;
	}

	//去除button边框
	button::after {
		border: none;
	}
</style>