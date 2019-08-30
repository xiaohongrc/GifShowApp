package com.hongenit.gifshowapp.util

import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import com.hongenit.gifshowapp.GlobalParam
import com.hongenit.gifshowapp.extension.logWarn
import java.util.*

/**
 * 获取各项基础数据的工具类。
 *
 * @author hong
 * @since 19/6/30
 */
object Utility {

    private const val TAG = "Utility"

    private var deviceSerial: String? = null

    /**
     * 获取设备的品牌和型号，如果无法获取到，则返回Unknown。
     * @return 会以此格式返回数据：品牌 型号。
     */
    val deviceName: String
        get() {
            var deviceName = Build.BRAND + " " + Build.MODEL
            if (TextUtils.isEmpty(deviceName)) {
                deviceName = "unknown"
            }
            return deviceName
        }

    /**
     * 获取当前App的版本号。
     * @return 当前App的版本号。
     */
    val appVersion: String
        get() {
            var version = ""
            try {
                val packageManager = GlobalParam.context.packageManager
                val packInfo = packageManager.getPackageInfo(GlobalParam.packageName, 0)
                version = packInfo.versionName
            } catch (e: Exception) {
                logWarn("getAppVersion", e.message, e)
            }

            if (TextUtils.isEmpty(version)) {
                version = "unknown"
            }
            return version
        }

    /**
     * 获取App网络请求验证参数，用于辨识是不是官方渠道的App。
     */
    val appSign: String
        get() {
//            return MD5.encrypt(SignUtil.getAppSignature() + appVersion)
            return ""
        }

    /**
     * 获取设备的序列号。如果无法获取到设备的序列号，则会生成一个随机的UUID来作为设备的序列号，UUID生成之后会存入缓存，
     * 下次获取设备序列号的时候会优先从缓存中读取。
     * @return 设备的序列号。
     */
    @SuppressLint("HardwareIds")
    fun getDeviceSerial(): String {
        if (deviceSerial == null) {
            var deviceId: String? = null
            val appChannel =  GlobalParam.getApplicationMetaData("APP_CHANNEL")
            if ("google" != appChannel || "samsung" != appChannel) {
                try {
                    deviceId = Settings.Secure.getString(GlobalParam.context.contentResolver, Settings.Secure.ANDROID_ID)
                } catch (e: Exception) {
                    logWarn(TAG, "get android_id with error", e)
                }
                if (!TextUtils.isEmpty(deviceId) && deviceId!!.length < 255) {
                    deviceSerial = deviceId
                    return deviceSerial.toString()
                }
            }
            var uuid = SharedUtil.read(SharedPrefConst.UUID, "")
            if (!TextUtils.isEmpty(uuid)) {
                deviceSerial = uuid
                return deviceSerial.toString()
            }
            uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase()
            SharedUtil.save(SharedPrefConst.UUID, uuid)
            deviceSerial = uuid
            return deviceSerial.toString()
        } else {
            return deviceSerial.toString()
        }
    }

}
