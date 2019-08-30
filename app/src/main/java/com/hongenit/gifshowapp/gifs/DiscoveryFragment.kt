package com.hongenit.gifshowapp.gifs

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hongenit.gifshowapp.BaseFragment
import com.hongenit.gifshowapp.R
import kotlinx.android.synthetic.main.fragment_discovery.*
import java.util.ArrayList

/**
 * Created by Xiaohong on 2019-07-18.
 * desc:
 */
class DiscoveryFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_discovery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        activity?.let {
            val adapter = DiscoveryPagerAdapter(it.supportFragmentManager)
            adapter.addFragment(HotFragment(), getString(R.string.discovery_hot))
            adapter.addFragment(NewFragment(), getString(R.string.discovery_new))
            vpDiscovery.adapter = adapter
        }
        tabs.setupWithViewPager(vpDiscovery)
    }


    inner class DiscoveryPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val mFragments = ArrayList<Fragment>()
        private val mFragmentTitle = ArrayList<String>()

        fun addFragment(fragment: Fragment, title: String) {
            mFragments.add(fragment)
            mFragmentTitle.add(title)
        }

        override fun getItem(p0: Int): Fragment {
            return mFragments[p0]
        }

        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitle[position]
        }
    }


}