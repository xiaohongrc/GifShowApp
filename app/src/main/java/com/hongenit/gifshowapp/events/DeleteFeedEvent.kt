package com.hongenit.gifshowapp.events

/**
 * 删除Feed的事件消息。
 *
 */
class DeleteFeedEvent : MessageEvent() {

    var feedId: String = ""

    var type: Int = 0

    companion object {

        const val DELETE_FROM_USER_HOME_PAGE = 0

        const val DELETE_FROM_FOLLOWING_PAGE = 1
    }
}
