package com.hongenit.gifshowapp.network.response

import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.events.ForceToLoginEvent
import com.hongenit.gifshowapp.extension.logWarn
import com.hongenit.gifshowapp.extension.showToast
import com.hongenit.gifshowapp.extension.showToastOnUiThread
import com.hongenit.gifshowapp.network.NetworkConst
import com.hongenit.gifshowapp.util.GlobalUtil
import com.hongenit.gifshowapp.util.UserModel
import org.greenrobot.eventbus.EventBus
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException

/**
 * 对服务器的返回进行相应的逻辑处理。注意此类只处理公众的返回逻辑，涉及具体的业务逻辑，仍然交由接口调用处自行处理。
 *
 */
object ResponseHandler {

    private val TAG = "ResponseHandler"

    /**
     * 当网络请求正常响应的时候，根据状态码处理通用部分的逻辑。
     * @param response
     * 响应实体类
     * @return 如果已经将该响应处理掉了，返回true，否则返回false。
     */
    fun handleResponse(response: BaseResponse?): Boolean {
        if (response == null) {
            logWarn(TAG, "handleResponse: response is null")
            showToast(GlobalUtil.getString(R.string.unknown_error))
            return true
        }
        val status = response.status
        when (status) {
            NetworkConst.STATUS_TOKEN_INVALID, NetworkConst.STATUS_USER_NOT_EXIST -> {
                logWarn(TAG, "handleResponse: status code is $status")
                UserModel.logOut()
                showToastOnUiThread(GlobalUtil.getString(R.string.login_status_expired))
                val event = ForceToLoginEvent()
                EventBus.getDefault().post(event)
                return true
            }
            else -> return false
        }
    }

    /**
     * 当网络请求没有正常响应的时候，根据异常类型进行相应的处理。
     * @param e
     * 异常实体类
     */
    fun handleFailure(e: Exception) {
        when (e) {
            is ConnectException -> {
//                showToastOnUiThread(GlobalUtil.getString(R.string.network_connect_error))
                logWarn(TAG, "handleFailure exception is $e")
            }
            is SocketTimeoutException -> {
//                showToastOnUiThread(GlobalUtil.getString(R.string.network_connect_timeout))
                logWarn(TAG, "handleFailure exception is $e")
            }
//            is ResponseCodeException -> showToastOnUiThread(GlobalUtil.getString(R.string.network_response_code_error) + e.responseCode)
            is NoRouteToHostException -> {
//                showToastOnUiThread(GlobalUtil.getString(R.string.no_route_to_host))
                logWarn(TAG, "handleFailure exception is $e")
            }
            else -> {
                logWarn(TAG, "handleFailure exception is $e")
                showToastOnUiThread(GlobalUtil.getString(R.string.unknown_error))
            }
        }
    }

}