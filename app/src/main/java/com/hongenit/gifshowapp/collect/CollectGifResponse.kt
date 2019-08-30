package com.hongenit.gifshowapp.collect

import com.google.gson.annotations.SerializedName
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.response.BaseResponse

/**
 * Created by Xiaohong on 2019-07-24.
 * desc: 收藏或取消收藏的请求
 */
class CollectGifResponse : BaseResponse() {
    @SerializedName("data")
    var collect = false

    companion object {
        fun getResponse(feed_id: String, userId:String,collect:Boolean, callback: Callback) {
            CollectGifRequest(feed_id, userId, collect)
                .listen(callback)
        }
    }


}