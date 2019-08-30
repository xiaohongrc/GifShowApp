package com.hongenit.gifshowapp.util

import android.os.Build
import android.os.Environment
import android.text.TextUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

/**
 * 以更加可读的方式提供Android系统版本号的判断方法。
 */
object AndroidVersion {

    /**
     * 判断当前手机系统版本API是否是16以上。
     * @return 16以上返回true，否则返回false。
     */
    fun hasJellyBean(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
    }

    /**
     * 判断当前手机系统版本API是否是17以上。
     * @return 17以上返回true，否则返回false。
     */
    fun hasJellyBeanMR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
    }

    /**
     * 判断当前手机系统版本API是否是18以上。
     * @return 18以上返回true，否则返回false。
     */
    fun hasJellyBeanMR2(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
    }

    /**
     * 判断当前手机系统版本API是否是19以上。
     * @return 19以上返回true，否则返回false。
     */
    fun hasKitkat(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    }

    /**
     * 判断当前手机系统版本API是否是21以上。
     * @return 21以上返回true，否则返回false。
     */
    fun hasLollipop(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    /**
     * 判断当前手机系统版本API是否是22以上。
     * @return 22以上返回true，否则返回false。
     */
    fun hasLollipopMR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1
    }

    /**
     * 判断当前手机系统版本API是否是23以上。
     * @return 23以上返回true，否则返回false。
     */
    fun hasMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    /**
     * 判断当前手机系统版本API是否是24以上。
     * @return 24以上返回true，否则返回false。
     */
    fun hasNougat(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }


    private val KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code"
    private val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private val KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage"

    val isMiUI8OrLower: Boolean
        get() {
            try {
                val prop = BuildProperties.newInstance()
                val isMiUI = (prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                        || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                        || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null)
                if (isMiUI) {
                    val versionName = prop.getProperty(KEY_MIUI_VERSION_NAME, null)
                    if (!TextUtils.isEmpty(versionName) && versionName!!.startsWith("V")) {
                        val versionNumber = versionName.replace("V", "")
                        val versionCode = Integer.parseInt(versionNumber)
                        if (versionCode < 9) {
                            return true
                        }
                    }
                }
                return false
            } catch (e: Exception) {
                return false
            }

        }

}


internal class BuildProperties @Throws(IOException::class) private constructor() {

    private val properties: Properties

    val isEmpty: Boolean
        get() = properties.isEmpty

    init {
        properties = Properties()
        properties.load(FileInputStream(File(Environment.getRootDirectory(), "build.prop")))
    }

    fun containsKey(key: Any): Boolean {
        return properties.containsKey(key)
    }

    fun containsValue(value: Any): Boolean {
        return properties.containsValue(value)
    }

    fun entrySet(): MutableSet<MutableMap.MutableEntry<Any, Any>> {
        return properties.entries
    }

    fun getProperty(name: String): String {
        return properties.getProperty(name)
    }

    fun getProperty(name: String, defaultValue: String?): String? {
        return properties.getProperty(name, defaultValue)
    }

    fun keys(): Enumeration<Any> {
        return properties.keys()
    }

    fun keySet(): Set<Any> {
        return properties.keys
    }

    fun size(): Int {
        return properties.size
    }

    fun values(): Collection<Any> {
        return properties.values
    }

    companion object {

        @Throws(IOException::class)
        fun newInstance(): BuildProperties {
            return BuildProperties()
        }
    }

}