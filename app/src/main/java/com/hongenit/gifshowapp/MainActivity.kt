package com.hongenit.gifshowapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hongenit.gifshowapp.account.LoginActivity
import android.content.Intent


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }
}
