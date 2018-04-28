package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.content.Context
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager



class StatisticsLocationTransactionsAmount : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.statistics_location_transaction_amount, container, false)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = getChildFragmentManager().findFragmentById(R.id.mapAmount) as SupportMapFragment
        mapFragment.getMapAsync(this)

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
        val mClusterManager: ClusterManager<StatisticsClusterItem>

        mMap = googleMap
        mClusterManager = ClusterManager<StatisticsClusterItem>( getActivity(), mMap)
        mClusterManager.renderer = StatisticsClusterManager(context as Context, mMap, mClusterManager, false)

        var latList : MutableList<Double> = mutableListOf<Double>()
        var lngList : MutableList<Double> = mutableListOf<Double>()

        val transactions : List<Transaction> = StatisticsHelper().GetTransactions()
        transactions.forEachIndexed { index, transaction ->
            val location = transaction.position
            //mMap.addMarker(MarkerOptions().position(location).title(transaction.category.categoryName + ": " + transaction.amount))
            latList.add(location.latitude)
            lngList.add(location.longitude)
            val offsetItem = StatisticsClusterItem(location.latitude, location.longitude, transaction.price)
            mClusterManager.addItem(offsetItem)
        }

        val center = LatLng(latList.average(), lngList.average())
        mMap.moveCamera(CameraUpdateFactory.newLatLng(center))

        mMap.setOnCameraIdleListener(mClusterManager)
        mMap.setOnMarkerClickListener(mClusterManager)
    }
}