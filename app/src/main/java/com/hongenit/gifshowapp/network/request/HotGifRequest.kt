package com.hongenit.gifshowapp.network.request

import com.hongenit.gifshowapp.network.NetworkConst
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.Request
import com.hongenit.gifshowapp.network.response.FetchHotResponse
import com.hongenit.gifshowapp.util.UserModel

/**
 * Created by hongenit on 2019/7/19.
 * desc: 热门gif列表请求
 */
class HotGifRequest(var pageNum: Int) : Request() {

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
        params[NetworkConst.PAGE] = pageNum.toString()
        return params
    }

    override fun listen(callback: Callback?) {
        inFlight(FetchHotResponse::class.java)
        setListener(callback)
    }

    private val URL = NetworkConst.BASE_URL + "gifs/fetch_hot"

}
