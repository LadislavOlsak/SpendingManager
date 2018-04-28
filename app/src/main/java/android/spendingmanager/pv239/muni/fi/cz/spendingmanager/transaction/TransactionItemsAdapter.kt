package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.CategoriesAdapter
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatSpinner
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.ArrayAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class TransactionItemsAdapter(
        private val fragment : Fragment,
        var items : List<TransactionItem>
) : BaseAdapter() {

    fun updateData(items : List<TransactionItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val transactionItem = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(fragment.context).inflate(R.layout.transaction_item, parent, false)
        }

        val categorySpinner = view?.findViewById(R.id.transaction_item_category) as AppCompatSpinner
        //val weeksSpiner = view.findViewById<View>(R.id.weeksSpinner) as Spinner

        val categoryAdapter: ArrayAdapter<String>
        var mTestArray = (parent?.getContext()?.getResources()?.getStringArray(R.array.default_categories)?.toMutableList())
        if (mTestArray == null)
        {
            mTestArray = mutableListOf()
        }
        FirebaseDb.getUserReference("categories")?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val category = postSnapshot.getValue<Category>(Category::class.java)
                    category?.dbKey = postSnapshot.key
                    if (category != null && category.id != "")
                    {
                        mTestArray.add(category.categoryName)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        categoryAdapter = ArrayAdapter<String>(
                parent?.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                mTestArray)
        categorySpinner.setAdapter(categoryAdapter)

        val priceEditText = view?.findViewById(R.id.transaction_item_price) as EditText
        priceEditText.setText(transactionItem.price.toString())

        val removeTransactionItem = view.findViewById(R.id.transaction_item_delete_iv) as ImageView
        removeTransactionItem.setOnClickListener { removeItem(position) }

        return view
    }

    override fun getItem(position: Int): TransactionItem = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = items.size

    private fun removeItem(position : Int) {
        val list = (items as MutableList<TransactionItem>)
        list.removeAt(position)
        updateData(list)
    }
}