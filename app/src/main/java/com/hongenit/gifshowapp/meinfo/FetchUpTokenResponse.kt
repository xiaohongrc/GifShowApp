package com.hongenit.gifshowapp.meinfo

import com.google.gson.annotations.SerializedName
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.response.BaseResponse

/**
 * Created by hongenit on 2019/8/14.
 * desc:
 */
class FetchUpTokenResponse : BaseResponse() {

    @SerializedName("data")
    var uptoken = ""

    companion object {
        fun getResponse(fileKey: String, callback: Callback) {
            FetchUpTokenRequest(fileKey)
                .listen(callback)
        }
    }

}