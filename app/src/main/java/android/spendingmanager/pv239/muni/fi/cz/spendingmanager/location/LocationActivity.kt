package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.location

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.gson.Gson

class LocationActivity : AppCompatActivity() {

    private val PLACE_PICKER_REQUEST = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        val builder = PlacePicker.IntentBuilder()
        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PLACE_PICKER_REQUEST) {
            if(resultCode == RESULT_OK) {
                val place : Place = PlacePicker.getPlace(this, data)
                val output = Intent()
                val placePoi = PoiPlace(place.name.toString(), place.address.toString(), place.latLng.latitude, place.latLng.longitude)
                output.putExtra("place", Gson().toJson(placePoi))
                output.putExtra("requestCode", 13)
                setResult(Activity.RESULT_OK, output)
                finish()
            }
        }

        finish()
    }
}
