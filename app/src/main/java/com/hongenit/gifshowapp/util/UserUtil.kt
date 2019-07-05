package com.hongenit.gifshowapp.util


/**
 * 获取当前登录用户信息的工具类。
 *
 * @author guolin
 * @since 17/3/10
 */
object UserUtil {

    val userId: String
        get() = SharedUtil.read(SharedPrefConst.User.UID, "")

    val token: String
        get() = SharedUtil.read(SharedPrefConst.User.TOKEN, "")


    val nickname: String
        get() = SharedUtil.read(SharedPrefConst.User.NICKNAME, "")

    val avatar: String
        get() = SharedUtil.read(SharedPrefConst.User.AVATAR, "")

    val bgImage: String
        get() = SharedUtil.read(SharedPrefConst.User.BG_IMAGE, "")

    val description: String
        get() = SharedUtil.read(SharedPrefConst.User.DESCRIPTION, "")


    fun saveUserId(userId: String?) {
        if (userId != null && userId.isNotBlank()) {
            SharedUtil.save(SharedPrefConst.User.UID, userId)
        } else {
            SharedUtil.clear(SharedPrefConst.User.UID)
        }
    }

    fun saveToken(token: String?) {
        if (token != null && token.isNotBlank()) {
            SharedUtil.save(SharedPrefConst.User.TOKEN, token)
        } else {
            SharedUtil.clear(SharedPrefConst.User.TOKEN)
        }
    }

    fun saveNickname(nickname: String?) {
        if (nickname != null && nickname.isNotBlank()) {
            SharedUtil.save(SharedPrefConst.User.NICKNAME, nickname)
        } else {
            SharedUtil.clear(SharedPrefConst.User.NICKNAME)
        }
    }

    fun saveAvatar(avatar: String?) {
        if (avatar != null && avatar.isNotBlank()) {
            SharedUtil.save(SharedPrefConst.User.AVATAR, avatar)
        } else {
            SharedUtil.clear(SharedPrefConst.User.AVATAR)
        }
    }

    fun saveBirthday(birthday: String?) {
        if (birthday != null && birthday.isNotBlank()) {
            SharedUtil.save(SharedPrefConst.User.BIRTHDAY, birthday)
        } else {
            SharedUtil.clear(SharedPrefConst.User.BIRTHDAY)
        }
    }

    fun saveGender(gender: Int?) {
        if (gender != null) {
            SharedUtil.save(SharedPrefConst.User.GENDER, gender)
        } else {
            SharedUtil.clear(SharedPrefConst.User.GENDER)
        }
    }

    fun saveDescription(description: String?) {
        if (description != null && description.isNotBlank()) {
            SharedUtil.save(SharedPrefConst.User.DESCRIPTION, description)
        } else {
            SharedUtil.clear(SharedPrefConst.User.DESCRIPTION)
        }
    }

}
