package com.hongenit.gifshowapp.util

import com.hongenit.gifshowapp.User
import com.hongenit.gifshowapp.events.UserInfoUpdateEvent
import com.hongenit.gifshowapp.extension.logDebug
import org.greenrobot.eventbus.EventBus


/**
 * 获取当前登录用户信息的工具类。
 *
 */
object UserModel {


    // 未登录时userid为0
    const val NO_USER_ID = "0"

    var mUser: User = User()

    var userId: String = NO_USER_ID

    var token: String = ""


    fun initUserModel() {
        userId = SharedUtil.read(SharedPrefConst.User.UID, NO_USER_ID)
        mUser.user_id = userId
        token = SharedUtil.read(SharedPrefConst.User.TOKEN, "")
        mUser.token = token
        mUser.nickname = SharedUtil.read(SharedPrefConst.User.NICKNAME, "")
        mUser.avatar = SharedUtil.read(SharedPrefConst.User.AVATAR, "")
        mUser.birthday = SharedUtil.read(SharedPrefConst.User.BIRTHDAY, "")
        mUser.gender = SharedUtil.read(SharedPrefConst.User.GENDER, 0)
        mUser.description = SharedUtil.read(SharedPrefConst.User.DESCRIPTION, "")
    }


    private fun saveUserId(userId: String?) {
        if (userId != null && userId.isNotBlank()) {
            SharedUtil.save(SharedPrefConst.User.UID, userId)
        } else {
            SharedUtil.clear(SharedPrefConst.User.UID)
        }
    }

    private fun saveToken(token: String?) {
        if (token != null && token.isNotBlank()) {
            SharedUtil.save(SharedPrefConst.User.TOKEN, token)
        } else {
            SharedUtil.clear(SharedPrefConst.User.TOKEN)
        }
    }

    private fun saveNickname(nickname: String?) {
        if (nickname != null && nickname.isNotBlank()) {
            SharedUtil.save(SharedPrefConst.User.NICKNAME, nickname)
        } else {
            SharedUtil.clear(SharedPrefConst.User.NICKNAME)
        }
    }

    private fun saveAvatar(avatar: String?) {
        if (avatar != null && avatar.isNotBlank()) {
            SharedUtil.save(SharedPrefConst.User.AVATAR, avatar)
        } else {
            SharedUtil.clear(SharedPrefConst.User.AVATAR)
        }
    }

    private fun saveBirthday(birthday: String?) {
        if (birthday != null && birthday.isNotBlank()) {
            SharedUtil.save(SharedPrefConst.User.BIRTHDAY, birthday)
        } else {
            SharedUtil.clear(SharedPrefConst.User.BIRTHDAY)
        }
    }

    private fun saveGender(gender: Int?) {
        if (gender != null) {
            SharedUtil.save(SharedPrefConst.User.GENDER, gender)
        } else {
            SharedUtil.clear(SharedPrefConst.User.GENDER)
        }
    }

    private fun saveDescription(description: String?) {
        if (description != null && description.isNotBlank()) {
            SharedUtil.save(SharedPrefConst.User.DESCRIPTION, description)
        } else {
            SharedUtil.clear(SharedPrefConst.User.DESCRIPTION)
        }
    }

    fun saveUser(user: User) {
        this.mUser = user
        saveUserId(user.user_id.toString())
        saveToken(user.token)
        saveAvatar(user.avatar)
        saveGender(user.gender)
        saveDescription(user.description)
        saveNickname(user.nickname)
        saveBirthday(user.birthday)

        EventBus.getDefault().post(UserInfoUpdateEvent())

        logDebug("save user = $user")
    }


    // 获取当前登录用户
    fun getSignInUser(): User {
        return mUser
    }


    // 退出登录，清空登录状态
    fun logOut() {
        saveUserId(null)
        saveToken(null)
        saveAvatar(null)
        saveGender(null)
        saveDescription(null)
        saveNickname(null)
        saveBirthday(null)
        initUserModel()
    }

    // 保存登录状态
    fun saveLoginStatus(userId: String, token: String) {
        saveUserId(userId)
        saveToken(token)
    }


    // 是否登录
    fun isLogin(): Boolean {
        userId = SharedUtil.read(SharedPrefConst.User.UID, NO_USER_ID)
        token = SharedUtil.read(SharedPrefConst.User.TOKEN, "")
        return userId != NO_USER_ID && token.isNotEmpty()
    }

}
