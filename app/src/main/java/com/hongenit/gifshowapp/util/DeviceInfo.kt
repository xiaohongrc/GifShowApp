package com.hongenit.gifshowapp.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.hongenit.gifshowapp.GlobalParam


/**
 * 提供所有与设备相关的信息。
 *
 */
object DeviceInfo {

    /**
     * 获取当前设备屏幕的宽度，以像素为单位。
     *
     * @return 当前设备屏幕的宽度。
     */
    val screenWidth: Int
        get() {
            val windowManager = GlobalParam.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val metrics = DisplayMetrics()
            if (AndroidVersion.hasJellyBeanMR1()) {
                windowManager.defaultDisplay.getRealMetrics(metrics)
            } else {
                windowManager.defaultDisplay.getMetrics(metrics)
            }
            return metrics.widthPixels
        }

    /**
     * 获取当前设备屏幕的高度，以像素为单位。
     *
     * @return 当前设备屏幕的高度。
     */
    val screenHeight: Int
        get() {
            val windowManager = GlobalParam.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val metrics = DisplayMetrics()
            if (AndroidVersion.hasJellyBeanMR1()) {
                windowManager.defaultDisplay.getRealMetrics(metrics)
            } else {
                windowManager.defaultDisplay.getMetrics(metrics)
            }
            return metrics.heightPixels
        }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    val statusBarHeight: Int
        get() {
            val resources = GlobalParam.context.resources
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            return resources.getDimensionPixelSize(resourceId)
        }


}
