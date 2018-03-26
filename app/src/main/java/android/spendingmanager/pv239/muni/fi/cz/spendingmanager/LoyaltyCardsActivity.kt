package android.spendingmanager.pv239.muni.fi.cz.spendingmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.adapters.LoyaltyCardsAdapter
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.entities.LoyaltyCard
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_loyalty_cards.*

class LoyaltyCardsActivity : AppCompatActivity() {

    private val SCAN_ACTIVITY_REQUEST_CODE = 1
    private val parameterName = "membership_number"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loyalty_cards)
        fab.setOnClickListener { startScanActivity() }

        val loyaltyCards = listOf(
                LoyaltyCard("129612631FDD", "Tesco"),
                LoyaltyCard("971R8B261", "Billa")
        )

        loyalty_cards_grid_view.adapter = LoyaltyCardsAdapter(this, loyaltyCards)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SCAN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val cardNumber = data.getStringExtra(parameterName)
                Toast.makeText(this, "Value :$cardNumber", Toast.LENGTH_LONG).show()
                //todo check if card exists
                startNewLoyaltyCardActivity(cardNumber)
            }
        }
    }

    private fun startNewLoyaltyCardActivity(cardNumber : String?) {
        val newCardActivity = Intent(this, NewLoyaltyCardActivity::class.java)
        newCardActivity.putExtra("cardNumber", cardNumber)
        startActivity(newCardActivity)
        //todo refresh view
    }

    private fun startScanActivity() {
        val scanIntent = Intent(this@LoyaltyCardsActivity, ScanActivity::class.java)
        scanIntent.putExtra("parameterName", parameterName)
        startActivityForResult(scanIntent, SCAN_ACTIVITY_REQUEST_CODE)
    }
}
