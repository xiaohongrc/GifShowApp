package com.hongenit.gifshowapp.collect

import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.NetworkConst
import com.hongenit.gifshowapp.network.Request
import com.hongenit.gifshowapp.util.UserModel

/**
 * Created by Xiaohong on 2019-07-31.
 * desc:
 */
class FetchCollectsRequest : Request() {

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
        return params
    }

    override fun listen(callback: Callback?) {
        inFlight(FetchCollectsResponse::class.java)
        setListener(callback)
    }

    private val URL = NetworkConst.BASE_URL + "collect/fetch_collects"

}