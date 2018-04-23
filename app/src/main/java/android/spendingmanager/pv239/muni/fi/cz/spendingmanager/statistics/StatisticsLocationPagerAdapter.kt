package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class StatisticsLocationPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    var mNumOfTabs: Int = 0

    constructor(fm: FragmentManager, NumOfTabs: Int) : this(fm) {
        this.mNumOfTabs = NumOfTabs
    }

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> {
                return StatisticsLocationTransactionsAmount()
            }
            1 -> {
                return StatisticsLocationTransactionsValue()
            }
            else -> return null
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }

}