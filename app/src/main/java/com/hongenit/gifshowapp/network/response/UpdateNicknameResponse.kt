package com.hongenit.gifshowapp.network.response

import com.google.gson.annotations.SerializedName
import com.hongenit.gifshowapp.User
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.request.SignInRequest
import com.hongenit.gifshowapp.network.request.SignUpRequest
import com.hongenit.gifshowapp.network.request.UpdateNicknameRequest

/**
 * Created by hongenit on 2019/7/7.
 * desc:
 */
class UpdateNicknameResponse : BaseResponse() {

    @SerializedName("data")
    var signUser = User()

    companion object {
        fun getResponse(userId: String, token: String, nickname: String, callback: Callback) {
            UpdateNicknameRequest(userId, token, nickname)
                .listen(callback)
        }
    }

}