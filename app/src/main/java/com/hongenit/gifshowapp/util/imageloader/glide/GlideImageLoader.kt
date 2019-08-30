package com.hongenit.gifshowapp.util.imageloader.glide

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.hongenit.gifshowapp.extension.logWarn
import com.hongenit.gifshowapp.util.imageloader.IImageLoader
import java.io.File
import java.io.FileInputStream


/**
 * Created by Xiaohong on 2019-06-03.
 * desc:
 */
class GlideImageLoader : IImageLoader {
    private val TAG: String = "GlideImageLoader"

    override fun displayImageWithPlaceHolder(
        imageView: ImageView,
        imageUrl: Any?, placeHolder: Drawable?,
        context: Context
    ) {
        if (context is Activity && context.isFinishing) {
            return
        }
        val options = RequestOptions().placeholder(placeHolder).error(placeHolder)
        Glide.with(context)
            .load(imageUrl)
            .priority(Priority.IMMEDIATE)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .apply(options)
            .into(imageView)
    }

    override fun displayRoundImage(
        imageView: ImageView,
        imageUrl: Any?, placeHolder: Drawable?,
        context: Context
    ) {
        if (context is Activity && context.isFinishing) {
            return
        }
        val options = RequestOptions().placeholder(placeHolder).error(placeHolder).circleCrop()
        Glide.with(context)
            .load(imageUrl)
            .circleCrop()
            .priority(Priority.IMMEDIATE)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .apply(options)
            .into(imageView)
    }

    override fun displayImageWithListener(
        imageView: ImageView,
        imageUrl: Any?, placeHolder: Drawable?,
        listener: IImageLoader.LoadImageListener?,
        context: Context
    ) {
        if (context is Activity && context.isFinishing) {
            return
        }
        val options = RequestOptions().placeholder(placeHolder).error(placeHolder)
        Glide.with(context)
            .load(imageUrl)
            .priority(Priority.IMMEDIATE)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .apply(options).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    listener?.onLoadFailed()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    try {
                        if (resource is BitmapDrawable) {
                            listener?.onLoadComplete(resource.bitmap)
                        } else {
                            logWarn(TAG, "resource is not BitmapDrawable!!")
                            listener?.onLoadFailed()
                        }
                    } catch (e: Exception) {
                        listener?.onLoadFailed()
                        e.printStackTrace()
                    }
                    return false
                }
            })
            .into(imageView)
    }


    override fun loadImage(imageUrl: String?, listener: IImageLoader.LoadImageListener?, context: Context) {
        if (context is Activity && context.isFinishing) {
            return
        }
        Glide.with(context).downloadOnly().load(imageUrl).listener(object : RequestListener<File> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<File>?,
                isFirstResource: Boolean
            ): Boolean {
                listener?.onLoadFailed()
                return false
            }

            override fun onResourceReady(
                resource: File?,
                model: Any?,
                target: Target<File>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                try {
                    val fis = FileInputStream(resource)
                    val bmp = BitmapFactory.decodeStream(fis)
                    listener?.onLoadComplete(bmp)
                } catch (e: Exception) {
                    listener?.onLoadFailed()
                    e.printStackTrace()
                }
                return false
            }
        }).preload()
    }


}