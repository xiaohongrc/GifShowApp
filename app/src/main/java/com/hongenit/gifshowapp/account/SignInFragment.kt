package com.hongenit.gifshowapp.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hongenit.gifshowapp.GlobalParam
import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.response.BaseResponse
import com.hongenit.gifshowapp.network.response.SignInResponse
import kotlinx.android.synthetic.main.frag_sign_in.*
import kotlinx.android.synthetic.main.frag_sign_up.etEmail
import kotlinx.android.synthetic.main.frag_sign_up.etPwd

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
            val email = etEmail.text.toString()
            val pwd = etPwd.text.toString()
            if (email.isNotEmpty() && pwd.isNotEmpty()) {
                SignInResponse.getResponse(email, pwd, object : Callback {
                    override fun onResponse(baseResponse: BaseResponse) {
                        val signInResponse = baseResponse as SignInResponse
                        GlobalParam.saveLoginStatus(signInResponse.user_id, signInResponse.token)
                        fetchMyInfo()
                        println("SignUpBaseResponse success")
                    }

                    override fun onFailure(e: Exception) {
                        println("onFailure$e")
                    }
                })
            }
        }

        tvSignUp.setOnClickListener {
            (activity as LoginActivity).showSignUp()
        }

    }


}