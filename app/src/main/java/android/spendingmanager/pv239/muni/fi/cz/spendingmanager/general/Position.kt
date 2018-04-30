package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.general

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.google.android.gms.maps.model.LatLng

class Position {

    companion object {

        var location : LatLng? = null

        fun getPosition(context : Context, func : (loc : LatLng?) -> Unit) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val listener = object : LocationListener {
                override fun onLocationChanged(p0: Location?) {
                    location = LatLng(p0?.latitude as Double, p0.longitude)
                    func(location)
                }
                override fun onProviderDisabled(p0: String?) { }
                override fun onProviderEnabled(p0: String?) { }
                override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) { }
            }

            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000, 10.0f, listener)
            }catch (e : SecurityException) {
                e.printStackTrace()
            }
        }
    }
}