package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.loyaltycards

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.graphics.drawable.Drawable
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.general.ViewMode
import android.support.v4.content.ContextCompat
import com.google.gson.Gson

/**
 * Created by Stefan on 26-Mar-18.
 */
class LoyaltyCardsAdapter(
        private val context: Context,
        private var loyaltyCards : List<LoyaltyCard>
) : BaseAdapter() {

    fun update(list : List<LoyaltyCard>) {
        loyaltyCards = list
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val loyaltyCard = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.loyalty_card_item, parent, false)
        }

        val cardNameTv = view?.findViewById(R.id.loyalty_cards_item_name_tv) as TextView
        cardNameTv.text = loyaltyCard.cardName

        val cardEanIv = view.findViewById(R.id.loyalty_card_item_barcode_iv) as ImageView
        cardEanIv.setImageBitmap(BarcodeImageGenerator.getBarcodeBitmap(loyaltyCard.cardNumber))

        val cardTileLayout = view.findViewById(R.id.loyalty_card_item_tile_layout) as LinearLayout
        cardTileLayout.background = getColoredBackground(loyaltyCard.color)
        val editCardIv = view.findViewById(R.id.loyalty_cards_item_edit_iv) as ImageView
        val deleteCardIv = view.findViewById(R.id.loyalty_cards_item_delete_iv) as ImageView
        editCardIv.setOnClickListener { startNewLoyaltyCardActivity(loyaltyCard) }
        deleteCardIv.setOnClickListener { displayConfirmationDialog(loyaltyCard) }

        return view
    }

    override fun getItem(position: Int): LoyaltyCard = loyaltyCards[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = loyaltyCards.size

    private fun getColoredBackground(color : Int) : Drawable {
        val border = getBorderDrawable()
        border.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
        return border
    }

    private fun getBorderDrawable(): Drawable {
        return ContextCompat.getDrawable(context, R.drawable.customborder) as Drawable
    }

    private fun startNewLoyaltyCardActivity(loyaltyCard : LoyaltyCard) {
        val intent = Intent(context, NewLoyaltyCardActivity::class.java)
        intent.putExtra("card", Gson().toJson(loyaltyCard))
        intent.putExtra("mode", ViewMode.Edit.toString())
        context.startActivity(intent)
    }

    private fun displayConfirmationDialog(loyaltyCard : LoyaltyCard) {

        val alertDialog = AlertDialog.Builder(context)
                .setTitle(loyaltyCard.cardName)
                .setMessage("Do you want to delete this card?")
                .setPositiveButton("Yes", { _, _ ->
                    run{
                        Toast.makeText(context, "Card \"" + loyaltyCard.cardName + "\" deleted.", Toast.LENGTH_SHORT).show()
                        FirebaseDb().deleteObject("loyaltycards/", loyaltyCard.key)
                    }
                })
                .setNegativeButton("No", { _, _ -> }).create()
        alertDialog.show()
    }
}