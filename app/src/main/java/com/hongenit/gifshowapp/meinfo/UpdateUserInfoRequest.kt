package com.hongenit.gifshowapp.meinfo

import com.hongenit.gifshowapp.extension.logDebug
import com.hongenit.gifshowapp.extension.logWarn
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.NetworkConst
import com.hongenit.gifshowapp.network.Request
import com.hongenit.gifshowapp.network.exception.UpdateUserInfoExcepiton
import com.hongenit.gifshowapp.network.exception.UploadAvatarException
import com.hongenit.gifshowapp.network.response.BaseResponse
import com.hongenit.gifshowapp.network.response.ResponseHandler
import com.hongenit.gifshowapp.util.GlobalUtil
import com.hongenit.gifshowapp.util.QiniuManager
import com.hongenit.gifshowapp.util.UserModel
import com.qiniu.android.http.ResponseInfo

/**
 * Created by hongenit on 2019/8/9.
 * desc: 更新用户的昵称，简介和头像等信息。
 */
class UpdateUserInfoRequest(
    var nickname: String,
    var description: String,
    var avatarFilePath: String
) : Request() {

    // 上传到oss后返回的文件地址。
    private var avatarUri: String = ""
    private var mCallback: Callback? = null
    private val TAG = "UpdateUserInfoRequest"


    override fun url(): String {
        return URL
    }

    override fun method(): Int {
        return GET
    }

    override fun params(): Map<String, String>? {
        val params = HashMap<String, String>()
        params[NetworkConst.USER_ID] = UserModel.userId
        params[NetworkConst.TOKEN] = UserModel.token
        if (nickname.isNotBlank()) {
            params[NetworkConst.NICKNAME] = nickname
        }
        if (description.isNotBlank()) {
            params[NetworkConst.DESCRIPTION] = description
        }
        if (avatarUri.isNotBlank()) {
            params[NetworkConst.AVATAR] = avatarUri

        }
        return params
    }

    override fun listen(callback: Callback?) {
        mCallback = callback

        if (avatarFilePath.isEmpty()) {
            requestUpdateUserInfo()
        } else {
            // 如果头像改变，则需要先上传头像到oss
            getUpToken()
        }

    }


    // 请求修改用户信息。
    private fun requestUpdateUserInfo() {
        inFlight(UpdateUserInfoResponse::class.java)
        setListener(mCallback)
    }


    /**
     * 获取用于上传到七牛云的uptoken。
     */
    private fun getUpToken() {
        FetchUpTokenResponse.getResponse(avatarFilePath, object : Callback {
            override fun onResponse(baseResponse: BaseResponse) {
                if (!ResponseHandler.handleResponse(baseResponse)) {
                    val fetchUpTokenResponse = baseResponse as FetchUpTokenResponse
                    val status = fetchUpTokenResponse.status
                    if (status == 0) {
                        val token = fetchUpTokenResponse.uptoken
                        uploadAvatarToQiniu(token)
                    } else {
                        logWarn("getUptoken", "")
                        mCallback?.onFailure(UpdateUserInfoExcepiton("get up token error"))
                    }
                } else {
                    mCallback?.onFailure(UpdateUserInfoExcepiton("get up token error"))
                }
            }

            override fun onFailure(e: Exception) {
                mCallback?.onFailure(e)
            }
        })
    }

    /**
     * 将头像上传到七牛云。
     * @param token
     * 从服务器获取到的uptoken
     */
    private fun uploadAvatarToQiniu(token: String) {
        val key = GlobalUtil.generateKey(avatarFilePath, "avatar")
        logDebug("avatarFilePath = $avatarFilePath   key = $key")
        QiniuManager.upload(avatarFilePath, key, token, object : QiniuManager.UploadListener {
            override fun onSuccess(key: String) {
                avatarUri = NetworkConst.DOMAIN_OF_AVATAR + key
                requestUpdateUserInfo()
            }

            override fun onFailure(info: ResponseInfo?) {
                if (info != null) {
                    mCallback?.onFailure(UploadAvatarException(info.error))
                } else {
                    mCallback?.onFailure(UploadAvatarException("unknown error"))
                }
            }

            override fun onProgress(percent: Double) {}
        })
    }


    private val URL = NetworkConst.BASE_URL + "user/update_info"

}
