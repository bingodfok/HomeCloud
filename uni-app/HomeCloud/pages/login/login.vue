<template>
	<view>
		<view class="top"></view>
		<view class="login-title">使用验证码登录</view>
		<view class="phone-put">
			<input class="phone" type="number" maxlength="11" @input="phone_input" placeholder="请输入手机号码"/>
		</view>
		<view class="line-1"></view>
		<view class="captcha-put">
			<view class="captcha-parent">
				<input class="captcha" type="number" maxlength="6" @input="input" placeholder="请输入验证码"/>
			</view>
			<view class="sub">
				<text @click="get_captcha">{{sendtext}}</text>
			</view>
		</view>
		<view class="line-1"></view>
		<view class="sub-img">
			<image class="submit" :src="imgurl" @click="OnClick"></image>
		</view>
		<view class="text">
			<view class="password-login">密码登录</view>
			<view class="register">注册</view>
		</view>
		<!-- <view class="bar">我已阅读用户协议</view> -->
	</view>
</template>

<script>
import router from './router';
	export default {
		data() {
			return {
				imgurl: "../../static/submit1.png",
				number:	0,
				btn_status:false,
				captcha_st:true,
				sendtext:"获取验证码",
				formdata: { 
					phone: "",
					captcha: ""
				},
				timeout: 60
			};
		},
		onLoad() {
			
		},
		methods: {
			OnClick(){
				if(this.btn_status){
					uni.request({
						url: router.data().captcha_login,
						data:this.formdata,
						header:{"ContentType":"application.json"},
						method:'POST',
						success: (res) => {
							
						},
					
					})
				}
			},
			input:function(data){
				let val = data.detail.value;
				if(val.length==6){
					this.formdata.captcha = val;
					this.btn_status = true;
					this.imgurl = "../../static/submit2.png";
				}else{
					this.imgurl = "../../static/submit1.png";
					this.btn_status = false;
				}
			},
			phone_input:function(data){
				let val = data.detail.value;
				this.formdata.phone = val;
			},
			get_captcha:function(){
				if(this.captcha_st){
					let that = this;
					this.captcha_st = false;
					let timer = setInterval(function(){
						that.timeout--;
						that.sendtext = that.timeout+"s后重新获取";
						if(that.timeout <= 0){
							clearInterval(timer);
							that.timeout = 60;
							that.sendtext = "获取验证码";
							that.captcha_st = true;
						}
					},1000)
				}
			}
		}
	}
</script>

<style lang="scss">
	.top{
		height: 250rpx;
	}
	.login-title{
		width: 750rpx;
		height: auto;
		font-size: 50rpx;
		font-style: $uni-border-color;
		font-weight: bold;
		text-align: center;
	}
	.phone-put{
		height: 80rpx;
		margin-top: 100rpx;
		padding-top: 40rpx;
		padding-right: 50rpx;
		padding-left: 30rpx;
		.phone{
			font-size: 35rpx;
			font-weight: bolder;
			margin-left: 100rpx;
		}
	}
	.captcha-put{
		height: 80rpx;
		padding-top: 40rpx;
		padding-right: 50rpx;
		padding-left: 30rpx;
		display: flex;
		flex-direction:row;
		.captcha-parent{
			width: 60%;
			.captcha{
				width: 300rpx;
				font-size: 35rpx;
				font-weight: bolder;
				margin-left: 100rpx;
				margin-right: 200rpx;
			}
		}
		.sub{
			font-size: 30rpx;
			height: 50rpx;
			width: 40%;
			font-weight: bold;
			color: blue;
			margin-left: 10rpx;
			margin-right: 55rpx;
			margin-top: 7rpx;
			text-align: right;
		}

	}
	.line-1{
		height: 1rpx;
		background-color: #959595;
		margin-top: 1rpx;
		margin-left: 100rpx;
		margin-right: 100rpx;
		margin-bottom: 1rpx;
	}
	
	.sub-img{
		margin-top: 100rpx;
		text-align: center;
		.submit{
			margin-top: 50rpx;
			height: 135rpx;
			width: 135rpx;
		}
	}
	
	.text{
		margin-top: 50rpx;
		display: flex;
		flex-direction: row;
		.password-login{
			width: 50%;
			padding-left: 110rpx;
			font-size: 30rpx;
		}
		.register{
			width: 50%;
			padding-left: 312rpx;
			font-size: 30rpx;
		}
	}
	
</style>
