package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.loyaltycards

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.general.ScanActivity
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_loyalty_cards.*

class LoyaltyCardsActivity : AppCompatActivity() {

    private val SCAN_ACTIVITY_REQUEST_CODE = 1
    private val parameterName = "membership_number"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loyalty_cards)
        fab.setOnClickListener { startScanActivity() }

        val loyaltyCards = listOf(
            LoyaltyCard("501928794164", "MultiSport", Color.BLUE),
            LoyaltyCard("971R8B261", "Billa", Color.MAGENTA),
            LoyaltyCard("1234567890", "Tesco", Color.YELLOW),
            LoyaltyCard("S421234567890", "ISIC", Color.GREEN)
        )

        loyalty_cards_list.adapter = LoyaltyCardsAdapter(this, loyaltyCards)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SCAN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val cardNumber = data.getStringExtra(parameterName)
                //todo check if card exists
                startNewLoyaltyCardActivity(cardNumber)
            } else if(resultCode == Activity.RESULT_CANCELED) {
                startNewLoyaltyCardActivity(null)
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
