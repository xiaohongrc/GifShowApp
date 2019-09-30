package com.hongenit.gifshowapp.network

/**
 * 网络通信模块的常量。
 *
 * @author guolin
 * @since 17/2/14
 */
interface NetworkConst {

    companion object {
        var isDebug = true

        var BASE_URL = getBaseUrl()

        private fun getBaseUrl(): String {
            return if (isDebug) BASE_URL_DEBUG else BASE_URL_FORMAL
        }


        // 存储头像的域名
        const val DOMAIN_OF_AVATAR = "http://pw2p0ejhr.bkt.clouddn.com/"

        const val SERVER_ADDR_HOME = "http://192.168.0.103:8080/"

        const val SERVER_ADDR_HOT_SPOT = "http://192.168.43.198:8080/"

        private const val SERVER_ADDR_DEBUG = "http://192.168.3.193:8080/"

        private const val BASE_URL_DEBUG = SERVER_ADDR_DEBUG + "gif_show_war_exploded/"

        const val SERVER_ADDR = "http://47.99.124.250:8080/"

        private const val BASE_URL_FORMAL = "http://47.99.124.250:8080/gif_show-1.0-SNAPSHOT/"


        const val ACCOUNT = "account"

        const val PWD = "pwd"

        const val TOKEN = "token"

        const val USER_ID = "user_id"

        const val NICKNAME = "nickname"

        const val PAGE = "page"

        const val PAGE_SIZE = "page_size"

        const val FEED_ID = "feed_id"

        const val CONTENT = "content"

        const val COLLECT_STATUS = "collect_status"

        const val FILE_KEY = "file_key"

        const val DESCRIPTION = "description"

        const val AVATAR = "avatar"


        /*  ------------------- */
        const val OPEN_SPACE = 1

        const val PRIVATE_SPACE = 2

        const val DEVICE_NAME = "device"

        const val DEVICE_SERIAL = "d"

        const val CLIENT_VERSION = "cv"

        const val CLIENT_CHANNEL = "channel"


        const val NUMBER = "number"

        const val VERIFY = "v"

        const val UUID = "ud"

        const val HEADER_USER_AGENT = "User-Agent"

        const val HEADER_USER_AGENT_VALUE = "GifFun Android"

        const val HEADER_APP_VERSION = "appv"

        const val HEADER_APP_SIGN = "apps"

        const val OPEN_ID = "openid"

        const val ACCESS_TOKEN = "access_token"


        const val CODE = "code"

        const val URI = "uri"

        const val SPACE = "space"

        const val COVER = "cover"

        const val GIF = "gif"

        const val GIF_MD5 = "gif_md5"


        const val IMG_WIDTH = "img_width"

        const val IMG_HEIGHT = "img_height"

        const val LAST_FEED = "last_feed"

        const val URL = "url"

        const val FEED = "feed"

        const val COMMENT = "comment"

        const val REF_FEED = "ref_feed"

        const val LAST_COMMENT = "last_comment"


        const val USER = "user"

        const val FOLLOWING_IDS = "following_ids"

        const val FOLLOWING_ID = "following_id"


        const val BG_IMAGE = "bg"

        const val LOADING_MORE = "loading_more"

        const val KEYWORD = "keyword"

        const val REASON = "reason"


        // 状态码常量
        // 成功
        val STATUS_OK = 0

        // 正常失败
        val STATUS_FAIL = 10000

        // 没有数据
        val STATUS_NO_DATA = 10001

        // 操作数据库失败
        val STATUS_OPERATE_DB_FAIL = 10002

        val STATUS_TOKEN_INVALID = 10003

        // 账号相关
        val STATUS_REGIST_FAIL = 11001
        val STATUS_ACCOUNT_EXIST = 11002
        val STATUS_NICKNAME_EXIST = 11003
        val STATUS_USER_NOT_EXIST = 11004
        val STATUS_UPDATE_FAIL = 11005
        val STATUS_LOGIN_FAIL = 11006
        val STATUS_USER_NICKNAME_IS_NULL = 11008
        val STATUS_USER_NOT_LOGIN = 11009


    }

}
