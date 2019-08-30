package com.hongenit.gifshowapp.meinfo

import com.google.gson.annotations.SerializedName
import com.hongenit.gifshowapp.User
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.response.BaseResponse

/**
 * Created by hongenit on 2019/7/7.
 * desc:
 */
class UpdateNicknameResponse : BaseResponse() {

    @SerializedName("data")
    var signUser = User()

    companion object {
        fun getResponse(nickname: String, callback: Callback) {
            UpdateNicknameRequest(nickname)
                .listen(callback)
        }
    }

}