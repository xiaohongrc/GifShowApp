package com.hongenit.gifshowapp.util.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.widget.ImageView

/**
 * Created by Xiaohong on 2019-06-03.
 * desc:
 */
interface IImageLoader {


    interface LoadImageListener {
        fun onLoadComplete(bitmap: Bitmap?)
        fun onLoadFailed()
    }

    // 加载图片
    fun displayImageWithPlaceHolder(
        imageView: ImageView,
        imageUrl: Any?, placeHolder: Drawable?,
        context: Context
    )

    // 加载圆形图片
    fun displayRoundImage(
        imageView: ImageView,
        imageUrl: Any?, placeHolder: Drawable?,
        context: Context
    )


    // 后台加载图片并监听结果
    fun loadImage(imageUrl: String?, listener: LoadImageListener?, context: Context)

    // 加载图片并监听结果
    fun displayImageWithListener(
        imageView: ImageView,
        imageUrl: Any?, placeHolder: Drawable?,
        listener: LoadImageListener?,
        context: Context
    )
}