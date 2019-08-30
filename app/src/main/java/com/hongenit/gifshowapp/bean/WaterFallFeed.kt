package com.hongenit.gifshowapp.bean

import com.google.gson.annotations.SerializedName


/**
 * 瀑布流Feed的实体类。
 */
open class WaterFallFeed : BaseFeed() {

    @SerializedName("comments_count")
    var commentsCount: Int = 0

}