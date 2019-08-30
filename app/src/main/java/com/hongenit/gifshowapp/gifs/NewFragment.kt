package com.hongenit.gifshowapp.gifs

import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.bean.WaterFallFeed
import com.hongenit.gifshowapp.callback.LoadDataListener
import com.hongenit.gifshowapp.callback.PendingRunnable
import com.hongenit.gifshowapp.events.*
import com.hongenit.gifshowapp.extension.logWarn
import com.hongenit.gifshowapp.extension.searchModelIndex
import com.hongenit.gifshowapp.extension.showToast
import com.hongenit.gifshowapp.network.OriginThreadCallback
import com.hongenit.gifshowapp.network.response.BaseResponse
import com.hongenit.gifshowapp.network.response.FetchNewResponse
import com.hongenit.gifshowapp.network.response.ResponseHandler
import com.hongenit.gifshowapp.util.GlobalUtil
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * Created by Xiaohong on 2019-07-18.
 * desc:
 */
class NewFragment : WaterFallGifsFragment(), LoadDataListener {
    companion object {
        private const val TAG = "NewFragment"
    }

    private var mCurrentPage: Int = 1
    /**
     * RecyclerView的数据源，用于存储所有展示中的Feeds。
     */
    internal var feedList: MutableList<WaterFallFeed> = ArrayList()

    override fun setupRecyclerView() {
        super.setupRecyclerView()
        adapter = NewFeedAdapter(this, feedList, imageWidth, layoutManager)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SpaceItemDecoration(adapter as NewFeedAdapter))
    }

    /**
     * 加载feeds。如果数据库有缓存则优先显示缓存内存，如果没有缓存则从网络获取feeds。
     */
    override fun loadGifs(page: Int) {
        val isRefreshing = page == 1 /* lastFeed等于0表示刷新 */
        val callback = object : OriginThreadCallback {
            override fun onResponse(baseResponse: BaseResponse) {
                handleFetchedFeeds(baseResponse, isRefreshing)
                isLoadingMore = false
                mCurrentPage++
            }

            override fun onFailure(e: Exception) {
                logWarn(TAG, e.message, e)
                if (isRefreshing) {
                    ResponseHandler.handleFailure(e)
                }
                activity.runOnUiThread {
                    loadFailed(null)
                    isLoadingMore = false
                }
            }
        }
        FetchNewResponse.getResponse(page, callback)
    }

    /**
     * 刷新feeds。
     */
    override fun refreshGifs() {
        mCurrentPage = 1
        loadGifs(mCurrentPage)
    }

    override fun loadGifsFromDB() {
        // 由于服务器接口设计原因，热门feed不能缓存到数据库中，因此这里直接刷新feeds列表
        refreshGifs()
    }

    override fun dataSetSize(): Int {
        return feedList.size
    }

    override fun onLoad() {
        if (!isLoadingMore) {
            if (feedList.isNotEmpty()) {
                isLoadingMore = true
                isLoadFailed = false
                isLoadingMore = true
                loadGifs(mCurrentPage) // 此处lastFeed恒定为1，表示加载更多。
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(messageEvent: MessageEvent) {
        if (messageEvent is DeleteFeedEvent) {
            val feedId = messageEvent.feedId
            searchModelIndex(feedList, feedId) { index ->
                feedList.removeAt(index)
                adapter.notifyItemRemoved(index)
            }
        } else if (messageEvent is LikeFeedEvent) {
            if (messageEvent.from == LikeFeedEvent.FROM_HOT) {
                return
            }
            val feedId = messageEvent.feedId
            searchModelIndex(feedList, feedId) { index ->
                // 对于Feed点赞状态同步要延迟执行，等到ViewPager切换到相应的界面时再执行，否则会出现状态同步的问题
                val runnable = object : PendingRunnable {
                    override fun run(index: Int) {
                        val feed = feedList[index]
                        feed.isLikedAlready = messageEvent.type == LikeFeedEvent.LIKE_FEED
                        feed.likesCount = messageEvent.likesCount
                        adapter.notifyItemChanged(index)
                    }
                }
                pendingRunnable.put(index, runnable)
            }
        } else if (messageEvent is ModifyUserInfoEvent) {
            if (messageEvent.modifyNickname || messageEvent.modifyAvatar) {
                swipeRefresh.isRefreshing = true
                refreshGifs()
            }
        } else if (messageEvent is PostCommentEvent) {
            val feedId = messageEvent.feedId
            searchModelIndex(feedList, feedId) { index ->
                val feed = feedList[index]
                val commentsCount = feed.commentsCount
                feed.commentsCount = commentsCount + 1
                adapter.notifyItemChanged(index)
            }
        } else if (messageEvent is DeleteCommentEvent) {
            val feedId = messageEvent.feedId
            searchModelIndex(feedList, feedId) { index ->
                val newFeed = feedList[index]
                val commentsCount = newFeed.commentsCount
                newFeed.commentsCount = commentsCount - 1
                adapter.notifyItemChanged(index)
            }
        } else {
            super.onMessageEvent(messageEvent)
        }
    }

    /**
     *
     * 处理获取世界频道feeds请求的返回结果。
     *
     * @param response
     * 服务器响应的获取feeds请求的实体类。
     * @param isRefreshing
     * true表示刷新请求，false表示加载更多请求。
     */
    private fun handleFetchedFeeds(response: BaseResponse, isRefreshing: Boolean) {
        isNoMoreData = false
        if (!ResponseHandler.handleResponse(response)) {
            val response = response as FetchNewResponse
            val status = response.status
            if (status == 0) {
                val feeds = response.gifs
                if (isRefreshing) {
                    activity.runOnUiThread {
                        feedList.clear()
                        feedList.addAll(feeds)
                        adapter.notifyDataSetChanged()
                        recyclerView.scrollToPosition(0)
                        loadFinished()
                    }
                } else {
                    val oldFeedsCount = feedList.size
                    activity.runOnUiThread {
                        feedList.addAll(feeds)
                        recyclerView.stopScroll()
                        adapter.notifyItemRangeInserted(oldFeedsCount, feeds.size)
                        loadFinished()
                    }
                }
            } else if (status == 10004) {
                isNoMoreData = true
                activity.runOnUiThread {
                    adapter.notifyItemChanged(adapter.itemCount - 1)
                    loadFinished()
                }
            } else {
                logWarn(TAG, "Fetch feeds failed. " + GlobalUtil.getResponseClue(status, response.msg))
                activity.runOnUiThread {
                    showToast(GlobalUtil.getString(R.string.fetch_data_failed))
                    loadFailed(GlobalUtil.getString(R.string.fetch_data_failed) + ": " + response.status)
                }
            }
        } else {
            activity.runOnUiThread { loadFailed(GlobalUtil.getString(R.string.unknown_error) + ": " + response.status) }
        }
    }



}