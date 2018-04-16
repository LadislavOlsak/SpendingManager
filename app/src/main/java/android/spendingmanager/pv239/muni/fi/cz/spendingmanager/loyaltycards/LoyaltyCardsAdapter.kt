package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.loyaltycards

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat


/**
 * Created by Stefan on 26-Mar-18.
 */
class LoyaltyCardsAdapter(
        private val context: Context,
        private var loyaltyCards : List<LoyaltyCard>
) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val loyaltyCard = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.loyalty_card_item, parent, false)
        }

        val cardNameTv = view?.findViewById(R.id.loyalty_card_item_name_tv) as TextView
        cardNameTv.text = loyaltyCard.cardName

        val cardEanIv = view.findViewById(R.id.loyalty_card_item_barcode_iv) as ImageView
        cardEanIv.setImageBitmap(getBarcodeBitmap(loyaltyCard.cardNumber))

        val cardTileLayout = view.findViewById(R.id.loyalty_card_item_tile_layout) as LinearLayout

        cardTileLayout.background = getColoredBackground(loyaltyCard.color)

        //cardEanIv.setOnClickListener { fullScreenDisplay() }

        return view
    }

    override fun getItem(position: Int): LoyaltyCard = loyaltyCards[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = loyaltyCards.size

    private fun getBarcodeBitmap(cardNumber : String) : Bitmap? {
        try {
            val bitMatrix = MultiFormatWriter().encode(cardNumber, BarcodeFormat.CODE_128,600,200) as BitMatrix
            return BarcodeEncoder().createBitmap(bitMatrix)
        } catch (e : WriterException) {
            e.printStackTrace()
        }
        return null
    }

    private fun getColoredBackground(color : Int) : Drawable {
        val border = getBorderDrawable()
        border.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
        return border
    }

    private fun getBorderDrawable(): Drawable {
        return ContextCompat.getDrawable(context, R.drawable.customborder)
    }
}