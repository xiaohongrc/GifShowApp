package com.hongenit.gifshowapp.util

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.hongenit.gifshowapp.GlobalParam

/**
 * SharedPreferences工具类，提供简单的封装接口，简化SharedPreferences的用法。
 *
 */
object SharedUtil {
    val mPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(GlobalParam.context)

    /**
     * 存储boolean类型的键值对到SharedPreferences文件当中。
     * @param key
     * 存储的键
     * @param value
     * 存储的值
     */
    fun save(key: String, value: Boolean) {

        mPref.edit().putBoolean(key, value).apply()
    }

    /**
     * 存储float类型的键值对到SharedPreferences文件当中。
     * @param key
     * 存储的键
     * @param value
     * 存储的值
     */
    fun save(key: String, value: Float) {
        mPref.edit().putFloat(key, value).apply()
    }

    /**
     * 存储int类型的键值对到SharedPreferences文件当中。
     * @param key
     * 存储的键
     * @param value
     * 存储的值
     */
    fun save(key: String, value: Int) {
        mPref.edit().putInt(key, value).apply()
    }

    /**
     * 存储long类型的键值对到SharedPreferences文件当中。
     * @param key
     * 存储的键
     * @param value
     * 存储的值
     */
    fun save(key: String, value: Long) {
        mPref.edit().putLong(key, value).apply()
    }

    /**
     * 存储String类型的键值对到SharedPreferences文件当中。
     * @param key
     * 存储的键
     * @param value
     * 存储的值
     */
    fun save(key: String, value: String) {
        mPref.edit().putString(key, value).apply()
    }

    /**
     * 从SharedPreferences文件当中读取参数传入键相应的boolean类型的值。
     * @param key
     * 读取的键
     * @param defValue
     * 如果读取不到值，返回的默认值
     * @return boolean类型的值，如果读取不到，则返回默认值
     */
    fun read(key: String, defValue: Boolean): Boolean {

        return mPref.getBoolean(key, defValue)
    }

    /**
     * 从SharedPreferences文件当中读取参数传入键相应的float类型的值。
     * @param key
     * 读取的键
     * @param defValue
     * 如果读取不到值，返回的默认值
     * @return float类型的值，如果读取不到，则返回默认值
     */
    fun read(key: String, defValue: Float): Float {

        return mPref.getFloat(key, defValue)
    }

    /**
     * 从SharedPreferences文件当中读取参数传入键相应的int类型的值。
     * @param key
     * 读取的键
     * @param defValue
     * 如果读取不到值，返回的默认值
     * @return int类型的值，如果读取不到，则返回默认值
     */
    fun read(key: String, defValue: Int): Int {

        return mPref.getInt(key, defValue)
    }

    /**
     * 从SharedPreferences文件当中读取参数传入键相应的long类型的值。
     * @param key
     * 读取的键
     * @param defValue
     * 如果读取不到值，返回的默认值
     * @return long类型的值，如果读取不到，则返回默认值
     */
    fun read(key: String, defValue: Long): Long {

        return mPref.getLong(key, defValue)
    }

    /**
     * 从SharedPreferences文件当中读取参数传入键相应的String类型的值。
     * @param key
     * 读取的键
     * @param defValue
     * 如果读取不到值，返回的默认值
     * @return String类型的值，如果读取不到，则返回默认值
     */
    fun read(key: String, defValue: String): String {

        return mPref.getString(key, defValue) ?: defValue
    }

    /**
     * 判断SharedPreferences文件当中是否包含指定的键值。
     * @param key
     * 判断键是否存在
     * @return 键已存在返回true，否则返回false。
     */
    operator fun contains(key: String): Boolean {

        return mPref.contains(key)
    }

    /**
     * 清理SharedPreferences文件当中传入键所对应的值。
     * @param key
     * 想要清除的键
     */
    fun clear(key: String) {
        mPref.edit().remove(key).apply()
    }

    /**
     * 将SharedPreferences文件中存储的所有值清除。
     */
    fun clearAll() {
        mPref.edit().clear().apply()
    }

}