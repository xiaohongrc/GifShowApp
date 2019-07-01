package com.hongenit.gifshowapp.network

/**
 * 网络请求响应的回调接口。
 *
 * @author hong
 * @since 17/2/12
 */
interface Callback {

    fun onResponse(baseResponse: BaseResponse)

    fun onFailure(e: Exception)

}
