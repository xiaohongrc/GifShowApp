package com.hongenit.gifshowapp.network.response

import com.google.gson.annotations.SerializedName
import com.hongenit.gifshowapp.User
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.request.SignInRequest
import com.hongenit.gifshowapp.network.request.SignUpRequest

/**
 * Created by hongenit on 2019/6/30.
 * desc:
 */
class SignInResponse : BaseResponse() {

    @SerializedName("user_id")
    var user_id = 0

    @SerializedName("token")
    var token = ""

    companion object {
        fun getResponse(email: String, pwd: String, callback: Callback) {
            SignInRequest()
                .email(email)
                .pwd(pwd)
                .listen(callback)
        }
    }

}