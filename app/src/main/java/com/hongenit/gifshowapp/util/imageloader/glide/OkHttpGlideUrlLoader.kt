package com.hongenit.gifshowapp.util.imageloader.glide

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import okhttp3.OkHttpClient
import java.io.InputStream

/**
 * 自定义的OkHttp ModelLoader，用于使用OkHttp的方向来进行Glide网络请求。
 */

internal class OkHttpGlideUrlLoader(private val okHttpClient: OkHttpClient) : ModelLoader<GlideUrl, InputStream> {

    class Factory : ModelLoaderFactory<GlideUrl, InputStream> {

        private var client: OkHttpClient? = null

        private val okHttpClient: OkHttpClient
            @Synchronized get() {
                if (client == null) {
                    client = OkHttpClient.Builder().build()
                }
                return client!!
            }

        constructor() {}

        constructor(client: OkHttpClient) {
            this.client = client
        }

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<GlideUrl, InputStream> {
            return OkHttpGlideUrlLoader(okHttpClient)
        }

        override fun teardown() {}
    }

    override fun buildLoadData(
        model: GlideUrl,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream>? {
        return ModelLoader.LoadData(model, OkHttpFetcher(okHttpClient, model))
    }

    override fun handles(model: GlideUrl): Boolean {
        return true
    }
}