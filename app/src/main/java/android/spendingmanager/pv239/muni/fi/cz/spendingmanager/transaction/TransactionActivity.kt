package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
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
import android.support.v7.widget.AppCompatSpinner
import android.widget.*
import java.text.SimpleDateFormat
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.DefaultCategories
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.location.LocationActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.location.PoiPlace
import android.support.v4.app.*
import android.support.v7.app.AlertDialog
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import org.ankit.gpslibrary.MyTracker

class TransactionActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private val MY_PERMISSION_ACCESS_COARSE_LOCATION = 11
    private val LOCATION_ACTIVITY_REQUEST = 13
    private val mPermission = Manifest.permission.ACCESS_FINE_LOCATION
    private var locationManager : LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        locationManager = getSystemService(android.content.Context.LOCATION_SERVICE) as android.location.LocationManager
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(mPermission, Manifest.permission.READ_PHONE_STATE),
                        MY_PERMISSION_ACCESS_COARSE_LOCATION)
            }else{
                getLocation()
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    private fun getLocation() {
        if(locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == false) {
            val dialogClickListener = DialogInterface.OnClickListener { _, which: Int ->
                when(which) {
                    DialogInterface.BUTTON_POSITIVE -> { turnOnGps() }
                    DialogInterface.BUTTON_NEGATIVE -> { }
                }
            }
            AlertDialog.Builder(this)
                    .setMessage("Do you want to enable location?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show()
        }
        setPoiPlaceInFragment(PoiPlace(MyTracker(this)))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSION_ACCESS_COARSE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                println("Permission denied!")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(data != null) {
            //https://stackoverflow.com/questions/5104269/using-startactivityforresult-how-to-get-requestcode-in-child-activity
            val reqCode : Int = data.getIntExtra("requestCode", 0)
            if(reqCode == LOCATION_ACTIVITY_REQUEST) {
                val place = Gson().fromJson(data.getStringExtra("place"), PoiPlace::class.java)
                setPoiPlaceInFragment(place)
            }
        }
    }

    private fun setPoiPlaceInFragment(place : PoiPlace) {
        setPlaceName(place, 0)
        setPlaceName(place, 1)
    }

    private fun setPlaceName(place: PoiPlace, fragment : Int) {
        val currLayout = container.getChildAt(fragment)
        if(currLayout != null) {
            val placeNameTv = currLayout.findViewById(R.id.transaction_item_place_name) as TextView
            placeNameTv.text = place.name
            val placeDataTv = currLayout.findViewById(R.id.transaction_item_place_data_tv) as TextView
            placeDataTv.text = Gson().toJson(place)
        }
    }

    private fun turnOnGps() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            val fragment = PlaceholderFragment.newInstance(position + 1)
            fragment.fragmentPosition = position

            return fragment
        }

        override fun getCount(): Int {
            return 2
        }
    }

    class PlaceholderFragment : Fragment() {

        private val LOCATION_ACTIVITY_REQUEST = 13
        private var dateTv : TextView? = null
        var fragmentPosition : Int = -1
        private val dateTimeFormat = "dd-MM-yyyy HH:mm"

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            super.onCreateView(inflater, container, savedInstanceState)

            val rootView = inflater.inflate(R.layout.fragment_transaction, container, false)

            dateTv = rootView?.findViewById(R.id.transaction_item_date) as TextView
            setDateAndTime(Calendar.getInstance().time)

            initCategories(rootView)

            initButtonListeners(rootView)

            return rootView
        }

        private fun initButtonListeners(rootView: View) {

            val transactionFab = rootView.findViewById<View>(R.id.transaction_fab) as FloatingActionButton
            transactionFab.setOnClickListener { save(rootView, fragmentPosition) }

            val timeBtn = rootView.findViewById(R.id.transaction_item_time_btn) as Button
            timeBtn.setOnClickListener {
                DateTimePickerDialog().create(activity as Activity).setListener { date ->
                    setDateAndTime(date)
                }.display()
            }

            val mapBtn = rootView.findViewById(R.id.transaction_item_map_btn) as Button
            mapBtn.setOnClickListener {
                activity?.startActivityForResult(Intent(activity, LocationActivity::class.java), LOCATION_ACTIVITY_REQUEST)
            }
        }

        private fun initCategories(rootView: View) {
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
        }

        private fun setDateAndTime(date : Date) {
            dateTv?.text = SimpleDateFormat(dateTimeFormat, Locale.getDefault()).format(date)
        }

        private fun save(rootView : View, fragmentPosition: Int) {
            saveNewTransaction(createTransactionFromInput(rootView, fragmentPosition))
        }

        private fun getLocation(rootView: View) : LatLng? {
            val placeDataTv = rootView.findViewById(R.id.transaction_item_place_data_tv) as TextView
            var place: PoiPlace? = null
            if (!placeDataTv.text.isNullOrEmpty()) {
                place = Gson().fromJson(placeDataTv.text.toString(), PoiPlace::class.java)
            }
            return if (place != null) {
                LatLng(place.latitude, place.longitude)
            } else {
                null
            }
        }

        private fun saveNewTransaction(newTransaction: Transaction?) {
            if(newTransaction != null) {
                FirebaseDb().createObject("transactions", newTransaction)
                Toast.makeText(activity, "Transaction created!", Toast.LENGTH_SHORT).show()

                activity?.finish()
            }
        }

        private fun createTransactionFromInput(rootView: View, fragmentPosition: Int) : Transaction? {
            val categorySpinner = rootView.findViewById(R.id.transaction_item_category) as AppCompatSpinner
            val noteEt = rootView.findViewById(R.id.transaction_item_note) as EditText
            val priceEt = rootView.findViewById(R.id.transaction_item_price) as EditText
            val dateTv = rootView.findViewById(R.id.transaction_item_date) as TextView

            val selectedDate: Date = SimpleDateFormat(dateTimeFormat, Locale.getDefault()).parse(dateTv.text.toString())
            val type: TransactionType = if (fragmentPosition == 0) TransactionType.EXPENDITURE else TransactionType.INCOME
            val price = priceEt.text.toString().toDoubleOrNull()
            val description = noteEt.text.toString()
            val category = Category(categorySpinner.selectedItem.toString(), categorySpinner.selectedItem.toString(), CategoryType.DEFAULT)

            if (price == null || price == 0.0) {
                Toast.makeText(activity, "Price cannot be empty or zero.", Toast.LENGTH_LONG).show()
                return null
            }

            val locationLatLng = getLocation(rootView)

            return Transaction(type, price, category, description, selectedDate, locationLatLng, "CZK")
        }

        companion object {

            private const val ARG_SECTION_NUMBER = "section_number"

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
