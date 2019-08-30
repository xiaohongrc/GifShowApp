package com.hongenit.gifshowapp.comment

import com.google.gson.annotations.SerializedName
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.response.BaseResponse

/**
 * Created by Xiaohong on 2019-07-24.
 * desc: 发布评论
 */
class PostCommentResponse : BaseResponse() {
    @SerializedName("comment_id")
    var commentId = ""

    companion object {
        fun getResponse(feed_id: String, content: String, callback: Callback) {
            PostCommentsRequest(feed_id, content)
                .listen(callback)
        }
    }


}