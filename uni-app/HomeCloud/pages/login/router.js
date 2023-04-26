import root_router from "../root_router"

export default {
	data() {
		return{
			captcha_login : root_router.data().rooturl+"/system/common/login",
		}
	}
}