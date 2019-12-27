package com.hongenit.gifshowapp.gifs

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.bean.WaterFallFeed


/**
 * 世界模块的RecyclerView适配器，用于在界面上展示世界模块的数据，以及处理世界模块的相关功能。
 *
 */
class WorldFeedAdapter(private val fragment: NewFragment, feedList: List<WaterFallFeed>, imageWidth: Int,
                       layoutManager: RecyclerView.LayoutManager) : WaterFallGifAdapter<WaterFallFeed>(fragment.activity, feedList, imageWidth, layoutManager) {

    override var isLoadFailed: Boolean = false
        get() = fragment.isLoadFailed

    override var isNoMoreData: Boolean = false
        get() = fragment.isNoMoreData

    override fun onLoad() {
//        fragment.onLoad()
    }

    override fun createFeedHolder(parent: ViewGroup): WaterFallGifAdapter.FeedViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.world_feed_item, parent, false)
        val holder = WorldFeedViewHolder(view)
        baseCreateFeedHolder(holder)
        return holder
    }

    override fun bindFeedHolder(holder: WaterFallGifAdapter.FeedViewHolder, position: Int) {
        val viewHolder = holder as WorldFeedViewHolder
        baseBindFeedHolder(viewHolder, position)
    }

    private class WorldFeedViewHolder internal constructor(view: View) : WaterFallGifAdapter.FeedViewHolder(view)

    companion object {

        private const val TAG = "WorldFeedAdapter"
    }

}