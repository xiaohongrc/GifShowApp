package com.hongenit.gifshowapp.extension

import android.annotation.SuppressLint
import android.widget.Toast
import com.hongenit.gifshowapp.GlobalParam

/**
 * Created by Xiaohong on 2019-07-09.
 * desc:
 */

var toast: Toast? = null

@SuppressLint("ShowToast")
@JvmOverloads
fun showToast(content: String, duration: Int = Toast.LENGTH_SHORT) {
    if (toast == null) {
        toast = Toast.makeText(GlobalParam.context, content, duration)
    } else {
        toast?.setText(content)
    }
    toast?.show()

}


/**
 * 切换到主线程后弹出Toast信息。此方法不管是在子线程还是主线程中，都可以成功弹出Toast信息。
 *
 * @param content
 * Toast中显示的内容
 * @param duration
 * Toast显示的时长
 */
@SuppressLint("ShowToast")
@JvmOverloads
fun showToastOnUiThread(content: String, duration: Int = Toast.LENGTH_SHORT) {
    GlobalParam.handler.post {
        if (toast == null) {
            toast = Toast.makeText(GlobalParam.context, content, duration)
        } else {
            toast?.setText(content)
        }
        toast?.show()
    }
}
