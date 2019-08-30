package com.hongenit.gifshowapp.util.imageloader

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.hongenit.gifshowapp.GlobalParam
import com.hongenit.gifshowapp.util.imageloader.glide.GlideImageLoader


/**
 * Created by Xiaohong on 2019-05-15.
 * desc:
 */
object MyImageLoader {

    private val imageLoader: IImageLoader = GlideImageLoader()
    private val appContext: Context = GlobalParam.context


    fun displayImage(
        imageView: ImageView,
        imageUrl: Any?, placeHolder: Drawable?,
        context: Context = appContext
    ) {
        imageLoader.displayImageWithPlaceHolder(imageView, imageUrl, placeHolder, context)
    }

    // 展示图片，不设置错误默认图
    fun displayImageNoPlaceHolder(imageView: ImageView, imageUrl: Any?, context: Context = appContext) {
        displayImage(imageView, imageUrl, null, context)
    }

    // 展示图片并裁切成圆形
    fun displayRoundIcon(imageView: ImageView, imageUrl: Any?, placeHolder: Drawable?, context: Context = appContext) {
        imageLoader.displayRoundImage(imageView, imageUrl, placeHolder, context)
    }

    // 后台下载图片并监听结果
    fun loadImage(iconUrl: String?, listener: IImageLoader.LoadImageListener?, context: Context = appContext) {
        imageLoader.loadImage(iconUrl, listener, context)
    }

    // 展示图片并监听结果
    fun displayImage(
        imageView: ImageView,
        imageUrl: Any?, placeHolder: Drawable?,
        listener: IImageLoader.LoadImageListener?,
        context: Context
    ) {
        imageLoader.displayImageWithListener(imageView, imageUrl, placeHolder, listener, context)
    }


}