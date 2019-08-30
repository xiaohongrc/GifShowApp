package com.hongenit.gifshowapp

import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.view.View
import com.hongenit.gifshowapp.account.LoginActivity
import com.hongenit.gifshowapp.util.UserModel

/**
 * 闪屏Activity界面，在这里进行程序初始化操作。
 *
 * @author guolin
 * @since 17/2/16
 */
class SplashActivity : BaseActivity() {

    /**
     *
     * 记录进入SplashActivity的时间。
     */
    var enterTime: Long = 0

    /**
     * 判断是否正在跳转或已经跳转到下一个界面。
     */
    var isForwarding = false

    var hasNewVersion = false

    lateinit var logoView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTime = System.currentTimeMillis()
        delayToForward()
        startInit()
    }


    override fun onBackPressed() {
        // 屏蔽手机的返回键
    }


    /** 初始化。*/
    private fun startInit() {
        forwardToNextActivity()

    }

    /**
     * 设置闪屏界面的最大延迟跳转，让用户不至于在闪屏界面等待太久。
     */
    private fun delayToForward() {
        Thread(Runnable {
            SystemClock.sleep(MAX_WAIT_TIME.toLong())
            forwardToNextActivity()
        }).start()
    }

    /**
     * 跳转到下一个Activity。如果在闪屏界面停留的时间还不足规定最短停留时间，则会在这里等待一会，保证闪屏界面不至于一闪而过。
     */
    @Synchronized
    open fun forwardToNextActivity() {
        if (!isForwarding) { // 如果正在跳转或已经跳转到下一个界面，则不再重复执行跳转
            isForwarding = true
            val currentTime = System.currentTimeMillis()
            val timeSpent = currentTime - enterTime
            if (timeSpent < MIN_WAIT_TIME) {
                SystemClock.sleep(MIN_WAIT_TIME - timeSpent)
            }
            runOnUiThread {
                if (UserModel.isLogin()) {
                    MainActivity.actionStart(this)
                } else {
                    LoginActivity.actionStart(this)
                }
                finish()
            }
        }
    }

    companion object {


        private const val TAG = "SplashActivity"

        /**
         * 应用程序在闪屏界面最短的停留时间。
         */
        const val MIN_WAIT_TIME = 200

        /**
         * 应用程序在闪屏界面最长的停留时间。
         */
        const val MAX_WAIT_TIME = 5000
    }

}
