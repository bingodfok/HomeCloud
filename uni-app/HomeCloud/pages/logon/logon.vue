<template>
	<view>
		<view style="height: 250rpx;"></view>
		<view style="text-align: center; font-size: 50rpx;font-weight: bold;">新用户注册</view>
		<view class="phone" style="margin-top: 160rpx; padding-left: 100rpx; padding-right: 100rpx;">
			<input class="phone_input" @input="phoneInput" maxlength="11" placeholder="请输入手机号码" />
		</view>
		<view style="height: 1rpx; background-color: #959595; margin-left: 100rpx; margin-right: 100rpx;"></view>
		<view class="captcha"
			style="margin-top: 60rpx; padding-left: 100rpx; padding-right: 100rpx;display: flex;flex-direction:row;">
			<input class="captcha-input" @input="captchaInput" maxlength="6" placeholder="请输入验证码" />
			<view class="captcha-btn" style="color: blue;font-size: 30rpx;font-weight: bold;" @click="get_captcha">{{sendText}}</view>
		</view>
		<view style="height: 1rpx; background-color: #959595; margin-left: 100rpx; margin-right: 100rpx;"></view>
		<view style="text-align: center;">
			<image :src="imgurl" style="width: 135rpx;height: 135rpx;margin-top: 150rpx"></image>
		</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				sendText: "获取验证码",
				imgurl: "/static/submit1.png",
				get_captcha_st: true,
				timeout:60
			};
		},
		onLoad() {

		},
		methods:{
			phoneInput: (data) => {

			},
			captchaInput: function(data) {
				let val = data.detail.value;
				if (val.length == 6) {
					this.imgurl = "/static/submit2.png";
				} else {
					this.imgurl = "/static/submit1.png";
				}
			},
			get_captcha: function(){
				if(this.get_captcha_st){
					this.get_captcha_st = false;
					let that = this;
					let timer = setInterval(function(){
						that.timeout--;
						that.sendText = that.timeout+"s后重新获取";
						if(that.timeout<=0){
							that.get_captcha_st = true;
							clearInterval(timer);
							that.sendText = "获取验证码";
							that.timeout = 60;
						}
					},1000)
				}
			}
		}
	}
</script>

<style lang="scss">
	.phone {
		font-weight: bold;
		padding-bottom: 25rpx;
		font-size: 35rpx;
	}

	.captcha {
		font-weight: bold;
		font-size: 35rpx;
		padding-bottom: 25rpx;

		.captcha-btn {
			text-align: right;
			width: 250rpx;
		}
	}
</style>
