package com.hongenit.gifshowapp.util

import android.graphics.drawable.TransitionDrawable
import android.widget.ImageView
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.SafeKeyGenerator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.hongenit.gifshowapp.GlobalParam
import com.hongenit.gifshowapp.extension.logDebug
import java.io.File

/**
 * Glide的工具类。
 *
 */
object GlideUtil {


    /**
     * 由于glide4.x不开放获取缓存目录的接口，看源码手动构造缓存目录
     * @return Glide4.x的图片缓存目录。
     */
    val cacheDir: File?
        get() {
            val cacheDirectory = GlobalParam.context.cacheDir ?: return null
            return File(cacheDirectory, InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)
        }


    /**
     * 获取指定URL地址的图片的缓存File，注意此方法只对使用DiskCacheStrategy.SOURCE缓存策略的图片有效。
     * @param url
     *          图片的url地址。
     * @return 图片的缓存File或者null。
     */
    fun getCacheFile(url: String): File? {
        val cacheDir = cacheDir
        if (cacheDir == null || !cacheDir.exists() || !cacheDir.isDirectory) {
            return null
        }
        val listFiles = cacheDir.listFiles()
        for (i in 0 until listFiles.size){
            logDebug("listFiles[$i] = ${listFiles[i].absolutePath}")
        }

        val file = File(cacheDir, getCachedKey(url)!! + ".0")
        logDebug("url  = $url")
        logDebug("getCachedKey(url)  = ${getCachedKey(url)}")
        logDebug("file path  = ${file.absoluteFile}")
        return file
    }


    /**
     * 获取ImageView当中的GifDrawable对象，如果获取失败则返回null。
     * @param imageView
     * 包含GIF图片的ImageView
     * @return ImageView当中的GifDrawable对象。
     */
    fun getGifDrawable(imageView: ImageView): GifDrawable? {
        val drawable = imageView.drawable ?: return null
        // 获取到图片并检查是否是GIF图
        var gifDrawable: GifDrawable? = null
        if (drawable is GifDrawable) {
            gifDrawable = drawable
        } else if (drawable is TransitionDrawable) {
            for (i in 0 until drawable.numberOfLayers) {
                if (drawable.getDrawable(i) is GifDrawable) {
                    gifDrawable = drawable.getDrawable(i) as GifDrawable
                    break
                }
            }
        }

        InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_SIZE
        val diskCacheFactory = InternalCacheDiskCacheFactory(GlobalParam.context)
        return gifDrawable
    }


    /**
     * 获取指定URL地址图片的缓存Key。
     * @param url
     * 图片的url地址。
     * @return 指定URL地址图片的缓存Key。
     */
    fun getCachedKey(url: String): String? {
        val originalKey = GlideUrl(url)
        val safeKeyGenerator = SafeKeyGenerator()
        return safeKeyGenerator.getSafeKey(originalKey)
    }

    /**
     * 获取指定GlideUrl对象图片的缓存Key。
     * @param glideUrl
     * 包含图片url地址的GlideUrl对象。
     * @return 指定GlideUrl对象图片的缓存Key。
     */
    fun getCachedKey(glideUrl: GlideUrl): String? {
        return getCachedKey(glideUrl.cacheKey)
    }


}
