package com.hongenit.gifshowapp.comment

import com.google.gson.annotations.SerializedName
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.response.BaseResponse

/**
 * Created by Xiaohong on 2019-07-24.
 * desc:
 */
class FetchCommentsResponse : BaseResponse() {
    @SerializedName("data")
    var comments: List<Comment> = ArrayList()

    companion object {
        fun getResponse(feed_id: String, pageSize: Int, callback: Callback) {
            FetchCommentsRequest(feed_id, pageSize)
                .listen(callback)
        }
    }


}