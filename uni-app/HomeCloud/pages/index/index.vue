<template>
	<view class="content">
		<text>{{title}}</text>
		<view>
			<form>
				昵称：<input class="name" type="text"/>
			</form>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				title: 'Hello'
			}
		},
		onLoad() {
			uni.startPullDownRefresh()
			setTimeout(function () {
						console.log('start pulldown');
						uni.stopPullDownRefresh();
					}, 5000);
			
			uni.request({
				url:"http://localhost:8080/system/common/register",
				method:'POST',
				success: (res) => {
					console.log(res.code);
				}
			})
			
		},
		methods: {
			
		}
	}
</script>

<style lang="scss">
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.logo {
		height: 200rpx;
		width: 200rpx;
		margin-top: 200rpx;
		margin-left: auto;
		margin-right: auto;
		margin-bottom: 50rpx;
	}

	.text-area {
		display: flex;
		justify-content: center;
	}

	.title {
		font-size: 36rpx;
		color: #8f8f94;
	}
</style>
