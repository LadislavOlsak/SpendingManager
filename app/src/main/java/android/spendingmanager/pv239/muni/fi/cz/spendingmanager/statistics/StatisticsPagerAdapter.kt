package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.TransactionType
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.io.Serializable
import java.util.concurrent.Semaphore

class StatisticsPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter (fragmentManager) {

    var mNumOfTabs: Int = 0
    lateinit var transactions: List<Transaction>
    lateinit var categories: List<Category>

    fun update(list : List<Transaction>) {
        transactions = list
        notifyDataSetChanged()
    }

    constructor(fm: FragmentManager, NumOfTabs: Int, Transactions: List<Transaction>, Categories: List<Category>) : this(fm) {
        this.mNumOfTabs = NumOfTabs
        this.transactions = Transactions
        this.categories = Categories
    }

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> {
                return StatisticsDateOverview.newInstance(transactions as Serializable, categories as Serializable)
            }
            1 -> {
                return StatisticsDayTimeOverview.newInstance(transactions as Serializable, categories as Serializable)
            }
            2 -> {
                return StatisticsLocationOverview.newInstance(transactions as Serializable, categories as Serializable)
            }
            else -> return null
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }

}