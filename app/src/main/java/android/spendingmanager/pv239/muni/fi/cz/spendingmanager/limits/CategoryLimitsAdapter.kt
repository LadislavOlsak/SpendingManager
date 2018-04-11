package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.limits

import android.content.Context
import android.content.Intent
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.account.AccountActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.home.Account
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.SwitchCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import java.util.*

class CategoryLimitsAdapter (
        private val context : Context,
        private val categoryLimits : List<CategoryLimit>
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val categoryLimit = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.limits_settings_item, parent, false)
        }

        val categoryNameTv = view?.findViewById(R.id.limits_item_category_name) as TextView
        categoryNameTv.text = categoryLimit.categoryName

        val categoryEnabledSw = view.findViewById(R.id.limits_item_switch) as SwitchCompat
        categoryEnabledSw.isChecked = categoryLimit.notificationEnabled

        val categoryLimitEt = view.findViewById(R.id.limits_item_value) as EditText
        categoryLimitEt.setText(String.format(Locale.getDefault(), categoryLimit.limitAmount.toString()))

        val categoryLimitCurrencySp = view.findViewById(R.id.limits_item_currency) as AppCompatSpinner
        //categoryLimitCurrencySp.tag = categoryLimit.limitAmountCurrency

        return view
    }

    override fun getItem(position: Int): CategoryLimit = categoryLimits[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = categoryLimits.size
}