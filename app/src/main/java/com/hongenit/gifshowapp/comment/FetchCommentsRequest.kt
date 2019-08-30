package com.hongenit.gifshowapp.comment

import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.NetworkConst
import com.hongenit.gifshowapp.network.Request
import com.hongenit.gifshowapp.util.UserModel

/**
 * Created by Xiaohong on 2019-07-24.
 * desc:
 */
class FetchCommentsRequest(var feed_id: String, var pageSize: Int) : Request() {

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
        params[NetworkConst.PAGE_SIZE] = pageSize.toString()
        return params
    }

    override fun listen(callback: Callback?) {
        inFlight(FetchCommentsResponse::class.java)
        setListener(callback)
    }

    private val URL = NetworkConst.BASE_URL + "comments/fetch_comments"

}