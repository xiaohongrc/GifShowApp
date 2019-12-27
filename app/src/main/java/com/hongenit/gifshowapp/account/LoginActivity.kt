package com.hongenit.gifshowapp.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.hongenit.gifshowapp.BaseActivity
import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.UmengEvent
import com.hongenit.gifshowapp.util.GlobalUtil
import com.hongenit.gifshowapp.util.SharedPrefConst
import com.hongenit.gifshowapp.util.SharedUtil

/**
 * Created by hongenit on 2019/6/29.
 * desc:
 */
class LoginActivity : BaseActivity() {


    companion object {

        private const val TAG = "LoginActivity"

        fun actionStart(activity: Activity) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
        }
    }


    private var mSignInFragment: SignInFragment? = null
    private var mSignUpFragment: SignUpFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        GlobalUtil.setStatusBarFullTransparent(this)
        // 如果注册过则显示登录页
        if (SharedUtil.read(SharedPrefConst.User.HAD_LOGIN, false)) {
            showSignIn()
        } else {
            showSignUp()
        }

    }


    fun showSignIn() {
        UmengEvent.showSignIn()
        val transaction = supportFragmentManager.beginTransaction()
        if (mSignInFragment == null) {
            mSignInFragment = SignInFragment()
        }
        mSignInFragment?.let {
            if (it.isAdded) {
                transaction.replace(R.id.fragmentContainer, it)
            } else {
                transaction.add(R.id.fragmentContainer, it)
            }
        }
        transaction.commit()
    }


    // 显示注册页面。
    fun showSignUp() {
        UmengEvent.showSignUp()
        val transaction = supportFragmentManager.beginTransaction()
        if (mSignUpFragment == null) {
            mSignUpFragment = SignUpFragment()
        }
        mSignUpFragment?.let {
            if (it.isAdded) {
                transaction.replace(R.id.fragmentContainer, it)
            } else {

                transaction.add(R.id.fragmentContainer, it)
            }
        }
        transaction.commit()
    }


}
