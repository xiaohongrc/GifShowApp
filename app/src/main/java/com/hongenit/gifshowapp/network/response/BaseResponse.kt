package com.hongenit.gifshowapp.network.response

/**
 * 请求响应的基类，这里封装了所有请求都必须会响应的参数，status和msg。
 *
 * @author hong
 * @since 19/2/12
 */
open class BaseResponse {

    companion object {
        val STATUS_OK = 0
        val STATUS_REGIST_FAIL = 10001
        val STATUS_ACCOUNT_EXIST = 10002
        val STATUS_NICKNAME_EXIST = 10003
        val STATUS_USER_NOT_EXIST = 10004
        val STATUS_UPDATE_FAIL = 10005
        val STATUS_LOGIN_FAIL = 10006
        val STATUS_FETCH_USER_INFO_FAIL = 10007
    }

    /**
     * 请求结果的状态码，这里可以查看所有状态码的含义：https://github.com/sharefunworks/giffun-server#2-状态码
     */
    var status: Int = 0

    /**
     * 请求结果的简单描述。
     */
    var msg: String = ""
}