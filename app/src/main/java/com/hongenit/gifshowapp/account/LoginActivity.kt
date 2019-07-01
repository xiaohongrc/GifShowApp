package com.hongenit.gifshowapp.account

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.hongenit.gifshowapp.R
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by hongenit on 2019/6/29.
 * desc:
 */
class LoginActivity : AppCompatActivity() {



    private var mSignInFragment: SignInFragment? = null
    private var mSignUpFragment: SignUpFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ibSignUp.setOnClickListener {
            showSignUp()
        }

        ibSignIn.setOnClickListener {
            showSignIn()
        }


    }

    private fun showSignIn() {
        val transaction = supportFragmentManager.beginTransaction()
        if (mSignInFragment == null) {
            mSignInFragment = SignInFragment()
        }
        mSignInFragment?.let {
            if (it.isAdded){
                transaction.replace(R.id.fragmentContainer, it)
            }else{
                transaction.add(R.id.fragmentContainer, it)
            }
        }
        transaction.commit()
    }

    private fun showSignUp() {
        val transaction = supportFragmentManager.beginTransaction()
        if (mSignUpFragment == null) {
            mSignUpFragment = SignUpFragment()
        }
        mSignUpFragment?.let {
            if (it.isAdded){
                transaction.replace(R.id.fragmentContainer, it)
            }else{

                transaction.add(R.id.fragmentContainer, it)
            }
        }
        transaction.commit()
    }


}
