package com.hongenit.gifshowapp.collect

import com.google.gson.annotations.SerializedName
import com.hongenit.gifshowapp.bean.WaterFallFeed
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.response.BaseResponse

/**
 * Created by Xiaohong on 2019-07-31.
 * desc: 请求收藏列表
 */
class FetchCollectsResponse : BaseResponse() {
    @SerializedName("data")
    var gifs: List<WaterFallFeed> = ArrayList()

    companion object {
        fun getResponse(callback: Callback) {
            FetchCollectsRequest()
                .listen(callback)
        }
    }


}