package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.limits

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_limits.*
import java.math.BigDecimal
import java.util.*

class LimitsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_limits)

        val list = findViewById<ListView>(R.id.limits_settings_list)

        val categoryLimits = listOf(
            CategoryLimit(getString(R.string.food_drinks), true, BigDecimal("22032.34"), Currency.getInstance("CZK")),
            CategoryLimit(getString(R.string.housing), false, BigDecimal("763.11"), Currency.getInstance("EUR")),
            CategoryLimit(getString(R.string.entertainment), true, BigDecimal("22032.34"), Currency.getInstance("CZK")),
            CategoryLimit(getString(R.string.others), false, BigDecimal("763.11"), Currency.getInstance("EUR")),
            CategoryLimit(getString(R.string.shopping), true, BigDecimal("22032.34"), Currency.getInstance("CZK")),
            CategoryLimit(getString(R.string.shopping), false, BigDecimal("763.11"), Currency.getInstance("EUR")),
            CategoryLimit(getString(R.string.entertainment), true, BigDecimal("22032.34"), Currency.getInstance("CZK")),
            CategoryLimit(getString(R.string.food_drinks), false, BigDecimal("763.11"), Currency.getInstance("EUR")),
            CategoryLimit(getString(R.string.others), true, BigDecimal("22032.34"), Currency.getInstance("CZK")),
            CategoryLimit(getString(R.string.housing), false, BigDecimal("763.11"), Currency.getInstance("EUR"))
        )

        list.adapter = CategoryLimitsAdapter(this, categoryLimits)

        limits_save_changed_btn.setOnClickListener { saveChanges() }
    }

    private fun saveChanges() {
        Toast.makeText(this, "TODO: Changes are saved!", Toast.LENGTH_LONG).show()
    }
}
