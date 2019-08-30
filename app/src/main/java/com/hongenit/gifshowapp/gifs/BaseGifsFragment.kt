package com.hongenit.gifshowapp.gifs

import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.hongenit.gifshowapp.BaseFragment
import com.hongenit.gifshowapp.GlobalParam
import com.hongenit.gifshowapp.MainActivity
import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.callback.InfiniteScrollListener
import com.hongenit.gifshowapp.callback.LoadDataListener
import com.hongenit.gifshowapp.callback.PendingRunnable
import com.hongenit.gifshowapp.events.CleanCacheEvent
import com.hongenit.gifshowapp.events.MessageEvent
import com.hongenit.gifshowapp.events.RefreshMainActivityGifsEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 显示gif列表的基类Fragment
 */
abstract class BaseGifsFragment : BaseFragment() {
    /**
     * 判断是否正在加载更多gifs。
     */
    internal var isLoadingMore = false

    lateinit var activity: MainActivity

    lateinit var swipeRefresh: SwipeRefreshLayout

    lateinit var recyclerView: RecyclerView

    internal lateinit var adapter: RecyclerView.Adapter<*>

    internal lateinit var loadDataListener: LoadDataListener

    internal lateinit var layoutManager: RecyclerView.LayoutManager

    var pendingRunnable = SparseArray<PendingRunnable>()

    var isLoadFailed: Boolean = false


    /**
     * 屏幕宽度
     */
    internal val screenWidth: Int
        get() {
            val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay?.getMetrics(metrics)
            return metrics.widthPixels
        }

    /**
     * 判断是否还有更多数据，当服务器端没有更多gifs时，此值为true。
     */
    /**
     * 判断是否还有更多数据。
     * @return 当服务器端没有更多gifs时，此值为true，否则此值为false。
     */
    var isNoMoreData = false
        internal set

    internal fun initViews(rootView: View) {
        recyclerView = rootView.findViewById(R.id.recyclerView)
        swipeRefresh = rootView.findViewById(R.id.swipeRefresh)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadDataListener = this as LoadDataListener
        activity = getActivity() as MainActivity
        // setup configurations and events
        setupRecyclerView()
        swipeRefresh.setColorSchemeResources(R.color.colorAccent)
        recyclerView.addOnScrollListener(object : InfiniteScrollListener(layoutManager) {

            override fun onLoadMore() {
                loadDataListener.onLoad()

            }

            override fun isDataLoading() = isLoadingMore

            override fun isNoMoreData() = isNoMoreData

        })
        swipeRefresh.setOnRefreshListener { refreshGifs() }
        loadGifsFromDB()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        EventBus.getDefault().register(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(messageEvent: MessageEvent) {
        if (messageEvent is RefreshMainActivityGifsEvent) {
            if (isLoadFailed) { // 只要当加载失败的情况下，收到RefreshMainActivitygifsEvent才会执行刷新，否则不进行刷新
                GlobalParam.handler.postDelayed({
                    // 略微进行延迟处理，使界面上可以看到波纹动画效果
                    reloadGifs()
                }, 300)
            }
        } else if (messageEvent is CleanCacheEvent) {
            reloadGifs()
        }
    }

    /**
     * 加载gifs完成，将gifs显示出来，将加载等待控件隐藏。
     */
    override fun loadFinished() {
        super.loadFinished()
        isLoadFailed = false
        recyclerView.visibility = View.VISIBLE
        swipeRefresh.visibility = View.VISIBLE
        if (swipeRefresh.isRefreshing) {
            swipeRefresh.isRefreshing = false
        }
    }

    /**
     * 加载gifs失败，将加载等待控件隐藏。
     */
    override fun loadFailed(msg: String?) {
        super.loadFailed(msg)
        isLoadFailed = true
        swipeRefresh.isRefreshing = false
        if (dataSetSize() == 0) {
            if (msg == null) {
                swipeRefresh.visibility = View.GONE
                showBadNetworkView(View.OnClickListener {
                    val event = RefreshMainActivityGifsEvent()
                    EventBus.getDefault().post(event)
                })
            } else {
                showLoadErrorView(msg)
            }
        } else {
            adapter.notifyItemChanged(adapter.itemCount - 1)
        }
    }

    /**
     * 重新加载gifs，在加载过程中如果界面上没有元素则显示ProgressBar，如果界面上已经有元素则显示SwipeRefresh。
     */
    private fun reloadGifs() {
        if (adapter.itemCount <= 1) {
            startLoading()
        } else {
            swipeRefresh.isRefreshing = true
        }
        refreshGifs()
    }

    /**
     * 执行潜在的Pending任务。
     */
    fun executePendingRunnableList() {
        val size = pendingRunnable.size()
        if (size > 0) {
            for (i in 0 until size) {
                val index = pendingRunnable.keyAt(i)
                val runnable = pendingRunnable.get(index)
                runnable.run(index)
            }
            pendingRunnable.clear()
        }
    }

    /**
     * 将RecyclerView滚动到顶部
     */
    fun scrollToTop() {
        if (adapter.itemCount != 0) {
            recyclerView.smoothScrollToPosition(0)
        }
    }


    // 设置recyclerView的样式
    internal abstract fun setupRecyclerView()

    // 加载gif数据
    internal abstract fun loadGifs(page: Int)

    // 刷新gif数据
    internal abstract fun refreshGifs()

    // 从数据库加载数据
    internal abstract fun loadGifsFromDB()

    // 数据集合大小
    internal abstract fun dataSetSize(): Int

    companion object {

        private const val TAG = "BaseGifsFragment"
    }

}
