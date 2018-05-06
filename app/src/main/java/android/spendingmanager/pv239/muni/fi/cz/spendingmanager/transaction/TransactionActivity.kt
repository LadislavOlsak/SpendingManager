package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction

import android.app.Activity
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import kotlinx.android.synthetic.main.activity_transaction.*
import java.util.*
import android.content.pm.PackageManager
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.CategoryType
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.general.Position
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatSpinner
import android.widget.*
import java.text.SimpleDateFormat
import android.content.Intent
import android.os.Parcelable
import android.provider.Settings
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.DefaultCategories
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.general.DatePickerDialog
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.location.LocationActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.location.PoiPlace
import android.support.v4.app.*
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime


class TransactionActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private val MY_PERMISSION_ACCESS_COARSE_LOCATION = 11
    private val LOCATION_ACTIVITY_REQUEST = 13

    companion object {
        var isLocationGranted = false
        var location : LatLng? = null
        var poiPlace : PoiPlace? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        //setSupportActionBar(toolbar)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        val result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION )
        if ( result != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, Array<String>(1) {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    MY_PERMISSION_ACCESS_COARSE_LOCATION )
        } else if (result == PackageManager.PERMISSION_GRANTED) {
            isLocationGranted = true
            Position.getPosition(this, this::setPosition)
            turnOnGps()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == MY_PERMISSION_ACCESS_COARSE_LOCATION) {
            TransactionActivity.isLocationGranted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            if(isLocationGranted) {
                turnOnGps()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(data != null) {
            //https://stackoverflow.com/questions/5104269/using-startactivityforresult-how-to-get-requestcode-in-child-activity
            val reqCode : Int = data.getIntExtra("requestCode", 0)
            if(reqCode == LOCATION_ACTIVITY_REQUEST) {
                val place = Gson().fromJson(data.getStringExtra("place"), PoiPlace::class.java)
                poiPlace = place
                setPoiPlaceInFragment(place)
            }
        }
    }

    private fun setPoiPlaceInFragment(place : PoiPlace) {
        //TODO not only first
        val currLayout = container.getChildAt(container.currentItem)
        if(currLayout != null) {
            val placeNameTv = currLayout.findViewById(R.id.transaction_item_place_name) as TextView
            placeNameTv.text = place.name
            val placeDataTv = currLayout.findViewById(R.id.transaction_item_place_data_tv) as TextView
            placeDataTv.text = Gson().toJson(place)
        }

//        currFragment.set
//        val selectedFragment = mSectionsPagerAdapter?.fragments?.get(0)
//        selectedFragment?.setPoiPlace(place)
    }

    private fun turnOnGps() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun setPosition(loc : LatLng?) {
        //Position.getPosition(this, this::setPosition)
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        //var fragments = mutableListOf<PlaceholderFragment>()

        override fun getItem(position: Int): Fragment {
            var fragment = PlaceholderFragment.newInstance(position + 1)
            fragment.fragmentPosition = position
            //fragments.add(fragment)

            return fragment
        }

        override fun getCount(): Int {
            return 2
        }
    }

    class PlaceholderFragment : Fragment(), android.app.DatePickerDialog.OnDateSetListener {

        private val LOCATION_ACTIVITY_REQUEST = 13
        private var dateEt : EditText? = null

        override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {

            val c = Calendar.getInstance()
            c.set(p1, p2, p3)
            dateEt?.setText(SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(c.time))
        }

        var fragmentPosition : Int = -1

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            super.onCreateView(inflater, container, savedInstanceState)

            val rootView = inflater.inflate(R.layout.fragment_transaction, container, false)

            dateEt = rootView?.findViewById(R.id.transaction_item_date) as EditText
            dateEt?.setText(SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Calendar.getInstance().time))

            val transactionFab = rootView.findViewById<View>(R.id.transaction_fab) as FloatingActionButton
            transactionFab.setOnClickListener { save(rootView, fragmentPosition) }

            FirebaseDb.getUserReference("categories")?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val categories = DefaultCategories.getDefaultCategories().map { x -> x.categoryName }.toMutableList()
                    snapshot.children.forEach { postSnapshot ->
                        val category = postSnapshot.getValue<Category>(Category::class.java) as Category
                        category.key = postSnapshot.key
                        categories.add(category.categoryName)
                    }
                    val categorySpinner = rootView.findViewById(R.id.transaction_item_category) as AppCompatSpinner
                    categorySpinner.adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, categories)
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })

            val dateBtn = rootView.findViewById(R.id.transaction_item_date_btn) as Button
            dateBtn.setOnClickListener { DatePickerDialog(activity as Activity).create(this).show() }

            val mapBtn = rootView.findViewById(R.id.transaction_item_map_btn) as Button
            mapBtn.setOnClickListener {
                activity?.startActivityForResult(Intent(activity, LocationActivity::class.java), LOCATION_ACTIVITY_REQUEST)
            }

            return rootView
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

        private fun save(rootView : View, fragmentPosition: Int) {

            val placeDataTv = rootView.findViewById(R.id.transaction_item_place_data_tv) as TextView
            var place : PoiPlace? = null
            if(!placeDataTv.text.isNullOrEmpty()) {
                place = Gson().fromJson(placeDataTv.text.toString(), PoiPlace::class.java)
            }

            if(TransactionActivity.isLocationGranted) {
                location = if(place != null) {
                    LatLng(place.latitude, place.longitude)
                } else {
                    Position.location
                }
            }

            val categorySpinner = rootView.findViewById(R.id.transaction_item_category) as AppCompatSpinner
            val noteEt = rootView.findViewById(R.id.transaction_item_note) as EditText
            val priceEt = rootView.findViewById(R.id.transaction_item_price) as EditText
            val dateEt = rootView.findViewById(R.id.transaction_item_date) as EditText

            val selectedDate: Date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(dateEt.text.toString())
            val type : TransactionType = if(fragmentPosition == 0) TransactionType.EXPENDITURE else TransactionType.INCOME
            val price = priceEt.text.toString().toDoubleOrNull()
            val description = noteEt.text.toString()
            val category = Category( categorySpinner.selectedItem.toString(), categorySpinner.selectedItem.toString(), CategoryType.DEFAULT)

            if(price == null || price == 0.0) {
                Toast.makeText(activity, "Price cannot be empty or zero.", Toast.LENGTH_LONG).show()
                return
            }

            val newTransaction = Transaction(type, price, category, description, selectedDate, location, "CZK")

            FirebaseDb().createObject("transactions", newTransaction)
            Toast.makeText(activity, "Transaction created!", Toast.LENGTH_SHORT).show()

            activity?.finish()
        }
    }
}
