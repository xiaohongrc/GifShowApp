package com.hongenit.gifshowapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.response.BaseResponse
import com.hongenit.gifshowapp.network.response.FetchMyInfo
import com.hongenit.gifshowapp.network.response.UpdateNicknameResponse
import com.hongenit.gifshowapp.util.UserUtil
import kotlinx.android.synthetic.main.activity_main.*


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
                if (signUser.nickname.isNullOrEmpty()) {
                    // 设置昵称
                    updateNickname()

                } else {
                    refreshUserInfo(signUser)
                }
                GlobalParam.saveUserInfo(signUser)
            }

            override fun onFailure(e: Exception) {

            }

        })
    }

    private fun updateNickname() {
        UpdateNicknameResponse.getResponse(UserUtil.userId,UserUtil.token,UserUtil.nickname,object : Callback{
            override fun onResponse(baseResponse: BaseResponse) {
                val user = (baseResponse as UpdateNicknameResponse).signUser
                GlobalParam.saveUserInfo(user)
            }

            override fun onFailure(e: Exception) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
