package com.hongenit.gifshowapp.network.response

import com.google.gson.annotations.SerializedName
import com.hongenit.gifshowapp.bean.WaterFallFeed
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.request.HotGifRequest
import com.hongenit.gifshowapp.network.request.NewGifRequest

/**
 * Created by hongenit on 2019/7/19.
 * desc:
 */
class FetchNewResponse : BaseResponse() {

    @SerializedName("data")
    var gifs: List<WaterFallFeed> = ArrayList()

    companion object {
        fun getResponse(pageNum: Int, callback: Callback) {
            NewGifRequest(pageNum)
                .listen(callback)
        }
    }

}