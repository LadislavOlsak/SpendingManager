package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction

import android.app.Activity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText

class TransactionItemsAdapter(
        private val fragment : Fragment,
        var items : List<TransactionItem>
) : BaseAdapter() {

    fun updateData(items : List<TransactionItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val tramsactionItem = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(fragment.context).inflate(R.layout.transaction_item, parent, false)
        }

        val priceEditText = view?.findViewById(R.id.transaction_item_price) as EditText
        priceEditText.setText(tramsactionItem.price.toString())

        return view
    }

    override fun getItem(position: Int): TransactionItem = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = items.size
}