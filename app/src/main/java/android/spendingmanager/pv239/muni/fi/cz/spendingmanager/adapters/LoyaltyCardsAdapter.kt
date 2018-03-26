package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.adapters

import android.content.Context
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.entities.LoyaltyCard
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridLayout
import android.widget.TextView
import android.widget.GridView



/**
 * Created by Stefan on 26-Mar-18.
 */
class LoyaltyCardsAdapter(
        private val context: Context,
        private var loyaltyCards : List<LoyaltyCard>
) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val loyaltyCard = getItem(position)

        val tv: TextView
        if (convertView == null) {
            tv = TextView(context)
            tv.textSize = 25f
            //tv.layoutParams = GridLayout.LayoutParams(85, 85)
        } else {
            tv = convertView as TextView
        }

        tv.text = "${loyaltyCard.cardName} ${loyaltyCard.cardNumber}"
        return tv
    }

    override fun getItem(position: Int): LoyaltyCard = loyaltyCards[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = loyaltyCards.size
}