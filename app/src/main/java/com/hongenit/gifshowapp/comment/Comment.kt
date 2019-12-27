package com.hongenit.gifshowapp.comment

import com.google.gson.annotations.SerializedName
import com.hongenit.gifshowapp.bean.Model

/**
 * Comment实体类，用于存储服务器返回的评论数据。
 *
 */
class Comment : Model() {

    override val modelId: String
        get() = commentId

    @SerializedName("comment_id")
    var commentId = ""

    var content = ""

    @SerializedName("user_id")
    var userId = ""

    var nickname = ""

    var avatar = ""

    @SerializedName("bg_image")
    var bgImage = ""

    @SerializedName("post_date")
    var postDate = 0L

    @SerializedName("goods_count")
    var goodsCount = 0

    @SerializedName("good_already")
    var isGoodAlready = false

}
