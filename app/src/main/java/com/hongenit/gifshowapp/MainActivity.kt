package com.hongenit.gifshowapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.response.BaseResponse
import com.hongenit.gifshowapp.network.response.FetchMyInfo
import com.hongenit.gifshowapp.util.UserUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        btnLogout.setOnClickListener {
            GlobalParam.logOut()
        }

        FetchMyInfo.getResponse(UserUtil.userId, UserUtil.token, object : Callback {
            override fun onResponse(baseResponse: BaseResponse) {
                val signUser = (baseResponse as FetchMyInfo).signUser
                GlobalParam.saveUserInfo(signUser)
                refreshUserInfo(signUser)

            }

            override fun onFailure(e: Exception) {

            }

        })
    }

    private fun refreshUserInfo(user: User) {
        runOnUiThread {
            tvUserInfo.text = user.toString()
        }
    }


    companion object {

        private const val TAG = "MainActivity"

        fun actionStart(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }


}
