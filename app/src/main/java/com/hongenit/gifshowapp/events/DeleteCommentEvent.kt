package com.hongenit.gifshowapp.events


/**
 * 删除评论的事件消息。
 *
 */
class DeleteCommentEvent : MessageEvent() {

    var commentId: String = ""

    var feedId: String = ""
}
