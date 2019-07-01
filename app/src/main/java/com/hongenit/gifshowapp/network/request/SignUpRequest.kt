package com.hongenit.gifshowapp.network.request

import com.hongenit.gifshowapp.network.NetworkConst
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.Request
import com.hongenit.gifshowapp.network.SignUpBaseResponse

/**
 * Created by hongenit on 2019/6/30.
 * desc:
 */
class SignUpRequest : Request() {
    private var email: String = ""
    private var pwd: String = ""
    fun email(email: String): SignUpRequest {
        this.email = email
        return this
    }

    fun pwd(pwd: String): SignUpRequest {
        this.pwd = pwd
        return this
    }

    override fun url(): String {
        return URL
    }

    override fun method(): Int {
        return GET
    }

    override fun params(): Map<String, String>? {
        val params = HashMap<String, String>()
        params[NetworkConst.ACCOUNT] = email
        params[NetworkConst.PWD] = pwd
        return params
    }

    override fun listen(callback: Callback?) {
        inFlight(SignUpBaseResponse::class.java)
        setListener(callback)
    }

    private val URL = NetworkConst.BASE_URL + "user/regist"

}
