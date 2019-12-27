package com.hongenit.gifshowapp.bean

import android.os.Parcel
import com.google.gson.annotations.SerializedName


/**
 * 瀑布流Feed的实体类。
 */
open class WaterFallFeed : BaseFeed() {

    @SerializedName("comments_count")
    var commentsCount: Int = 0


    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)
        dest?.writeInt(commentsCount)
    }

}