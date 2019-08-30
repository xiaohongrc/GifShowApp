package com.hongenit.gifshowapp.events

import com.hongenit.gifshowapp.bean.BaseFeed
import com.hongenit.gifshowapp.bean.WaterFallFeed


/**
 * Feed点赞的事件消息。
 *
 */
class LikeFeedEvent : MessageEvent() {


    var feed: WaterFallFeed = WaterFallFeed()

    var feedId: String = ""

    var likesCount: Int = 0

    var type: Int = 0

    var from: Int = 0

    companion object {

        val LIKE_FEED = 0

        val UNLIKE_FEED = 1

        val FROM_FEED_DETAIL = 0

        val FROM_WORLD = 1

        val FROM_FOLLOWING = 2

        val FROM_HOT = 3

        val FROM_USER_HOME = 4
    }
}
