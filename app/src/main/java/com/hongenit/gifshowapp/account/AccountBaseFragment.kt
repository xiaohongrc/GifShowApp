package com.hongenit.gifshowapp.account

import com.hongenit.gifshowapp.BaseFragment
import com.hongenit.gifshowapp.MainActivity
import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.extension.logDebug
import com.hongenit.gifshowapp.extension.showToast
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.response.BaseResponse
import com.hongenit.gifshowapp.network.response.SignInResponse
import com.hongenit.gifshowapp.util.GlobalUtil
import com.hongenit.gifshowapp.util.SharedPrefConst
import com.hongenit.gifshowapp.util.SharedUtil
import com.hongenit.gifshowapp.util.UserModel

open class AccountBaseFragment : BaseFragment() {


    fun onLoginSucceed() {
        SharedUtil.save(SharedPrefConst.User.HAD_LOGIN, true)
        forwardToMainActivity()
        activity?.finish()
    }

    private fun forwardToMainActivity() {
        activity?.let { MainActivity.actionStart(it) }
    }


    // 检查用户名是否合规
    fun checkAccountAndPwd(email: String, pwd: String): Boolean {
        if (email.isEmpty() || pwd.isEmpty()) {
            showToast(getString(R.string.account_or_pwd_canot_empty))
            return false
        }

        if (email.length < 6) {
            showToast(getString(R.string.account_is_too_short))
            return false
        }

        if (!GlobalUtil.validateAccountStr(email)) {
            showToast(getString(R.string.account_is_illegal))
            return false
        }

        return true
    }


}
