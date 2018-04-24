package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class StatisticsLocationOverview : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.statistics_location_overview, container, false)

        val statisticsLocationTabs = view.findViewById<View>(R.id.statistics_location_tabs) as TabLayout
        statisticsLocationTabs.addTab(statisticsLocationTabs.newTab().setText("Transaction count"))
        statisticsLocationTabs.addTab(statisticsLocationTabs.newTab().setText("Transaction value"))
        statisticsLocationTabs.tabGravity = TabLayout.GRAVITY_FILL

        val statisticsLocationViewpager = view.findViewById<View>(R.id.statistics_location_viewpager) as ViewPager
        val statisticsLocationAdapter = StatisticsLocationPagerAdapter(getFragmentManager() as FragmentManager, statisticsLocationTabs.tabCount)
        statisticsLocationViewpager.adapter = statisticsLocationAdapter
        statisticsLocationViewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(statisticsLocationTabs))
        statisticsLocationTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                statisticsLocationViewpager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        return view
    }

}