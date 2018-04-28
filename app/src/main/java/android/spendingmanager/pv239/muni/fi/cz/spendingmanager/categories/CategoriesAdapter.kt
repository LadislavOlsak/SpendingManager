package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories

import android.content.Context
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.limits.CategoryLimit
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.SwitchCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import java.util.*

class CategoriesAdapter(
        private val context : Context,
        private val categories : List<Category>
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        val category = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false)
        }

        val categoryName = view?.findViewById<View>(R.id.category_item_text) as TextView
        categoryName.text = category.categoryName

        return view
    }

    override fun getItem(position: Int): Category = categories[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = categories.size
}