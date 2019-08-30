/*
 * Copyright (C) guolin, Suzhou Quxiang Inc. Open source codes for study only.
 * Do not use for commercial purpose.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
