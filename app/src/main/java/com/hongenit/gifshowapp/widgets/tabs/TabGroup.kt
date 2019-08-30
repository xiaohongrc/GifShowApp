package com.hongenit.gifshowapp.widgets.tabs

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * Created by Xiaohong on 2019-07-17.
 * desc:
 */
class TabGroup(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr), ViewPager.OnPageChangeListener {
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?) : this(context, null)

    private lateinit var mViewPager: ViewPager
    // 当前选中的tab索引
    private var mCurrentIndex: Int = -1

    private val mTabs = mutableListOf<Tab>()


    init {
        initView()
    }

    private fun initView() {
        orientation = HORIZONTAL
    }


    // 将子tab添加到容器
    fun addTab(tab: Tab) {
        addView(tab)
        mTabs.add(tab)
        tab.setOnClickListener {
            val index = mTabs.indexOf(tab)
            select(index)
            mViewPager.currentItem = index
        }
    }

    // 选中tab
    private fun select(index: Int) {
        if (mCurrentIndex == index) {
            return
        }
        mCurrentIndex = index

        // 更新所有tab的选中状态
        for (i in 0 until mTabs.size) {
            mTabs[i].putChecked(i == index)
        }
    }


    // 设置关联的viewpager
    fun setUpViewPager(viewPager: ViewPager) {
        mViewPager = viewPager
        val pagerAdapter = viewPager.adapter
        pagerAdapter?.let { adapter ->
            for (i in 0 until adapter.count) {
                adapter.getPageTitle(i)
            }
        }
        viewPager.addOnPageChangeListener(this)
        select(0)

    }


    override fun onPageScrollStateChanged(p0: Int) {
        println("onPageScrollStateChanged p0 = $p0")
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
        println("onPageScrolled p0 = $p0  p1 = $p1  p2 = $p2 ")
    }

    override fun onPageSelected(position: Int) {
        select(position)
    }


}