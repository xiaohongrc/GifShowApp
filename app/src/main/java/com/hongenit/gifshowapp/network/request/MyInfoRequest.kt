package com.hongenit.gifshowapp.network.request

import com.hongenit.gifshowapp.network.NetworkConst
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.Request
import com.hongenit.gifshowapp.network.response.FetchMyInfo
import com.hongenit.gifshowapp.util.UserModel

/**
 * Created by hongenit on 2019/6/30.
 * desc:
 */
class MyInfoRequest : Request() {

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
        inFlight(FetchMyInfo::class.java)
        setListener(callback)
    }

    private val URL = NetworkConst.BASE_URL + "user/fetch_my_info"

}
