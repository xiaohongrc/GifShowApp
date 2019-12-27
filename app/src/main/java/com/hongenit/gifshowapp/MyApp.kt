package com.hongenit.gifshowapp

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.google.android.gms.ads.MobileAds
import com.hongenit.gifshowapp.util.UserModel
import com.umeng.commonsdk.UMConfigure

/**
 * Created by hongenit on 2019/6/30.
 * desc:
 */
class MyApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        GlobalParam.initialize(this)
        UserModel.initUserModel()
        initAdMob()
        initUmeng()
        UmengEvent.appStart()
    }

    private fun initUmeng() {
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null)
    }

    private fun initAdMob() {
        MobileAds.initialize(this, getString(R.string.admob_app_id))
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}