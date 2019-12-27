package com.hongenit.gifshowapp

import com.umeng.analytics.MobclickAgent/**
 * Created by Xiaohong on 2019-10-15.
 * desc:
 */
object UmengEvent {

    private fun onEvent(action: String) {
        MobclickAgent.onEvent(GlobalParam.context,action)
    }


    private fun onEvent(action: String,label:String) {
        MobclickAgent.onEvent(GlobalParam.context,action,label)
    }



    // 启动成功
    fun appStart(){
        onEvent("appStart")
    }

    // 点击某个gif
    fun clickGif(feedId: String) {
        onEvent("clickGif",feedId)
    }

    //    取消收藏gif
    fun unCollectGif(feedId: String){
        onEvent("unCollectGif",feedId)
    }

    //    收藏gif
    fun collectGif(feedId: String){
        onEvent("collectGif",feedId)
    }

    //    登录成功
    fun signInSucceed(userId: String){
        onEvent("signInSucceed",userId)
    }

    //    注册成功
    fun signUpSucceed(userId: String){
        onEvent("signUpSucceed",userId)
    }

    //    显示登录界面
    fun showSignIn(){
        onEvent("showSignIn")
    }

    //    显示注册页面
    fun showSignUp(){
        onEvent("showSignup")
    }

    // 点击分享按钮
    fun clickShare(feedId: String){
        onEvent("clickShare",feedId)
    }








}


