package com.hongenit.gifshowapp.events

/**
 * 修改用户信息的事件消息。
 *
 */
class ModifyUserInfoEvent : MessageEvent() {

    var modifyNickname = false

    var modifyDescription = false

    var modifyAvatar = false

    var modifyBgImage = false

}
