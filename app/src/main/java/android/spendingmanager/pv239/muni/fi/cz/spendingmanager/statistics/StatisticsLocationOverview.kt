package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.io.Serializable


class StatisticsLocationOverview : Fragment() {

    companion object {

        fun newInstance(transactions: Serializable, categories: Serializable): StatisticsLocationOverview {

            val args = Bundle()
            args.putSerializable("transactions", transactions)
            args.putSerializable("categories", categories)
            val fragment = StatisticsLocationOverview()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.statistics_location_overview, container, false)

        val transactions = arguments?.getSerializable("transactions") as List<Transaction>
        val categories = arguments?.getSerializable("categories") as List<Category>

        val statisticsLocationTabs = view.findViewById<View>(R.id.statistics_location_tabs) as TabLayout
        statisticsLocationTabs.addTab(statisticsLocationTabs.newTab().setText("Transaction count"))
        statisticsLocationTabs.addTab(statisticsLocationTabs.newTab().setText("Transaction value"))
        statisticsLocationTabs.tabGravity = TabLayout.GRAVITY_FILL

        val statisticsLocationViewpager = view.findViewById<View>(R.id.statistics_location_viewpager) as ViewPager
        val statisticsLocationAdapter = StatisticsLocationPagerAdapter(getFragmentManager() as FragmentManager, statisticsLocationTabs.tabCount, transactions, categories)
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