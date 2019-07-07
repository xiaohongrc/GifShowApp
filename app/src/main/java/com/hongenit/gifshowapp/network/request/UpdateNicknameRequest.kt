package com.hongenit.gifshowapp.network.request

import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.NetworkConst
import com.hongenit.gifshowapp.network.Request
import com.hongenit.gifshowapp.network.response.SignInResponse
import com.hongenit.gifshowapp.network.response.UpdateNicknameResponse

/**
 * Created by hongenit on 2019/6/30.
 * desc:
 */
class UpdateNicknameRequest(var userId: String, var token: String, var nickname: String) : Request() {
    override fun url(): String {
        return URL
    }

    override fun method(): Int {
        return GET
    }

    override fun params(): Map<String, String>? {
        val params = HashMap<String, String>()
        params[NetworkConst.USER_ID] = userId
        params[NetworkConst.TOKEN] = token
        params[NetworkConst.NICKNAME] = nickname
        return params
    }

    override fun listen(callback: Callback?) {
        inFlight(UpdateNicknameResponse::class.java)
        setListener(callback)
    }

    private val URL = NetworkConst.BASE_URL + "user/update"

}
