package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.planning

import android.app.Activity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class PlannedTransactionAdapter(
        val context : Activity,
        var plannedTransactions : List<PlannedTransaction>
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val plannedTransaction = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.planned_transaction_item, parent, false)
        }

        val categoryTv = view?.findViewById(R.id.planned_transaction_item_category_name_tv) as TextView
        categoryTv.text = plannedTransaction.category.categoryName

        val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val dateTv = view.findViewById(R.id.planned_transaction_item_date_tv) as TextView
        dateTv.text = dateFormatter.format(plannedTransaction.datetime.time)

        val noteTv = view.findViewById(R.id.planned_transaction_item_note_tv) as TextView
        noteTv.text = plannedTransaction.description

        val priceTv = view.findViewById(R.id.planned_transaction_item_price_tv) as TextView
        priceTv.text = "${plannedTransaction.price} CZK"

        val frequencyTv = view.findViewById(R.id.planned_transaction_item_frequency_tv) as TextView
        frequencyTv.text = plannedTransaction.frequency.toString()

        return view
    }

    override fun getItem(position: Int): PlannedTransaction = plannedTransactions[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = plannedTransactions.size
}