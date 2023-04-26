<template>
	<!-- <uni-nav-bar background-color="#4bd8ff" fixed="true" height="150rpx" left-width="0" right-width="0"> -->
		<view class="nar" style="width:100%;height: 150rpx;background-color: #cbebff;">
			<!-- <view style="width:100rpx; heigth:50rpx; background-color:red; right: 50rpx;">
			
			</view> -->
			<view class="nar-menu">
				<image src="../../static/menu.png" style="width: 56rpx; height: 56rpx;"></image>
			</view>
			<view class="nar-scan">
				<image src="../../static/scan.png" style="width: 43rpx; height: 43rpx;" @click="scan_click" ></image>
			</view>
			<view class="nar-add" >
				<image src="../../static/add.png" style="width: 66rpx; height: 66rpx;" @click="getwifiInfo"></image>
			</view>
			
		</view>
	<!-- </uni-nav-bar> -->
	<view class="main">
		<!-- <uni-nav-bar background-color="#4bd8ff" fixed="true" height="150rpx" left-width="0" right-width="0"> -->
			<!-- <view style="height: 100rpx;width:700rpx;"></view> -->
		<!-- </uni-nav-bar> -->
		<scroll-view scroll-y="true" :refresher-threshold="80" refresher-background="linear-gradient(to bottom,#ccecff,#ffffff)" show-scrollbar="true" refresher-enabled="true" :style="{'height':scroll_height}">
			<!-- <view style="height: 150rpx;background-color: #ccecff;z-index: 9;width: 100%;position: fixed;"></view> -->
			<view style="height: 500rpx;"></view>
			<view class="grid">
				<uni-grid :column="2" :highlight="false" :showBorder="false" :square="false">
					<uni-grid-item v-for="(item,index) in itemlist" :index="index" :key="index">
						<view class="grid-item">
							<view class="grid-item-cn" :style="{'background-color': (index===click_index?color_2:color_1) , 'margin': index===click_index?20+'rpx':15+'rpx'}"  @touchstart="touchstart(index)" @touchend="touchend(index)" >
								<view style=" margin-top: 25rpx;">
									<image style="width: 80rpx;height: 80rpx;margin-left: 27rpx;" :src='item.icon'></image>
								</view>												
								<view class="grid-info">
									<view style="font-weight: bold;">{{'客厅'+item.name}}</view>
									<view style="display: flex; flex-direction: row;">客厅|{{item.status?'在线':'离线'}}
									<view class="status" :style="{'background-color':item.status?(item.on_off?status_on:status_off):status_outline}"></view></view>
								</view>
							</view>
						</view>
					</uni-grid-item>
				</uni-grid>
			</view>
		</scroll-view>
	</view>
</template>

<script>
//import logonVue from '../logon/logon.vue';
	export default {
		data() {
			return {
				click_index:-1,
				status_on:'#00ffa2',
				status_off:'#ff8001',
				status_outline:'#a0a0a0',
				scroll_height:"",
				itemlist: [{
					name: '台灯',
					icon: "../../static/tv.png",
					status: true,
					on_off: true,
				}, {
					name: '台灯',
					icon: "../../static/light.png",
					// 在线状态
					status: false,
					// 开关状态
					on_off: false,
				}, {
					name: '电视',
					icon: "../../static/tv.png",
					status: false,
					on_off: false,
				}, {
					name: '电视',
					icon: "../../static/tv.png",
					status: false,
					on_off: false,
				}, {
					name: '台灯',
					icon: "../../static/light.png",
					status: true,
					on_off: false,
				}, {
					name: '空气净化器',
					icon: "../../static/air.png",
					status: false,
					on_off: false,
				}, {
					name: '台灯',
					icon: "../../static/light.png",
					status: true,
					on_off: true,
				}, {
					name: '台灯',
					icon: "../../static/light.png",
					status: false,
					on_off: false,
				}, {
					name: '台灯',
					icon: "../../static/light.png",
					status: true,
					on_off: false,
				}, {
					name: '空气净化器',
					icon: "../../static/air.png",
					status: false,
					on_off: false,
				}, {
					name: '电视',
					icon: "../../static/tv.png",
					status: false,
					on_off: false,
				}, {
					name: '空气净化器',
					icon: "../../static/air.png",
					status: true,
					on_off: true,
				}, {
					name: '台灯',
					icon: "../../static/light.png",
					status: false,
					on_off: false,
				}
			],
			color_1:"#ffffff",
			color_2:"#e7e7e7",
				// item_background_color:"$uni-bg-color",
			};
		},
		onReady() {
			let that = this;
			uni.getSystemInfo({
				success: (res)=> {
					that.scroll_height = parseInt(res.windowHeight*(750/res.windowWidth)-150)+"rpx";
				}
			})
		},
		methods:{
			item_click:function(index){
				this.click_index = Number(index);
			},
			touchstart:function(index){
				this.click_index = Number(index);
			},
			touchend:function(index){
				this.click_index = -1;
			},
			scan_click:function(){
				uni.scanCode({
					autoDecodeCharSet:true,
					success: (res) => {
						console.log(res.result);
						uni.showModal({
							content:res.result,
							success: (res) => {
								if(res){
									uni.navigateTo({
										url:"/pages/config_net/config_net"
									})
								}
							}
						})
						uni.vibrateLong();
					}
				})
			},
			getwifiInfo:function(){
				console.log(this.getWiFiIP());
				uni.navigateTo({
					url:"/pages/config_net/config_net"
				})
				
				// uni.startWifi({});
				// console.log("dianji");
				// uni.getConnectedWifi({
				// 	partialInfo:true,
				// 	success:function(res){
				// 		console.log(res);
				// 	}
				// })	
			},
			getWiFiIP(){
				// MainActivity
				var MainActivity = plus.android.runtimeMainActivity()
				// Context
				var Context = plus.android.importClass('android.content.Context')
				// WiFi 相关包  
				plus.android.importClass("android.net.wifi.WifiManager")
				plus.android.importClass("android.net.wifi.WifiInfo")
				plus.android.importClass("android.net.wifi.ScanResult")
				plus.android.importClass("java.util.ArrayList")
				// WiFi 管理实例
				var wifiManager = MainActivity.getSystemService(Context.WIFI_SERVICE)
				// 开启 WiFi
				// wifiManager.setWifiEnabled(true)
				// 当前连接 WiFi 信息  
				var wifiInfo = wifiManager.getConnectionInfo()
				console.log(wifiInfo.toString())
				var ipAddress = wifiInfo.getIpAddress()
				var ip = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "." + (ipAddress >> 16 & 0xff) + "." + (
					ipAddress >> 24 & 0xff))
				console.log(ip)
				return ip
			},
		}
	}
</script>

<style lang="scss">
	.main{
		background-image:linear-gradient(to bottom,#cbebff,#ffffff);
	}
	
	.nar{
		position: relative;
		display: flex;
		flex-direction: row;
		.nar-add{
			position: absolute;
			right: 30rpx;
			margin-top: 75rpx;
		}
		.nar-scan{
			position: absolute;
			right: 120rpx;
			margin-top: 86rpx;
		}
		.nar-menu{
			position: absolute;
			left: 30rpx;
			margin-top: 82rpx;
		}
		
	}
	.grid {
		padding: 25rpx;
	}

	.grid-item {
		display: flex;
		height: 230rpx;
		// padding: 15rpx;
		.grid-item-cn {
			width: 100%;
			//background-color: white;
			border-radius: 10px;
			box-shadow: 0px 1px 10px 2px rgba($color: #a5a4a4, $alpha: 0.1);
			
			.grid-info{
				font-size: 25rpx;
				padding-left: 20rpx;
				// margin-top: 5%;
				.status{
					width: 14rpx; height: 14rpx;
					border-radius: 7px; 
					background-color: green;
					margin-top: 10rpx;
					margin-left: 10rpx;
					
				}
			}
		}
	}
</style>