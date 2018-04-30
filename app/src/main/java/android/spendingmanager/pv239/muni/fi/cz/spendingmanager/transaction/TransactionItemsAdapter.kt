package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction

import android.app.Activity
import android.content.Intent
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.general.DatePickerDialog
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.location.LocationActivity
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatSpinner
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.ArrayAdapter
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*


class TransactionItemsAdapter(
        private val fragment : Fragment,
        var items : List<Transaction>
) : BaseAdapter(), android.app.DatePickerDialog.OnDateSetListener {

    private val LOCATION_ACTIVITY_REQUEST = 13

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        val c = Calendar.getInstance()
        c.set(p1, p2, p3)
        dateEt?.setText(SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(c.time))
        notifyDataSetChanged()
    }

    private var dateEt : EditText? = null

    fun updateData(items : List<Transaction>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val transactionItem = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(fragment.context).inflate(R.layout.transaction_item, parent, false)
        }

        val categorySpinner = view?.findViewById<View>(R.id.transaction_item_category) as AppCompatSpinner
        //val weeksSpiner = view.findViewById<View>(R.id.weeksSpinner) as Spinner

        val categoryAdapter: ArrayAdapter<String>
        var mTestArray = (parent?.context?.resources?.getStringArray(R.array.default_categories)?.toMutableList())
        if (mTestArray == null)
        {
            mTestArray = mutableListOf()
        }
        FirebaseDb.getUserReference("categories")?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val category = postSnapshot.getValue<Category>(Category::class.java)
                    category?.key = postSnapshot.key
                    if (category != null && category.id != "")
                    {
                        mTestArray.add(category.categoryName)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        categoryAdapter = ArrayAdapter<String>(
                parent?.context,
                android.R.layout.simple_spinner_dropdown_item,
                mTestArray)
        categorySpinner.adapter = categoryAdapter

        val priceEditText = view.findViewById<View>(R.id.transaction_item_price) as EditText
        priceEditText.setText(transactionItem.price.toString())

        val removeTransactionItem = view.findViewById<View>(R.id.transaction_item_delete_iv) as ImageView
        removeTransactionItem.visibility = View.GONE
        //removeTransactionItem.setOnClickListener { removeItem(position) }

        dateEt = view.findViewById(R.id.transaction_item_date) as EditText
        dateEt?.setText(SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Calendar.getInstance().time))

        val dateBtn = view.findViewById<View>(R.id.transaction_item_date_btn) as Button
        dateBtn.setOnClickListener { DatePickerDialog(fragment.activity as Activity).create(this).show() }

        val mapBtn = view.findViewById(R.id.transaction_item_map_btn) as Button
        mapBtn.setOnClickListener {
            fragment.startActivityForResult(Intent(fragment.context, LocationActivity::class.java), LOCATION_ACTIVITY_REQUEST)
        }

        return view
    }

    override fun getItem(position: Int): Transaction = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = items.size

    private fun removeItem(position : Int) {
        val list = (items as MutableList<Transaction>)
        list.removeAt(position)
        updateData(list)
    }
}