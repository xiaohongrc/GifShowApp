package com.hongenit.gifshowapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import com.hongenit.gifshowapp.collect.CollectFragment
import com.hongenit.gifshowapp.extension.showToast
import com.hongenit.gifshowapp.gifs.BaseGifsFragment
import com.hongenit.gifshowapp.gifs.DiscoveryFragment
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.response.BaseResponse
import com.hongenit.gifshowapp.network.response.FetchMyInfo
import com.hongenit.gifshowapp.meinfo.UpdateNicknameResponse
import com.hongenit.gifshowapp.util.UserModel
import com.hongenit.gifshowapp.widgets.EditRemarkDialog
import com.hongenit.gifshowapp.widgets.tabs.Tab
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : BaseActivity() {


    companion object {

        private const val TAG = "MainActivity"

        fun actionStart(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
//        GlobalUtil.setStatusBarFullTransparent(this)

        initView()
    }

    private fun initView() {
        fetchMyInfo()

        adapter = MainViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(DiscoveryFragment(), createMainTab(TAB_INDEX_DISCOVERY))
        adapter.addFragment(MeFragment(), createMainTab(TAB_INDEX_ME))
        adapter.addFragment(CollectFragment(), createMainTab(TAB_INDEX_POST))

        vpMain.offscreenPageLimit = 3
        vpMain.adapter = adapter
        mainTabs.setUpViewPager(vpMain)
        vpMain.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(position: Int) {
                currentPagerPosition = position
                executePendingRunnable()
            }
        })

    }


    /**
     * 执行Pending任务，用于同步ViewPager各面页签之间的状态。
     */
    private fun executePendingRunnable() {
        val fragment = adapter.getItem(currentPagerPosition)
        if (fragment is BaseGifsFragment) {
            fragment.executePendingRunnableList()
        }
    }

    override fun onResume() {
        super.onResume()
        executePendingRunnable()
    }


    private fun createMainTab(index: Int): Tab {
        val mainTab = Tab(this)

        val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT)
        layoutParams.weight = 1f
        layoutParams.gravity = Gravity.CENTER
        mainTab.layoutParams = layoutParams

        when (index) {
            TAB_INDEX_POST -> {
                mainTab.setTitleText(getString(R.string.collect))
                mainTab.setTitleIcon(R.drawable.selector_main_tab_collect)
            }
            TAB_INDEX_DISCOVERY -> {
                mainTab.setTitleText(getString(R.string.discovery))
                mainTab.setTitleIcon(R.drawable.selector_main_tab_discovery)

            }
            TAB_INDEX_ME -> {
                mainTab.setTitleText(getString(R.string.me))
                mainTab.setTitleIcon(R.drawable.selector_main_tab_me)

            }
        }
        return mainTab

    }


    private var mExitTime: Long = 0
    private val mCurrentItem: BaseFragment? = null
    private val TAB_INDEX_DISCOVERY: Int = 0
    private val TAB_INDEX_ME: Int = 1
    private val TAB_INDEX_POST: Int = 2
    private var currentPagerPosition: Int = TAB_INDEX_DISCOVERY
    private lateinit var adapter: MainViewPagerAdapter


    /**
     * 获取我的信息
     */
    private fun fetchMyInfo() {
        showLoadingDialog()
        FetchMyInfo.getResponse(object : Callback {
            override fun onResponse(baseResponse: BaseResponse) {
                val signUser = (baseResponse as FetchMyInfo).signUser
                signUser?.let {
                    if (signUser.nickname.isNullOrEmpty()) {
                        showEditNicknameDialog()
                    } else {
//                    refreshUserInfo(signUser)
                    }
                    UserModel.saveUser(it)
                }
                dismissLoadingDialog()
            }

            override fun onFailure(e: Exception) {
                dismissLoadingDialog()
            }
        })

    }

    // 修改昵称弹窗
    private fun showEditNicknameDialog() {
        val remarkDialog = EditRemarkDialog(this, UserModel.getSignInUser().nickname)
        remarkDialog.setOnIActionListener(object : EditRemarkDialog.IActionListener {
            override fun close() {
            }

            override fun confirm(nickname: String) {
                updateNickname(nickname)
            }

        })
        remarkDialog.show()
    }


    // 请求修改昵称。
    private fun updateNickname(nickname: String) {
        showLoadingDialog()
        UpdateNicknameResponse.getResponse(nickname, object : Callback {
            override fun onResponse(baseResponse: BaseResponse) {
                val user = (baseResponse as UpdateNicknameResponse).signUser
                UserModel.saveUser(user)
                dismissLoadingDialog()
            }

            override fun onFailure(e: Exception) {
                dismissLoadingDialog()
            }

        })
    }


    inner class MainViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val mFragments = ArrayList<Fragment>()
        private val mFragmentTitleTab = ArrayList<Tab>()

        fun addFragment(fragment: Fragment, tab: Tab) {
            mFragments.add(fragment)
            mFragmentTitleTab.add(tab)
        }

        override fun getItem(p0: Int): Fragment {
            return mFragments[p0]
        }

        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            mainTabs.addTab(mFragmentTitleTab[position])
            return mFragmentTitleTab[position].titleText
        }


    }


    override fun onBackPressed() {
        if (currentPagerPosition != TAB_INDEX_DISCOVERY) {
            setCurrentPage(TAB_INDEX_DISCOVERY)
            return
        }
        if (exitConfirm()) {
            super.onBackPressed()
        }
    }


    private fun exitConfirm(): Boolean {
        if (System.currentTimeMillis() - mExitTime < 2000) {
            return true
        } else {
            showToast(getString(R.string.str_exit_tap_again), Toast.LENGTH_SHORT)
            mExitTime = System.currentTimeMillis()
            return false
        }
    }

    private fun setCurrentPage(pageIndex: Int) {
        vpMain.setCurrentItem(pageIndex, true)

    }

}