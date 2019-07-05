package com.hongenit.gifshowapp.network.response

import com.google.gson.annotations.SerializedName
import com.hongenit.gifshowapp.User
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.request.SignUpRequest

/**
 * Created by hongenit on 2019/6/30.
 * desc:
 */
class SignUpBaseResponse : BaseResponse() {

    @SerializedName("user_id")
    var user_id = ""

    @SerializedName("token")
    var token = ""


    companion object {
        fun getResponse(email: String, pwd: String, callback: Callback) {
            SignUpRequest()
                .email(email)
                .pwd(pwd)
                .listen(callback)
        }
    }

}