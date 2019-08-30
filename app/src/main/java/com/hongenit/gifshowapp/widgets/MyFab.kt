package com.hongenit.gifshowapp.widgets

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet

/**
 * 自定义的Fab按钮，用于实现Fab偏移功能。
 */
class MyFab(context: Context, attrs: AttributeSet) : FloatingActionButton(context, attrs) {

    private var minOffset = 0

    fun setOffset(offset: Int) {
        if (offset.toFloat() != translationY) {
            val trans = Math.max(minOffset, offset)
            translationY = trans.toFloat()
        }
    }

    fun setMinOffset(minOffset: Int) {
        this.minOffset = minOffset
    }

}