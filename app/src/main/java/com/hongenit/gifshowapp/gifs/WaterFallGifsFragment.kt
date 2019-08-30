package com.hongenit.gifshowapp.gifs

import android.os.Bundle
import android.support.v7.widget.SimpleItemAnimator
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.callback.LoadDataListener
import com.hongenit.gifshowapp.extension.dp2px
import org.greenrobot.eventbus.EventBus

/**
 * 展示瀑布流的Feeds内容。
 *
 */
abstract class WaterFallGifsFragment : BaseGifsFragment(), LoadDataListener {

    /**
     * 通过获取屏幕宽度来计算出每张图片的宽度。
     *
     * @return 计算后得出的每张图片的宽度。
     */
    internal val imageWidth: Int
        get() {
            val columnWidth = screenWidth / COLUMN_COUNT
            return columnWidth - dp2px(18f)
        }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_water_fall_feeds, container, false)
        initViews(view)
        return super.onCreateView(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun setupRecyclerView() {
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        (layoutManager as StaggeredGridLayoutManager).gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    companion object {

        private const val TAG = "WaterFallFeedsFragment"

        private const val COLUMN_COUNT = 2
    }
}