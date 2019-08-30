package com.hongenit.gifshowapp.detail

import com.google.gson.annotations.SerializedName
import com.hongenit.gifshowapp.comment.Comment
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.response.BaseResponse

/**
 * Created by Xiaohong on 2019-07-24.
 * desc:
 */
class FetchGifDetailResponse : BaseResponse() {
    @SerializedName("likes_count")
    var likesCount: Int = 0

    @SerializedName("is_liked")
    var isLiked: Boolean = false

    @SerializedName("is_following")
    var isFollowing: Boolean = false

    @SerializedName("data")
    var comments: List<Comment> = mutableListOf()

    companion object {
        fun getResponse(feed_id: String, callback: Callback) {
            FetchGifDetailRequest(feed_id)
                .listen(callback)
        }
    }


}