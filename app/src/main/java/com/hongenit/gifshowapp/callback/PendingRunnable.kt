package com.hongenit.gifshowapp.callback

/**
 * 用于延迟执行的特定Runnable，允许在执行的时候传入index参数。专为解决主界面ViewPager之间页签切换时点赞状态同步不准确的问题。
 */
interface PendingRunnable {

    fun run(index: Int)

}
