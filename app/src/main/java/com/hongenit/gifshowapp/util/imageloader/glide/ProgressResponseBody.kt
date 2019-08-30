package com.hongenit.gifshowapp.util.imageloader.glide

import com.hongenit.gifshowapp.extension.logDebug
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException


class ProgressResponseBody(url: String, val responseBody: ResponseBody) : ResponseBody() {

    private val TAG = "XGlide"

    private var bufferedSource: BufferedSource? = null


    private var listener: ProgressListener? = null

    init {
        listener = ProgressInterceptor.LISTENER_MAP[url]
    }


    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(ProgressSource(responseBody.source()))
        }
        return bufferedSource
    }

    private inner class ProgressSource internal constructor(source: Source) : ForwardingSource(source) {

        internal var totalBytesRead: Long = 0

        internal var currentProgress: Int = 0

        @Throws(IOException::class)
        override fun read(sink: Buffer, byteCount: Long): Long {
            val bytesRead = super.read(sink, byteCount)
            val fullLength = responseBody.contentLength()
            if (bytesRead == -1L) {
                totalBytesRead = fullLength
            } else {
                totalBytesRead += bytesRead
            }
            val progress = (100f * totalBytesRead / fullLength).toInt()
            logDebug(TAG, "download progress is $progress")
            if (listener != null && progress != currentProgress) {
                listener!!.onProgress(progress)
            }
            if (listener != null && totalBytesRead == fullLength) {
                listener = null
            }

            currentProgress = progress
            return bytesRead
        }
    }

}
