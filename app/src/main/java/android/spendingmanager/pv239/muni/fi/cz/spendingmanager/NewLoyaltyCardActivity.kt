package android.spendingmanager.pv239.muni.fi.cz.spendingmanager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_new_loyalty_card.*

class NewLoyaltyCardActivity(
        private var cardNumber : String? = null
) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_loyalty_card)

        cardNumber = intent.getStringExtra("cardNumber")

        setFields()
    }

    private fun setFields() {
        if(cardNumber != null && cardNumber?.isNotEmpty() == true) {
            new_loyalty_card_number.setText(cardNumber)
        }
    }

}
