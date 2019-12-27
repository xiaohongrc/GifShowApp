//package com.hongenit.gifshowapp.detail
//
//import android.graphics.Bitmap
//import android.graphics.drawable.Drawable
//import android.os.RecoverySystem
//import android.widget.ImageView
//
//import com.bumptech.glide.load.resource.gif.GifDrawable
//
///**
// * 用于对GIF图播放进行控制的Target，默认情况下GIF图永远循环播放，可以通过参数来指定只播放一次，并且提供了暂停和继续播放GIF图的接口。
// *
// */
//class GifPlayTarget @JvmOverloads constructor(view: ImageView,
//                                              /**
//                                               * 获取选中的GIF图的路径。
//                                               * @return 选中的GIF图的路径。
//                                               */
//                                              val gifPath: String? = null,
//                                              /**
//                                               * 获取选中的GIF图的第一帧的Bitmap。
//                                               * @return 选中的GIF图的第一帧的Bitmap。
//                                               */
//                                              val firstFrame: Bitmap? = null,
//
//                                              playForever: Boolean = true) : GlideDDrawableImageViewTarget(view, if (playForever) GifDrawable.LOOP_FOREVER else 1) {
//
//    private var gifDrawable: GifDrawable? = null
//
//    private var url: String? = null
//
//    private var delayRatio = 1.0
//
//    /**
//     * 判断当前GIF图是否正在播放。
//     * @return 正在播放返回true，否则返回false。
//     */
//    val isRunning: Boolean
//        get() = gifDrawable != null && gifDrawable!!.isRunning
//
//    constructor(view: ImageView, playForever: Boolean) : this(view, null, null, playForever) {}
//
//    override fun onResourceReady(resource: GlideDrawable, animation: GlideAnimation<in GlideDrawable>) {
//        super.onResourceReady(resource, animation)
//        url?.let { ProgressInterceptor.removeListener(it) }
//        if (resource is GifDrawable) {
//            gifDrawable = resource
//            gifDrawable!!.decoder.setDelayRatio(delayRatio)
//        }
//    }
//
//    override fun onLoadFailed(e: Exception, errorDrawable: Drawable) {
//        super.onLoadFailed(e, errorDrawable)
//        url?.let { ProgressInterceptor.removeListener(it) }
//    }
//
//    override fun onStart() {}
//
//    override fun onStop() {}
//
//    /**
//     * 设置网络请求下载进度监听器。
//     * @param url
//     * Glide请求的url地址。
//     * @param listener
//     * 下载进度的监听器。
//     */
//    fun setProgressListener(url: String, listener: RecoverySystem.ProgressListener) {
//        this.url = url
//        ProgressInterceptor.addListener(url, listener)
//    }
//
//    /**
//     * 设置GIF的播放速度。
//     * @param playSpeed
//     * 1代表1/3速度播放，2代表1/2速度播放，3代表正常速度播放，4代表1.5倍速度播放，5代表2倍速度播放。
//     */
//    fun setGifPlaySpeed(playSpeed: String) {
//        when (playSpeed) {
//            "1" -> delayRatio = 3.0
//            "2" -> delayRatio = 2.0
//            "3" -> delayRatio = 1.0
//            "4" -> delayRatio = 1 / 1.5
//            "5" -> delayRatio = 1 / 2.0
//        }
//    }
//
//    /**
//     * 恢复GIF播放。
//     */
//    fun resumePlaying() {
//        super.onStart()
//    }
//
//    /**
//     * 暂停GIF播放。
//     */
//    fun pausePlaying() {
//        super.onStop()
//    }
//
//}
