package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class StatisticsPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter (fragmentManager) {

    var mNumOfTabs: Int = 0
    lateinit var transactions: List<Transaction>

    constructor(fm: FragmentManager, NumOfTabs: Int, Transactions: List<Transaction>) : this(fm) {
        this.mNumOfTabs = NumOfTabs
        this.transactions = Transactions
    }

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> {
                return StatisticsDateOverview()
            }
            1 -> {
                return StatisticsDayTimeOverview()
            }
            2 -> {
                return StatisticsLocationOverview()
            }
            else -> return null
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }

}