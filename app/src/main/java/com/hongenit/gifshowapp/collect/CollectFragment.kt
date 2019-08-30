package com.hongenit.gifshowapp.collect

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.bean.WaterFallFeed
import com.hongenit.gifshowapp.callback.LoadDataListener
import com.hongenit.gifshowapp.callback.PendingRunnable
import com.hongenit.gifshowapp.events.*
import com.hongenit.gifshowapp.extension.logWarn
import com.hongenit.gifshowapp.extension.searchModelIndex
import com.hongenit.gifshowapp.extension.showToast
import com.hongenit.gifshowapp.gifs.BaseGifsFragment
import com.hongenit.gifshowapp.network.OriginThreadCallback
import com.hongenit.gifshowapp.network.response.BaseResponse
import com.hongenit.gifshowapp.network.response.ResponseHandler
import com.hongenit.gifshowapp.util.GlobalUtil
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import android.support.v7.widget.PagerSnapHelper
import com.hongenit.gifshowapp.extension.dp2px
import com.hongenit.gifshowapp.network.NetworkConst
import org.greenrobot.eventbus.EventBus


/**
 * Created by Xiaohong on 2019-07-18.
 * desc: 收藏页
 */
class CollectFragment : BaseGifsFragment(), LoadDataListener {


    companion object {

        private const val TAG = "CollectFragment"
    }

    private var mCurrentPage: Int = 1
    /**
     * RecyclerView的数据源，用于存储所有展示中的Feeds。
     */
    internal var feedList: MutableList<WaterFallFeed> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.fragment_collect, container, false)
        initViews(view)
        EventBus.getDefault().register(this)
        return super.onCreateView(view)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    override fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        getActivity()?.let {
            adapter = CollectAdapter(it, feedList, screenWidth)
            recyclerView.adapter = adapter
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(recyclerView)
        }
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
        FetchCollectsResponse.getResponse(callback)
    }

    /**
     * 刷新feeds。
     */
    override fun refreshGifs() {
        startLoading()
        mCurrentPage = 1
        loadGifs(mCurrentPage)
    }

    override fun loadGifsFromDB() {
        // 直接刷新feeds列表
        refreshGifs()
    }

    override fun dataSetSize(): Int {
        return feedList.size
    }

    override fun onLoad() {
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
            val feed = messageEvent.feed
            if (messageEvent.type == LikeFeedEvent.UNLIKE_FEED) {
                // 取消收藏
                searchModelIndex(feedList, feed.feedId) { index ->
                    feedList.removeAt(index)
                    adapter.notifyDataSetChanged()
                }
            } else {
                // 收藏
                if (!feedList.contains(feed)) {
                    feedList.add(0, feed)
                    adapter.notifyDataSetChanged()
                }

            }
        } else if (messageEvent is ModifyUserInfoEvent) {
            if (messageEvent.modifyNickname || messageEvent.modifyAvatar) {
                swipeRefresh.isRefreshing = true
                refreshGifs()
            }
        } else if (messageEvent is PostCommentEvent) {
            val feedId = messageEvent.feedId
            searchModelIndex(feedList, feedId) { index ->
                val hotFeed = feedList[index]
                val commentsCount = hotFeed.commentsCount
                hotFeed.commentsCount = commentsCount + 1
                adapter.notifyItemChanged(index)
            }
        } else if (messageEvent is DeleteCommentEvent) {
            val feedId = messageEvent.feedId
            searchModelIndex(feedList, feedId) { index ->
                val hotFeed = feedList[index]
                val commentsCount = hotFeed.commentsCount
                hotFeed.commentsCount = commentsCount - 1
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
            val collectsResponse = response as FetchCollectsResponse
            val status = collectsResponse.status
            if (status == 0) {
                val feeds = collectsResponse.gifs
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
            } else if (status == NetworkConst.STATUS_NO_DATA) {
                isNoMoreData = true
                activity.runOnUiThread {
                    feedList.clear()
                    showNoContentView(getString(R.string.no_more_content))
                    adapter.notifyDataSetChanged()
                    loadFinished()
                }
            } else {
                logWarn(
                    TAG,
                    "Fetch feeds failed. " + GlobalUtil.getResponseClue(
                        status,
                        collectsResponse.msg
                    )
                )
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