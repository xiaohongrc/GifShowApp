package com.hongenit.gifshowapp.collect

import android.app.Activity
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hongenit.gifshowapp.GlobalParam
import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.bean.WaterFallFeed
import com.hongenit.gifshowapp.detail.FeedDetailActivity
import com.hongenit.gifshowapp.events.LikeFeedEvent
import com.hongenit.gifshowapp.extension.dp2px
import com.hongenit.gifshowapp.extension.showToast
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.NetworkConst
import com.hongenit.gifshowapp.network.response.BaseResponse
import com.hongenit.gifshowapp.util.DeviceInfo
import com.hongenit.gifshowapp.util.GlobalUtil
import com.hongenit.gifshowapp.util.UserModel
import com.hongenit.gifshowapp.util.imageloader.IImageLoader
import com.hongenit.gifshowapp.util.imageloader.MyImageLoader
import org.greenrobot.eventbus.EventBus
import kotlin.math.min

/**
 * Created by Xiaohong on 2019-07-22.
 * desc:
 */
open class CollectAdapter(
    val activity: Activity,
    private val feedList: List<WaterFallFeed>,
    private val imageWidth: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    val TAG_KEY_BTN_DELETE = R.id.btn_delete
    val TAG_KEY_FEEDCOVER = R.id.feedCover

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.feedCover -> {
                FeedDetailActivity.actionStart(
                    activity,
                    v,
                    v.getTag(TAG_KEY_FEEDCOVER) as WaterFallFeed
                )
            }

            R.id.btn_delete -> {
                val gif = v.getTag(TAG_KEY_BTN_DELETE) as WaterFallFeed
                deleteCollectGif(gif)
            }

        }

    }

    // 删除收藏的gif，即取消收藏
    private fun deleteCollectGif(gif: WaterFallFeed) {
        CollectGifResponse.getResponse(
            gif.feedId,
            UserModel.userId,
            false,
            object : Callback {
                override fun onResponse(baseResponse: BaseResponse) {
                    val response = baseResponse as CollectGifResponse
                    if (response.status == NetworkConst.STATUS_OK) {
                        if (response.collect) {
                            showToast(GlobalUtil.getString(R.string.collect_succeed))
                        } else {
                            showToast(GlobalUtil.getString(R.string.uncollect_succeed))
                            onDeleteSucceed(gif)
                        }
                    }
                }

                override fun onFailure(e: Exception) {
                    showToast(GlobalUtil.getString(R.string.collect_failed))
                }
            })
    }

    private fun onDeleteSucceed(gif: WaterFallFeed) {
        val event = LikeFeedEvent()
        event.from = LikeFeedEvent.FROM_FEED_DETAIL
        event.feed = gif
        event.type = LikeFeedEvent.UNLIKE_FEED
        EventBus.getDefault().post(event)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        println("---------------------------onCreateViewHolder p1 = $p1 ")
        val itemView = LayoutInflater.from(activity).inflate(R.layout.item_collect, p0, false)
        return CollectViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return feedList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        val collectHolder = holder as CollectViewHolder
        bindFeedHolder(collectHolder, pos)
    }

    private fun bindFeedHolder(holder: CollectViewHolder, position: Int) {
        println("---------------------------bindFeedHolder position = $position ")
        val feed = feedList[position]
        holder.feedContent.text = feed.content
        holder.nickname.text = feed.nickname
        holder.btnDelete.setOnClickListener(this)
        holder.btnDelete.setTag(TAG_KEY_BTN_DELETE, feed)
        var imageHeight = calculateImageHeight(feed)
        // 最大高度不超过屏幕剩余高度
        val maxHeight = DeviceInfo.screenHeight - dp2px(100f) - DeviceInfo.statusBarHeight
        imageHeight = min(imageHeight, maxHeight)
        holder.feedCover.layoutParams.width = imageWidth
        holder.feedCover.layoutParams.height = imageHeight
        holder.feedCover.setOnClickListener(this)

        holder.feedCover.setTag(TAG_KEY_FEEDCOVER, feed)



        MyImageLoader.displayImage(
            holder.feedCover,
            feed.cover,
            null,
            object : IImageLoader.LoadImageListener {
                override fun onLoadComplete(bitmap: Bitmap?) {
                    loadCoverOver()
                }

                override fun onLoadFailed() {
                    loadCoverOver()
                }

                private fun loadCoverOver() {
                    GlobalParam.handler.post {
                        loadFeedGif(feed, holder.feedCover, imageHeight)
                    }
                }
            },
            activity
        )

        if (feed.avatar.isBlank()) {
            Glide.with(activity)
                .load(R.drawable.avatar_default)
                .placeholder(R.drawable.loading_bg_circle)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(holder.avatar)
        } else {
            Glide.with(activity)
                .load(feed.avatar)
                .placeholder(R.drawable.loading_bg_circle)
                .error(R.drawable.avatar_default)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(holder.avatar)
        }


//        val visibleItemCount = layoutManager.childCount
//        if (visibleItemCount >= dataItemCount - 1) {
//            onLoad()
//        }

    }

    // 加载预览图
    private fun loadFeedGif(feed: WaterFallFeed, targetView: ImageView, imageHeight: Int) {
        val virtualGifUrl = feed.gif
        println("--------------------------loadFeedGif virtualGifUrl=$virtualGifUrl")
        Glide.with(activity)
            .load(virtualGifUrl)
            .override(imageWidth, imageHeight)
            .placeholder(targetView.drawable)
            .priority(Priority.IMMEDIATE)
            .into(targetView)
    }

    private fun calculateImageHeight(feed: WaterFallFeed): Int {
        val originalWidth = feed.imgWidth
        val originalHeight = feed.imgHeight
        return imageWidth * originalHeight / originalWidth
    }


    class CollectViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val feedCover: ImageView = view.findViewById(R.id.feedCover)

        val feedContent: TextView = view.findViewById(R.id.feedContent)

        val avatar: ImageView = view.findViewById(R.id.avatar)

        val nickname: TextView = view.findViewById(R.id.nickname)

        val btnDelete: ImageButton = view.findViewById(R.id.btn_delete)

    }

}


