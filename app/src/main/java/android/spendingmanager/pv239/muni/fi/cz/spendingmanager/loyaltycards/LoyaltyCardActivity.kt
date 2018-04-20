package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.loyaltycards

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_loyalty_card.*

class LoyaltyCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loyalty_card)

        initView()
    }

    private fun initView() {
        val loyaltyCard = getCard()
        loyalty_card_iv.setImageBitmap(BarcodeImageGenerator.getBarcodeBitmap(loyaltyCard.cardNumber))
        title = loyaltyCard.cardName
    }

    private fun getCard() : LoyaltyCard {
        return Gson().fromJson(intent.getStringExtra("card"), LoyaltyCard::class.java)
    }
}
