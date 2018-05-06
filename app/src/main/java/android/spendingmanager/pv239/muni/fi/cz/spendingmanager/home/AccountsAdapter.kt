package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.home

import android.content.Context
import android.content.Intent
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.account.AccountActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.loyaltycards.LoyaltyCard
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson

class AccountsAdapter(
        val context : Context,
        val accounts : List<Account>
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val account = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.account_item, parent, false)
        }

        val accountNameTv = view?.findViewById(R.id.main_account_name) as TextView
        accountNameTv.text = account.accountName

        val accountAmountTv = view.findViewById(R.id.main_account_amount_and_curr) as TextView
        accountAmountTv.text = "${account.amount.toString()} ${account.currency.currencyCode}"

        //val accountWrapper = view.findViewById(R.id.main_account_wrapper) as LinearLayout

        val accountIntent = Intent(context, AccountActivity::class.java)
        accountIntent.putExtra("account", Gson().toJson(account))
        //accountWrapper.setOnClickListener { context.startActivity(accountIntent) }

        return view
    }

    override fun getItem(position: Int): Account = accounts[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = accounts.size

}