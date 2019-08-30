package com.hongenit.gifshowapp.account

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.extension.logDebug
import com.hongenit.gifshowapp.extension.showToast
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.response.BaseResponse
import com.hongenit.gifshowapp.network.response.SignInResponse
import com.hongenit.gifshowapp.util.GlobalUtil
import com.hongenit.gifshowapp.util.UserModel
import kotlinx.android.synthetic.main.frag_sign_in.*

/**
 * Created by hongenit on 2019/6/30.
 * desc:
 */
class SignInFragment : AccountBaseFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.frag_sign_in, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {

        btnSignIn.setOnClickListener {
            doSignIn()
        }

        tvSignUp.setOnClickListener {
            (activity as LoginActivity).showSignUp()
        }


        etPwd.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    doSignIn()
                }

                return false
            }
        })


    }


    // 登录操作
    private fun doSignIn() {
        val email = etEmail.text.toString().trim()
        val pwd = etPwd.text.toString().trim()

        if (checkAccountAndPwd(email, pwd)) {
            SignInResponse.getResponse(email, pwd, object : Callback {
                override fun onResponse(baseResponse: BaseResponse) {
                    val signInResponse = baseResponse as SignInResponse
                    if (signInResponse.status == BaseResponse.STATUS_OK && signInResponse.user_id > 0) {
                        UserModel.saveLoginStatus(signInResponse.user_id.toString(), signInResponse.token)
                        onLoginSucceed()
                        logDebug("SignInResponse success")
                    } else {
                        // 用户名或密码错误。
                        showToast(getString(R.string.error_account_pwd))
                        logDebug("code = ${signInResponse.status}   msg = ${signInResponse.msg}")
                    }
                }

                override fun onFailure(e: Exception) {
                    // 网络异常
                    showToast(getString(R.string.network_error))
                    logDebug("login network error")
                }
            })
        }
    }


}