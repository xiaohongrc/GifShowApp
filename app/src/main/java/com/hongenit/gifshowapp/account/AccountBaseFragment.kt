package com.hongenit.gifshowapp.account

import com.hongenit.gifshowapp.BaseFragment
import com.hongenit.gifshowapp.GlobalParam
import com.hongenit.gifshowapp.MainActivity
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.response.BaseResponse
import com.hongenit.gifshowapp.network.response.FetchMyInfo
import com.hongenit.gifshowapp.util.UserUtil

open class AccountBaseFragment : BaseFragment() {


    private fun forwardToMainActivity() {
        activity?.let { MainActivity.actionStart(it) }
    }

    /**
     * 获取我的信息
     */
    fun fetchMyInfo() {
        FetchMyInfo.getResponse(UserUtil.userId, UserUtil.token, object : Callback {
            override fun onResponse(baseResponse: BaseResponse) {
                val user = (baseResponse as FetchMyInfo).signUser
                println("user info = $user")
                GlobalParam.saveUserInfo(user)
                forwardToMainActivity()
            }

            override fun onFailure(e: Exception) {
                println("fetchMyInfo onFailure$e")
            }
        })


    }

}
