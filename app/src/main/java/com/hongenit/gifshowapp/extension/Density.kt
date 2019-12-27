package com.hongenit.gifshowapp.extension

import com.hongenit.gifshowapp.GlobalParam


/**
 * 根据手机的分辨率将dp转成为px
 */
fun dp2px(dp: Float): Int {
    val scale = GlobalParam.context.resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

/**
 * 根据手机的分辨率将px转成dp
 */
fun px2dp(px: Float): Int {
    val scale = GlobalParam.context.resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}
