package com.hongenit.gifshowapp.network

/**
 * 请求响应的基类，这里封装了所有请求都必须会响应的参数，status和msg。
 *
 * @author hong
 * @since 19/2/12
 */
open class BaseResponse {

    /**
     * 请求结果的状态码，这里可以查看所有状态码的含义：https://github.com/sharefunworks/giffun-server#2-状态码
     */
    var status: Int = 0

    /**
     * 请求结果的简单描述。
     */
    var msg: String = ""
}