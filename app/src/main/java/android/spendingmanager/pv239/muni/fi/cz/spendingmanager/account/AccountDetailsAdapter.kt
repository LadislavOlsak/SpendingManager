package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.account

import android.app.Activity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class AccountDetailsAdapter(
        val context : Activity,
        val details : List<AccountDetail>
) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val detail = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val detailName = view?.findViewById(R.id.account_detail_item_name_tv) as TextView
        detailName.text = detail.name

        val detailValue = view.findViewById(R.id.account_detail_item_value_tv) as TextView
        detailValue.text = detail.value

        return view
    }

    override fun getItem(position: Int): AccountDetail = details[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = details.size
}