package com.hongenit.gifshowapp.util.imageloader.glide

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GlideUrl
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.IOException
import java.io.InputStream

/**
 * 自定义的OkHttp DataFetcher，用于使用OkHttp的方向来进行Glide网络请求。
 */
internal class OkHttpFetcher(private val client: OkHttpClient, private val url: GlideUrl) : DataFetcher<InputStream> {
    private lateinit var progressCall: Call
    private var stream: InputStream? = null
    private var responseBody: ResponseBody? = null
    @Volatile
    private var isCancelled: Boolean = false


    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.REMOTE
    }

    @Throws(Exception::class)
    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        val request = Request.Builder().url(url.toString()).build()
        val client = OkHttpClient()
        client.interceptors().add(ProgressInterceptor())

        try {
            progressCall = client.newCall(request)
            val response = progressCall.execute()
            if (isCancelled) {
                return
            }

            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            stream = response.body()!!.byteStream()
        } catch (e: IOException) {
            e.printStackTrace()
            return
        }

        return
    }

    override fun cleanup() {
        try {
            if (stream != null) {
                stream!!.close()
            }
            if (responseBody != null) {
                responseBody!!.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    override fun cancel() {
        isCancelled = true
    }
}