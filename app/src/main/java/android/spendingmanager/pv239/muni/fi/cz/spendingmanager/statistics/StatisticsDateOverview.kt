package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.CategoryType
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.TransactionType
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.home.MainActivity
import android.support.v4.app.FragmentManager
import android.widget.AdapterView
import java.io.Serializable


class StatisticsDateOverview : Fragment() {

    companion object {

        fun newInstance(transactions: Serializable, categories: Serializable): StatisticsDateOverview {

            val args = Bundle()
            args.putSerializable("transactions", transactions)
            args.putSerializable("categories", categories)
            val fragment = StatisticsDateOverview()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.statistics_date_overview, container, false)

        val transactions = arguments?.getSerializable("transactions") as List<Transaction>
        val categories = arguments?.getSerializable("categories") as List<Category>

        val statisticsDateTabs = view.findViewById<View>(R.id.statistics_date_tabs) as TabLayout
        statisticsDateTabs.addTab(statisticsDateTabs.newTab().setText("Graphs"))
        statisticsDateTabs.addTab(statisticsDateTabs.newTab().setText("General"))
        statisticsDateTabs.tabGravity = TabLayout.GRAVITY_FILL

        val statisticsDateViewpager = view.findViewById<View>(R.id.statistics_date_viewpager) as ViewPager
        val statisticsDateAdapter = StatisticsDatePagerAdapter(childFragmentManager, statisticsDateTabs.tabCount, transactions, categories)
        statisticsDateViewpager.adapter = statisticsDateAdapter
        statisticsDateViewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(statisticsDateTabs))
        statisticsDateTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                statisticsDateViewpager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        return view
    }


}