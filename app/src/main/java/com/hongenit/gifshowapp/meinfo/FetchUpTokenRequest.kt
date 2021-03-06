package com.hongenit.gifshowapp.meinfo

import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.NetworkConst
import com.hongenit.gifshowapp.network.Request
import com.hongenit.gifshowapp.util.UserModel

/**
 * Created by hongenit on 2019/6/30.
 * desc: 获取七牛云的上传token
 */
class FetchUpTokenRequest(var fileKey: String) : Request() {
    override fun url(): String {
        return URL
    }

    override fun method(): Int {
        return GET
    }

    override fun params(): Map<String, String>? {
        val params = HashMap<String, String>()
        params[NetworkConst.USER_ID] = UserModel.userId
        params[NetworkConst.TOKEN] = UserModel.token
        params[NetworkConst.FILE_KEY] = fileKey
        return params
    }

    override fun listen(callback: Callback?) {
        inFlight(FetchUpTokenResponse::class.java)
        setListener(callback)
    }

    private val URL = NetworkConst.BASE_URL + "user/fetch_up_token"

}
