package com.hongenit.gifshowapp.account

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.BaseResponse
import com.hongenit.gifshowapp.network.SignUpBaseResponse
import kotlinx.android.synthetic.main.frag_sign_up.*

/**
 * Created by hongenit on 2019/6/30.
 * desc:
 */
class SignUpFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.frag_sign_up, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSignUp.setOnClickListener {

            val email = etEmail.text.toString()
            val pwd = etPwd.text.toString()
            if (email.isNotEmpty() && pwd.isNotEmpty()) {
                SignUpBaseResponse.getResponse(email, pwd, object : Callback {
                    override fun onResponse(baseResponse: BaseResponse) {
                        println("successs")

                    }

                    override fun onFailure(e: Exception) {
                        println("onFailure" + e.toString())
                    }
                })
            }

        }

    }


}