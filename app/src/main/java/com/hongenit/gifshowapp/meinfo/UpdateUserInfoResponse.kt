package com.hongenit.gifshowapp.meinfo

import com.google.gson.annotations.SerializedName
import com.hongenit.gifshowapp.User
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.response.BaseResponse

/**
 * Created by hongenit on 2019/8/9.
 * desc:
 */
class UpdateUserInfoResponse : BaseResponse() {

    @SerializedName("data")
    var signUser = User()

    companion object {
        fun getResponse(nickname: String, description: String, avatarFilePath: String, callback: Callback) {
            UpdateUserInfoRequest(nickname, description, avatarFilePath)
                .listen(callback)
        }
    }

}