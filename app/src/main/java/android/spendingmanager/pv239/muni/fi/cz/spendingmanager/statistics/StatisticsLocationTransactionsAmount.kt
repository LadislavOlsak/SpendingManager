package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.TransactionActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.TransactionType
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.algo.Algorithm

class StatisticsLocationTransactionsAmount : Fragment(), OnMapReadyCallback,
        ClusterManager.OnClusterClickListener<StatisticsClusterItem>,
        ClusterManager.OnClusterItemClickListener<StatisticsClusterItem> {

    private var expenseOnlyList = mutableListOf<Transaction>()
    private var mapFragment : SupportMapFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.statistics_location_transaction_amount, container, false)

        val transactionsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val transactions = mutableListOf<Transaction>()
                dataSnapshot.children.mapNotNullTo(transactions) {
                    val transaction = it.getValue<Transaction>(Transaction::class.java)
                    transaction?.key = it.key
                    transaction
                }
                expenseOnlyList = transactions.filter { x ->
                    x.type == TransactionType.EXPENDITURE
                }.toMutableList()

                if(mapFragment == null) {
                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                    mapFragment = childFragmentManager.findFragmentById(R.id.mapAmount) as SupportMapFragment
                    mapFragment?.getMapAsync(this@StatisticsLocationTransactionsAmount)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        FirebaseDb.getUserReference("transactions")?.addValueEventListener(transactionsListener)

        return view
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        val mClusterManager: ClusterManager<StatisticsClusterItem> = ClusterManager<StatisticsClusterItem>(activity, googleMap)
        mClusterManager.renderer = StatisticsClusterManager(context as Context, googleMap, mClusterManager, false)
        mClusterManager.setOnClusterClickListener(this)
        mClusterManager.setOnClusterItemClickListener(this)

        val latList : MutableList<Double> = mutableListOf<Double>()
        val lngList : MutableList<Double> = mutableListOf<Double>()

        expenseOnlyList.forEachIndexed { index, transaction ->
            if(transaction.latitude != null && transaction.longitude != null) {
                val latitude = transaction.latitude as Double
                val longitude = transaction.longitude as Double
                latList.add(latitude)
                lngList.add(longitude)
                val offsetItem = StatisticsClusterItem(transaction, latitude, longitude, transaction.price.toString() + " CZK", "", transaction.price)
                mClusterManager.addItem(offsetItem)
            }
        }

        val center = LatLng(latList.average(), lngList.average())
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(center))
        googleMap.setOnCameraIdleListener(mClusterManager)
        googleMap.setOnMarkerClickListener(mClusterManager)
        mClusterManager.cluster()
    }

    override fun onClusterClick(p0: Cluster<StatisticsClusterItem>?): Boolean {
        //todo display list of transactions
        var message = ""
        p0?.items?.forEach{ x -> run {
            message += x.getItem().price.toString() + " CZK " + x.getItem().datetime.toString() + "\n"
        }}
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        return true
    }

    override fun onClusterItemClick(p0: StatisticsClusterItem?): Boolean {
        return false
    }

}