package com.hongenit.gifshowapp.detail

import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.NetworkConst
import com.hongenit.gifshowapp.network.Request
import com.hongenit.gifshowapp.util.UserModel

/**
 * Created by Xiaohong on 2019-07-24.
 * desc:
 */
class FetchGifDetailRequest(var feed_id: String) : Request() {

    override fun url(): String {
        return URL
    }

    override fun method(): Int {
        return GET
    }

    override fun params(): Map<String, String>? {
        val params = HashMap<String, String>()
        params[NetworkConst.USER_ID] = UserModel.userId
        params[NetworkConst.TOKEN] = UserModel.token
        params[NetworkConst.FEED_ID] = feed_id
        return params
    }

    override fun listen(callback: Callback?) {
        inFlight(FetchGifDetailResponse::class.java)
        setListener(callback)
    }

    private val URL = NetworkConst.BASE_URL + "gifs/fetch_gif_detail"

}