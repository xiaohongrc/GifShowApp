package com.hongenit.gifshowapp.network.request

import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.NetworkConst
import com.hongenit.gifshowapp.network.Request
import com.hongenit.gifshowapp.network.response.SignInResponse

/**
 * Created by hongenit on 2019/6/30.
 * desc:
 */
class SignInRequest : Request() {
    private var email: String = ""
    private var pwd: String = ""
    fun email(email: String): SignInRequest {
        this.email = email
        return this
    }

    fun pwd(pwd: String): SignInRequest {
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
        inFlight(SignInResponse::class.java)
        setListener(callback)
    }

    private val URL = NetworkConst.BASE_URL + "user/login"

}
