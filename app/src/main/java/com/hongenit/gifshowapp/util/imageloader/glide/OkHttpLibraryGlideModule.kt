package com.hongenit.gifshowapp.util.imageloader.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import java.io.InputStream
import com.bumptech.glide.GlideBuilder




/**
 * Created by Xiaohong on 2019-08-21.
 * desc:
 */
class OkHttpLibraryGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {}

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpGlideUrlLoader.Factory())
    }

}