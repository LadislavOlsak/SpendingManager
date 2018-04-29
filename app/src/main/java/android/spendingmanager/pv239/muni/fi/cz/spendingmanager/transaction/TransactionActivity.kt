package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction

import android.app.Activity
import android.content.Context
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
import kotlinx.android.synthetic.main.activity_transaction.*
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList
import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import android.location.LocationListener
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.*
import com.google.android.gms.maps.model.LatLng



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
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            var fragment = PlaceholderFragment.newInstance(position + 1)
            fragment.fragmentPosition = position
            return fragment
        }

        override fun getCount(): Int {
            return 3
        }
    }

    class PlaceholderFragment : Fragment() {

        var fragmentPosition : Int = -1
        private var transactionItems : List<Transaction> = mutableListOf()
        private var adapter : TransactionItemsAdapter? = null

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_transaction, container, false)

            val list = rootView.findViewById<View>(R.id.transaction_items_lv) as ListView
            adapter = TransactionItemsAdapter(this, getData())
            list.adapter = adapter

            val addTransactionItemBtn = rootView.findViewById<View>(R.id.transaction_add_new_field_btn) as Button
            addTransactionItemBtn.setOnClickListener { addNewTransactionItem() }

            val transactionFab = rootView.findViewById<View>(R.id.transaction_fab) as FloatingActionButton
            transactionFab.setOnClickListener { save(rootView, fragmentPosition) }

            //rootView.section_label.text = getString(R.string.section_format, arguments.getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        private fun getData() : List<Transaction> {
            transactionItems += Transaction()
            return transactionItems
        }

        private fun addNewTransactionItem() {
            transactionItems += Transaction()
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

        private fun save(rootView: View, fragmentPosition: Int) {

            var type : TransactionType
            var price : Int = 0
            var category: Category = Category()
            var description: String = ""
            var datetime: GregorianCalendar
            var position: LatLng? = null
            var priceCurrency : Currency = Currency.getInstance("CZK" )
            var gps : LatLng? = null

            // Set category
            if (fragmentPosition == 0)
            {
                type = TransactionType.TRANSFER
            }
            else if (fragmentPosition == 1)
            {
                type = TransactionType.EXPENDITURE
            }
            else //(fragmentPosition == 2)
            {
                type = TransactionType.INCOME
            }

            // Set GPS
            val MY_PERMISSION_ACCESS_COARSE_LOCATION = 11
            val locationManager = getActivity()?.getSystemService(LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                var listener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        gps = LatLng(location.getLatitude(), location.getLongitude())
                    }
                    override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
                    override fun onProviderEnabled(s: String) {}
                    override fun onProviderDisabled(s: String) {}
                }
                // Tady bude asi v reálu problém
                if ( ContextCompat.checkSelfPermission( getContext() as Context, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( getActivity() as Activity, Array<String>(1) {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                            MY_PERMISSION_ACCESS_COARSE_LOCATION );
                }
                // vyhazuje výjimku
                // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10.0f, listener);
            }

            // Set time
            datetime =  GregorianCalendar.getInstance() as GregorianCalendar

            // Set user items
            val transactionsView = rootView.findViewById<View>(R.id.transaction_items_lv) as ListView
            val transactionsCount = transactionsView.getCount()
            for (i in 0..(transactionsCount - 1)) {

                // Nevrací View, ze kterého by šlo načíst hodnoty, ale přímo Transaction (která je ale prázdná)
                //val transactionItem = transactionsView.getItemAtPosition(i) as View
                //val priceEdit = transactionItem?.findViewById<View>(R.id.transaction_item_price) as EditText

                price = 500
                category = Category()
                description = "Mounthly rent"
                priceCurrency = Currency.getInstance("CZK" )
            }


            val newTransaction = Transaction(type, price, category, description, datetime, gps, priceCurrency)
            //FirebaseDb().createObject("transactions", newTransaction)
            //finish()

            Toast.makeText(getActivity(), "FAB Clicked!", Toast.LENGTH_LONG).show()
        }
    }
}
