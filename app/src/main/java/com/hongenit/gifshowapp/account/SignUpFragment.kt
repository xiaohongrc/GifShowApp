package com.hongenit.gifshowapp.account

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.UmengEvent
import com.hongenit.gifshowapp.extension.logDebug
import com.hongenit.gifshowapp.extension.showToast
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.response.BaseResponse
import com.hongenit.gifshowapp.network.response.SignUpBaseResponse
import com.hongenit.gifshowapp.util.UserModel
import kotlinx.android.synthetic.main.frag_sign_in.*
import kotlinx.android.synthetic.main.frag_sign_up.*
import kotlinx.android.synthetic.main.frag_sign_up.etEmail
import kotlinx.android.synthetic.main.frag_sign_up.etPwd

/**
 * Created by hongenit on 2019/6/30.
 * desc:
 */
class SignUpFragment : AccountBaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.frag_sign_up, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        btnSignUp.setOnClickListener {
            doSignUp()
        }


        tvSignIn.setOnClickListener {
            (activity as LoginActivity).showSignIn()
        }



        etPwd.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    doSignUp()
                }
                return false
            }
        })

    }


    // 注册登录操作
    private fun doSignUp() {
        val email = etEmail.text.toString()
        val pwd = etPwd.text.toString()
        if (checkAccountAndPwd(email, pwd)) {
            SignUpBaseResponse.getResponse(email, pwd, object : Callback {
                override fun onResponse(baseResponse: BaseResponse) {
                    val signUpResponse = baseResponse as SignUpBaseResponse
                    if (signUpResponse.status == BaseResponse.STATUS_OK && signUpResponse.user_id > 0) {
                        val userId = signUpResponse.user_id.toString()
                        UserModel.saveLoginStatus(userId, signUpResponse.token)
                        onLoginSucceed()
                        logDebug("SignUpBaseResponse success")
                        UmengEvent.signUpSucceed(userId)
                    } else {
                        // 用户名或密码错误。
                        showToast(getString(R.string.error_account_pwd))
                        logDebug("code = ${signUpResponse.status}   msg = ${signUpResponse.msg}")
                    }
                }

                override fun onFailure(e: Exception) {
                    // 网络异常
                    showToast(getString(R.string.network_error))
                    logDebug("sign up network error")
                }
            })
        }
    }


}