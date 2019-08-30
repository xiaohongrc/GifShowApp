package com.hongenit.gifshowapp.widgets.tabs

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.hongenit.gifshowapp.R
import kotlinx.android.synthetic.main.tab_layout.view.*

/**
 * Created by Xiaohong on 2019-07-16.
 * desc:
 */
open class Tab(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr) {


    var titleText: CharSequence = ""

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?) : this(context, null)


    init {

        initView()

    }

    private fun initView() {
        inflate(context, R.layout.tab_layout, this)

    }


    fun putChecked(isChecked: Boolean) {
        tvTitle.isEnabled = isChecked
//        viewUnderline.isEnabled = isChecked
        ivTitleIcon.isEnabled = isChecked
    }

    // 设置图标
    fun setTitleIcon(imgResId: Int) {
        ivTitleIcon.setImageResource(imgResId)
    }


    // 设置标题文本
    fun setTitleText(text: String) {
        titleText = text
        tvTitle.text = text
    }

    // 设置标题文本颜色
    fun setTitleTextColor(color: Int) {
        tvTitle.setTextColor(color)
    }

    // 设置标题文本颜色选择器
    fun setTitleTextColor(colors: ColorStateList) {
        tvTitle.setTextColor(colors)
    }


    // 设置下划线背景
    fun setUnderlineBgColor(bgRes: Int) {
        viewUnderline.setBackgroundResource(bgRes)
    }


}