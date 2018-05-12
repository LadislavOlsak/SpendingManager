package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.limits

import android.content.Context
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.support.v7.widget.SwitchCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class CategoryLimitsAdapter (
        private val context : Context,
        var categoryLimits : List<CategoryLimit>
) : BaseAdapter()  {

    fun update(limits : List<CategoryLimit>) {
        categoryLimits = limits
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val categoryLimit = getItem(position)

        var view = convertView

        val viewHolder : ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.limits_settings_item, parent, false)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.limit = categoryLimit

        val limitValueText = if(categoryLimit.limitAmount == 0.0) "" else categoryLimit.limitAmount.toString()
        viewHolder.limitEt.setText(limitValueText)
        viewHolder.limitEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if(p0.isNullOrEmpty() || p0?.toString()?.toDoubleOrNull() == null) {
                    return
                }
                viewHolder.limit?.limitAmount = p0.toString().toDouble()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })

        viewHolder.enabledSwitch.isChecked = categoryLimit.isActive == true
        viewHolder.enabledSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewHolder.limit?.isActive = isChecked
        }

        viewHolder.categoryNameTv.text = categoryLimit.categoryName

        return view
    }

    override fun getItem(position: Int): CategoryLimit = categoryLimits[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = categoryLimits.size

    class ViewHolder(row : View) {
        var limit : CategoryLimit? = null
        var limitEt: EditText = row.findViewById(R.id.limits_item_value)
        var enabledSwitch: SwitchCompat = row.findViewById(R.id.limits_item_switch)
        val categoryNameTv : TextView = row.findViewById(R.id.limits_item_category_name)
    }
}