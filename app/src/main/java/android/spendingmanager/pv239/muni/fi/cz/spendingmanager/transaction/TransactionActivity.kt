package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction

import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_transaction.*
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

class TransactionActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        //setSupportActionBar(toolbar)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        transaction_fab.setOnClickListener { save() }
    }

    private fun save() {
        Toast.makeText(this, "FAB Clicked!", Toast.LENGTH_LONG).show()
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            return 3
        }
    }

    class PlaceholderFragment : Fragment() {

        private var transactionItems : List<TransactionItem> = mutableListOf()
        private var adapter : TransactionItemsAdapter? = null

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_transaction, container, false)

            val list = rootView.findViewById(R.id.transaction_items_lv) as ListView
            adapter = TransactionItemsAdapter(this, getData())
            list.adapter = adapter

            val addTransactionItemBtn = rootView.findViewById(R.id.transaction_add_new_field_btn) as Button
            addTransactionItemBtn.setOnClickListener { addNewTransactionItem() }

            //rootView.section_label.text = getString(R.string.section_format, arguments.getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        private fun getData() : List<TransactionItem> {
            transactionItems += TransactionItem("NANANA", BigDecimal("54848.84"), Currency.getInstance("CZK"))
            return transactionItems
        }

        private fun addNewTransactionItem() {
            transactionItems += TransactionItem("Test", BigDecimal("0"), Currency.getInstance("CZK"))
            adapter?.updateData(transactionItems)
        }

        companion object {

            private val ARG_SECTION_NUMBER = "section_number"

            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
