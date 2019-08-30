package com.hongenit.gifshowapp.network


import com.google.gson.GsonBuilder
import com.hongenit.gifshowapp.GlobalParam
import com.hongenit.gifshowapp.network.response.BaseResponse
import com.hongenit.gifshowapp.util.Utility
import com.hongenit.gifshowapp.extension.logVerbose
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.OkHttpClient
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 网络请求模式的基类，所有的请求封装都应该要继承此类。这里会提供网络模块的配置，以及请求的具体逻辑处理等。
 *
 * @author hong
 * @since 17/2/12
 */
abstract class Request {

    private val TAG: String = "Request"
    private lateinit var okHttpClient: OkHttpClient

    private val okHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
//            .addNetworkInterceptor(LoggingInterceptor())

    private var callback: Callback? = null

    private var params: Map<String, String>? = null

    var getParamsAlready = false

//    var deviceName: String
//
//    var deviceSerial: String

    init {
        connectTimeout(10)
        writeTimeout(10)
        readTimeout(10)
//        deviceName = Utility.deviceName
//        deviceSerial = Utility.getDeviceSerial()
    }

    private fun build() {
        okHttpClient = okHttpBuilder.build()
    }

    fun connectTimeout(seconds: Int) {
        okHttpBuilder.connectTimeout(seconds.toLong(), TimeUnit.SECONDS)
    }

    fun writeTimeout(seconds: Int) {
        okHttpBuilder.writeTimeout(seconds.toLong(), TimeUnit.SECONDS)
    }

    fun readTimeout(seconds: Int) {
        okHttpBuilder.readTimeout(seconds.toLong(), TimeUnit.SECONDS)
    }

    /**
     * 设置响应回调接口
     * @param callback
     * 回调的实例
     */
    fun setListener(callback: Callback?) {
        this.callback = callback
    }

    /**
     * 组装网络请求后添加到HTTP发送队列，并监听响应回调。
     * @param requestModel
     * 网络请求对应的实体类
     */
    fun <T : BaseResponse> inFlight(requestModel: Class<T>) {
        build()
        val requestBuilder = okhttp3.Request.Builder()
        if (method() == GET && getParams() != null) {
            requestBuilder.url(urlWithParam())
            logVerbose(TAG, "urlWithParam = ${urlWithParam()}")
        } else {
            requestBuilder.url(url())
            logVerbose(TAG, "url = ${url()}")
        }
        requestBuilder.headers(headers(Headers.Builder()).build())
        when {
            method() == POST -> requestBuilder.post(formBody())
            method() == PUT -> requestBuilder.put(formBody())
            method() == DELETE -> requestBuilder.delete(formBody())
        }
        okHttpClient.newCall(requestBuilder.build()).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                notifyFailure(e)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                try {
                    if (response.isSuccessful) {
                        val body = response.body()
                        val result = if (body != null) {
                            body.string()
                        } else {
                            ""
                        }
                        logVerbose(TAG, result)
                        val gson = GsonBuilder().disableHtmlEscaping().create()
                        val responseModel = gson.fromJson(result, requestModel)
                        response.close()
                        notifyResponse(responseModel)
                    } else {
//                        notifyFailure(ResponseCodeException(response.code()))
                    }
                } catch (e: Exception) {
                    notifyFailure(e)
                }
            }


        })
    }

    abstract fun url(): String

    abstract fun method(): Int

    abstract fun listen(callback: Callback?)

    /**
     * 构建和服务器身份认证相关的请求参数。
     * @param params
     * 构建参数的param对象
     * @return 如果完成了身份认证参数构建返回true，否则返回false。
     */
    fun buildAuthParams(params: MutableMap<String, String>?): Boolean {
//        if (params != null && AuthUtil.isLogin) {
//            val userId = AuthUtil.userId.toString()
//            val token = AuthUtil.token
//            params[NetworkConst.UID] = userId
//            params[NetworkConst.DEVICE_SERIAL] = deviceSerial
//            params[NetworkConst.TOKEN] = token
//            return true
//        }
        return false
    }

    /**
     * 根据传入的keys构建用于进行服务器验证的参数，并添加到请求头当中。
     * @param builder
     * 请求头builder
     * @param keys
     * 用于进行服务器验证的键。
     */
    fun buildAuthHeaders(builder: Headers.Builder?, vararg keys: String) {
        if (builder != null && keys.isNotEmpty()) {
            val params = mutableListOf<String>()
            for (i in keys.indices) {
                val key = keys[i]
                getParams()?.let {
                    val p = it[key]
                    if (p != null) {
                        params.add(p)
                    }
                }
            }
            builder.add(NetworkConst.VERIFY, AuthUtil.getServerVerifyCode(*params.toTypedArray()))
        }
    }

    /**
     * Android客户端的所有请求都需要添加User-Agent: GifFun Android这样一个请求头。每个接口的封装子类可以添加自己的请求头。
     * @param builder
     * 请求头builder
     * @return 添加完请求头后的builder。
     */
    open fun headers(builder: Headers.Builder): Headers.Builder {
        builder.add(NetworkConst.HEADER_USER_AGENT, NetworkConst.HEADER_USER_AGENT_VALUE)
        builder.add(NetworkConst.HEADER_APP_VERSION, Utility.appVersion)
        builder.add(NetworkConst.HEADER_APP_SIGN, Utility.appSign)
        return builder
    }

    open fun params(): Map<String, String>? {
        return null
    }

    /**
     * 构建POST、PUT、DELETE请求的参数体。
     *
     * @return 组装参数后的FormBody。
     */
    private fun formBody(): FormBody {
        val builder = FormBody.Builder()
        val params = getParams()
        if (params != null) {
            val keys = params.keys
            if (keys.isNotEmpty()) {
                for (key in keys) {
                    val value = params[key]
                    if (value != null) {
                        builder.add(key, value)
                    }
                }
            }
        }
        return builder.build()
    }

    /**
     * 当GET请求携带参数的时候，将参数以key=value的形式拼装到GET请求URL的后面，并且中间以?符号隔开。
     * @return 携带参数的URL请求地址。
     */
    private fun urlWithParam(): String {
        val params = getParams()
        if (params != null) {
            val keys = params.keys
            if (keys.isNotEmpty()) {
                val paramsBuilder = StringBuilder()
                var needAnd = false
                for (key in keys) {
                    if (needAnd) {
                        paramsBuilder.append("&")
                    }
                    paramsBuilder.append(key).append("=").append(params[key])
                    needAnd = true
                }
                return url() + "?" + paramsBuilder.toString()
            }
        }
        return url()
    }

    /**
     * 获取本次请求所携带的所有参数。
     *
     * @return 本次请求所携带的所有参数，以Map形式返回。
     */
    private fun getParams(): Map<String, String>? {
        if (!getParamsAlready) {
            params = params()
            getParamsAlready = true
        }
        return params
    }

    /**
     * 当请求响应成功的时候，将服务器响应转换后的实体类进行回调。
     * @param baseResponse
     * 服务器响应转换后的实体类
     */
    private fun notifyResponse(baseResponse: BaseResponse) {
        callback?.let {
            if (it is OriginThreadCallback) {
                it.onResponse(baseResponse)
                callback = null
            } else {
                GlobalParam.handler.post {
                    it.onResponse(baseResponse)
                    callback = null
                }
            }
        }
    }

    /**
     * 当请求响应失败的时候，将具体的异常进行回调。
     * @param e
     * 请求响应的异常
     */
    private fun notifyFailure(e: Exception) {
        callback?.let {
            if (it is OriginThreadCallback) {
                it.onFailure(e)
                callback = null
            } else {
                GlobalParam.handler.post {
                    it.onFailure(e)
                    callback = null
                }
            }
        }
    }

    companion object {

        const val GET = 0

        const val POST = 1

        const val PUT = 2

        const val DELETE = 3
    }

}