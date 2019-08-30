package com.hongenit.gifshowapp.gifs

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.bean.WaterFallFeed
import com.hongenit.gifshowapp.util.GlobalUtil

/**
 * 最新Feed模块的RecyclerView适配器，用于在界面上展示最新Feed数据。
 *
 */
class NewFeedAdapter(
    private val fragment: NewFragment, private val feedList: List<WaterFallFeed>, imageWidth: Int,
    layoutManager: RecyclerView.LayoutManager
) : WaterFallGifAdapter<WaterFallFeed>(fragment.activity, feedList, imageWidth, layoutManager) {

    override var isLoadFailed: Boolean = false
        get() = fragment.isLoadFailed

    override var isNoMoreData: Boolean = false
        get() = fragment.isNoMoreData

    override fun onLoad() {
        fragment.onLoad()
    }

    override fun createFeedHolder(parent: ViewGroup): WaterFallGifAdapter.FeedViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.hot_feed_item, parent, false)
        val holder = HotFeedViewHolder(view)
        baseCreateFeedHolder(holder)
        return holder
    }

    override fun bindFeedHolder(holder: WaterFallGifAdapter.FeedViewHolder, position: Int) {
        val viewHolder = holder as HotFeedViewHolder
        val feed = feedList[position]
        viewHolder.commentsCount.text = GlobalUtil.getConvertedNumber(feed.commentsCount)
        baseBindFeedHolder(viewHolder, position)
    }

    private class HotFeedViewHolder internal constructor(view: View) : WaterFallGifAdapter.FeedViewHolder(view) {

        val commentsCount: TextView = view.findViewById(R.id.commentsCount)


    }

    companion object {

        private const val TAG = "NewFeedAdapter"
    }

}
