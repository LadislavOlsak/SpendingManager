package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.loyaltycards

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.general.ScanActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.general.ViewMode
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_loyalty_cards.*

class LoyaltyCardsActivity : AppCompatActivity() {

    private val SCAN_ACTIVITY_REQUEST_CODE = 1
    private val parameterName = "membership_number"
    private var adapter : LoyaltyCardsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loyalty_cards)

        setListeners()
    }

    private fun setListeners() {
        adapter = LoyaltyCardsAdapter(this, emptyList())
        loyalty_cards_list.adapter = adapter

        val cardsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val loyaltyCards = mutableListOf<LoyaltyCard>()
                dataSnapshot.children.mapNotNullTo(loyaltyCards) {
                    val card = it.getValue<LoyaltyCard>(LoyaltyCard::class.java)
                    card?.key = it.key
                    card
                }
                adapter?.update(loyaltyCards)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        FirebaseDb.getUserReference("loyaltycards")?.addValueEventListener(cardsListener)

        fab.setOnClickListener { startScanActivity() }
        loyalty_cards_list.setOnItemClickListener { _, _, i, _ ->
            val selectedCard = loyalty_cards_list.adapter.getItem(i) as LoyaltyCard
            startLoyaltyCardActivity(selectedCard)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == this.SCAN_ACTIVITY_REQUEST_CODE) {
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
        newCardActivity.putExtra("mode", ViewMode.New.toString())
        startActivity(newCardActivity)
    }

    private fun startScanActivity() {
        val scanIntent = Intent(this, ScanActivity::class.java)
        scanIntent.putExtra("parameterName", parameterName)
        startActivityForResult(scanIntent, SCAN_ACTIVITY_REQUEST_CODE)
    }

    private fun startLoyaltyCardActivity(card : LoyaltyCard) {
        val intent = Intent(this, LoyaltyCardActivity::class.java)
        intent.putExtra("card", Gson().toJson(card))
        startActivity(intent)
    }
}
