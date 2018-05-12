package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories

import android.content.Context
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CategoriesAdapter(
        private val context : Context,
        private var categories : List<Category>
) : BaseAdapter() {

    fun update(list : List<Category>) {
        categories = list
        notifyDataSetChanged()
    }

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