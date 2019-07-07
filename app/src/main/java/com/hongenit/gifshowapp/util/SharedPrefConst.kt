package com.hongenit.gifshowapp.util

/**
 * Created by hongenit on 2019/6/30.
 * desc:
 */
interface SharedPrefConst {
    companion object {
        val UUID = "uuid"
    }

    interface User {
        companion object {
            const val UID = "uid"

            const val TOKEN = "token"

            const val NICKNAME = "nickname"

            const val AVATAR = "avatar"

            const val BG_IMAGE = "bg_image"

            const val DESCRIPTION = "description"

            const val BIRTHDAY = "birthday"

            const val GENDER = "gender"

        }

    }

}