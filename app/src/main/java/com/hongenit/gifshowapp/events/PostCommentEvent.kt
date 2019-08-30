package com.hongenit.gifshowapp.events

/**
 * 发布评论的事件消息。
 *
 */
class PostCommentEvent : MessageEvent() {

    var commentId: String = ""

    var feedId: String = ""
}
