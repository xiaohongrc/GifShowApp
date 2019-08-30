package com.hongenit.gifshowapp.network.response

import com.google.gson.annotations.SerializedName
import com.hongenit.gifshowapp.User
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.request.MyInfoRequest
import com.hongenit.gifshowapp.network.request.SignUpRequest

/**
 * Created by hongenit on 2019/6/30.
 * desc:
 */
class FetchMyInfo : BaseResponse() {

    @SerializedName("data")
    var signUser = User()

    companion object {
        fun getResponse( callback: Callback) {
            MyInfoRequest()
                .listen(callback)
        }
    }

}