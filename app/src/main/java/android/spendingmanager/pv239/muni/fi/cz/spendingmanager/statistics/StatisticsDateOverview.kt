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


class StatisticsDateOverview : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.statistics_date_overview, container, false)

        val categories = listOf(
                Category("food", getString(R.string.food_drinks), CategoryType.DEFAULT),
                Category("housing", getString(R.string.housing), CategoryType.DEFAULT),
                Category("entertainment", getString(R.string.entertainment), CategoryType.DEFAULT),
                Category("others",getString(R.string.others), CategoryType.DEFAULT),
                Category("shopping",getString(R.string.shopping), CategoryType.DEFAULT)
        )

        val transactions = listOf(
                Transaction(TransactionType.EXPENDITURE, 250, categories.get(1), "Some meet and fruits", GregorianCalendar(2018, 3, 12, 12, 34) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 80, categories.get(1), "Some meet and fruits", GregorianCalendar(2018, 3, 13, 11, 21) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 40, categories.get(1), "Bread and rolls", GregorianCalendar(2018, 3, 13, 11, 22) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 850, categories.get(1), "Everything", GregorianCalendar(2018, 3, 16, 13, 50) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 563, categories.get(1), "Something...", GregorianCalendar(2018, 3, 25, 12, 40) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 85, categories.get(1), "Something...", GregorianCalendar(2018, 3, 28, 16, 23) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 420, categories.get(1), "Something...", GregorianCalendar(2018, 4, 2, 9, 20) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 236, categories.get(1), "Something...", GregorianCalendar(2018, 4, 6, 12, 20) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 2500, categories.get(2), "Something...", GregorianCalendar(2018, 3, 18, 19, 20) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 1500, categories.get(2), "Something...", GregorianCalendar(2018, 3, 25, 18, 30) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 3000, categories.get(2), "Something...", GregorianCalendar(2018, 4, 10, 18, 55) ,"" )
        )

        val statisticsDateTabs = view.findViewById<View>(R.id.statistics_date_tabs) as TabLayout
        statisticsDateTabs.addTab(statisticsDateTabs.newTab().setText("General"))
        statisticsDateTabs.addTab(statisticsDateTabs.newTab().setText("Graphs"))
        statisticsDateTabs.tabGravity = TabLayout.GRAVITY_FILL

        val statisticsDateViewpager = view.findViewById<View>(R.id.statistics_date_viewpager) as ViewPager
        val statisticsDateAdapter = StatisticsDatePagerAdapter(getFragmentManager(), statisticsDateTabs.tabCount, transactions)
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