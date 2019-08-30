package com.hongenit.gifshowapp.gifs

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.hongenit.gifshowapp.extension.dp2px


/**
 * 实现主界面Feed的左右两边间距相同的功能。
 *
 */
class SpaceItemDecoration(private val adapter: RecyclerView.Adapter<*>) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
        val type = adapter.getItemViewType(position)
        when (type) {
            WaterFallGifAdapter.TYPE_FEEDS -> if (spanIndex == 0) {
                outRect.left = dp2px(12f)
                outRect.right = dp2px(6f)
            } else { //if you just have 2 span . Or you can use (staggeredGridLayoutManager.getSpanCount()-1) as last span
                outRect.left = dp2px(6f)
                outRect.right = dp2px(12f)
            }
        }
    }

}
