package com.hongenit.gifshowapp.network

import com.hongenit.gifshowapp.network.Callback

/**
 * 网络请求响应的回调接口，回调时保留原来线程进行回调，不切换到主线程。
 *
 * @author guolin
 * @since 17/2/16
 */
interface OriginThreadCallback : Callback
