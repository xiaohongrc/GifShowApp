package com.hongenit.gifshowapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import com.hongenit.gifshowapp.util.UserUtil
import com.hongenit.gifshowapp.util.logWarn

/**
 * Created by hongenit on 2019/6/30.
 * desc: 全局变量
 */

@SuppressLint("StaticFieldLeak")
object GlobalParam {

    private const val TAG: String = "GlobalParam"
    const val isDebug = true
    lateinit var handler: Handler
    lateinit var context: Context
        private set

    fun initialize(c: Context) {
        context = c
        handler = Handler(Looper.getMainLooper())
    }


    /**
     * 返回当前应用的包名。
     */
    val packageName: String
        get() = context.packageName

    /**
     * 获取AndroidManifest.xml文件中，<application>标签下的meta-data值。
     *
     * @param key
     *  <application>标签下的meta-data健
     */
    fun getApplicationMetaData(key: String): String? {
        var applicationInfo: ApplicationInfo? = null
        try {
            applicationInfo = context.packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            logWarn(TAG, e.message, e)
        }
        if (applicationInfo == null) return ""
        return applicationInfo.metaData.getString(key)
    }


    // 是否登录
    fun isLogin(): Boolean {
        return UserUtil.userId.isNotEmpty() && UserUtil.token.isNotEmpty()
    }


    fun saveLoginStatus(userId: String, token: String) {
        UserUtil.saveUserId(userId)
        UserUtil.saveToken(token)
    }

    // 保存我的信息
    fun saveUserInfo(user: User) {
        UserUtil.saveAvatar(user.avatar)
        UserUtil.saveGender(user.gender)
        UserUtil.saveDescription(user.description)
        UserUtil.saveNickname(user.nickname)
        UserUtil.saveBirthday(user.birthday)
    }

    // 退出登录，清空登录状态
    fun logOut() {
        UserUtil.saveUserId(null)
        UserUtil.saveToken(null)
    }


}
