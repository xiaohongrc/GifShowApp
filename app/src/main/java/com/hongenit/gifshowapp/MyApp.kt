package com.hongenit.gifshowapp

import android.app.Application
import com.hongenit.gifshowapp.GlobalParam

/**
 * Created by hongenit on 2019/6/30.
 * desc:
 */
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalParam.initialize(this)
    }

}