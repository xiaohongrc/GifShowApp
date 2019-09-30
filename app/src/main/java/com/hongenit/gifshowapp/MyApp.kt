package com.hongenit.gifshowapp

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.hongenit.gifshowapp.util.UserModel

/**
 * Created by hongenit on 2019/6/30.
 * desc:
 */
class MyApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        GlobalParam.initialize(this)
        UserModel.initUserModel()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}