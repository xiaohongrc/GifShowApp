package com.hongenit.gifshowapp.network

import com.hongenit.gifshowapp.network.request.SignUpRequest

/**
 * Created by hongenit on 2019/6/30.
 * desc:
 */
class SignUpBaseResponse : BaseResponse() {

    companion object {
        fun getResponse(email: String, pwd: String, callback: Callback) {
            SignUpRequest()
                    .email(email)
                    .pwd(pwd)
                    .listen(callback)
        }
    }

}