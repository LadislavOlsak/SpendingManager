package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import java.io.Serializable

class StatisticsLocationPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    var mNumOfTabs: Int = 0
    lateinit var transactions: List<Transaction>
    lateinit var categories: List<Category>

    constructor(fm: FragmentManager, NumOfTabs: Int, Transactions: List<Transaction>, Categories: List<Category>) : this(fm) {
        this.mNumOfTabs = NumOfTabs
        this.transactions = Transactions
        this.categories = Categories
    }

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> {
                return StatisticsLocationTransactionsAmount.newInstance(transactions as Serializable, categories as Serializable)
            }
            1 -> {
                return StatisticsLocationTransactionsValue.newInstance(transactions as Serializable, categories as Serializable)
            }
            else -> return null
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }

}