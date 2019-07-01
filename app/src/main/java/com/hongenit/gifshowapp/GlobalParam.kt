package com.hongenit.gifshowapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import com.hongenit.gifshowapp.util.logWarn

/**
 * Created by hongenit on 2019/6/30.
 * desc: 全局变量
 */

@SuppressLint("StaticFieldLeak")
object GlobalParam {

    private val TAG: String = "GlobalParam"
    val isDebug = true
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

}
