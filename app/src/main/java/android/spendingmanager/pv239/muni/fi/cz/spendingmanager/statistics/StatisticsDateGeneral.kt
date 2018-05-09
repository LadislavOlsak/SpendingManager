package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.graphics.Color
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Gravity
import android.graphics.Typeface
import android.inputmethodservice.Keyboard
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.DefaultCategories
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.util.TypedValue
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.home.MainActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*


class StatisticsDateGeneral : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.statistics_date_general, container, false)

        val weeksSpiner = view.findViewById<View>(R.id.weeksSpinner) as Spinner
        val items = arrayOf("1", "2", "3", "4 (month)", "52 (year)")
        val weeksSpinerAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, items)
        weeksSpiner.adapter = weeksSpinerAdapter
        val spinnerPosition = weeksSpinerAdapter.getPosition("4 (month)")
        weeksSpiner.setSelection(spinnerPosition)

        val categories : MutableList<String> = mutableListOf<String>()
        val categoriesList : MutableList<Category> = mutableListOf<Category>()
        var transactions : MutableList<Transaction> = mutableListOf()

        val categoriesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Category>()
                snapshot.children.mapNotNullTo(list) {
                    val category = it.getValue<Category>(Category::class.java)
                    category?.key = it.key
                    category
                }
                categories.clear()
                list.forEach { x -> categoriesList.add(x) }
                DefaultCategories.getDefaultCategories().forEach { x -> categoriesList.add(x) }

                categoriesList.forEachIndexed { index, category ->
                    categories.add(category.categoryName)
                }


                val transactionsListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.children.mapNotNullTo(transactions) {
                            val transaction = it.getValue<Transaction>(Transaction::class.java)
                            transaction?.key = it.key
                            transaction
                        }

                        var btnWeekSpinner = view.findViewById<View>(R.id.btnWeeksSpinner) as Button
                        btnWeekSpinner.setOnClickListener {
                            GenerateData(view, weeksSpiner, categories, transactions)
                        }
                        GenerateData(view, weeksSpiner, categories, transactions)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        println("loadPost:onCancelled ${databaseError.toException()}")
                    }
                }

                FirebaseDb.getUserReference("transactions")?.addValueEventListener(transactionsListener)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        FirebaseDb.getUserReference("categories")?.addValueEventListener(categoriesListener)


        return view
    }

    private fun GenerateData(view: View, weeksSpiner: Spinner, categoriesList: List<String>, transactions : MutableList<Transaction>)
    {
        val categoriesListIterator = categoriesList.iterator()

        var tableDatesOverview = view.findViewById<View>(R.id.tableDatesOverview) as TableLayout
        tableDatesOverview.removeAllViews()
        val row = TableRow(activity)
        val txt1 = EditText(activity)
        txt1.setText("Category")
        val txt2 = EditText(activity)
        txt2.setText("Value")
        val txt3 = EditText(activity)
        txt3.setText("+/- *")
        row.addView(txt1)
        row.addView(txt2)
        row.addView(txt3)
        row.setBackgroundColor(Color.rgb(220,220,220))
        tableDatesOverview.addView(row)

        var i: Int = 1
        while (categoriesListIterator.hasNext()) {
            val category = categoriesListIterator.next()
            val row = TableRow(activity)
            if (i % 2 == 0) {
                row.setBackgroundColor(Color.rgb(220,220,220))
            } else {
                row.setBackgroundColor(Color.rgb(180,180,180))
            }

            val txt1 = EditText(activity)
            txt1.setText(category)
            row.addView(txt1)

            val currentDate = GregorianCalendar.getInstance()
            val onlyNumbers = Regex("[^0-9]")
            val weeksCount : Int = onlyNumbers.replace(weeksSpiner.selectedItem.toString(), "").toInt()
            val value = StatisticsHelper().CalculateValueTransactions(category, transactions, weeksCount, currentDate)
            val txt2 = EditText(activity)
            txt2.setText(value.toString())
            row.addView(txt2)

            // TODO: Vypočítat minulé období
            val lastSeasonDate = StatisticsHelper().CalculateStartDate(weeksCount, currentDate, 1)
            val lastSeasonValue = StatisticsHelper().CalculateValueTransactions(category, transactions, weeksCount, lastSeasonDate)
            var ratio = 0.0
            if (lastSeasonValue != 0.0)
            {
                ratio = ((value/lastSeasonValue.toDouble()) * 100)
            }
            else if (value != 0.0)
            {
                ratio = 999.0
            }

            val txt3 = EditText(activity)
            txt3.setText(ratio.toInt().toString() + " %")
            if (ratio > 100)
            {
                txt3.setTextColor(Color.rgb(121,0,36))
            }
            else
            {
                txt3.setTextColor(Color.rgb(0,115,46))
            }
            row.addView(txt3)

            tableDatesOverview.addView(row)
            i++
        }

        var tableDatesNote = view.findViewById<View>(R.id.tableDatesNote) as TextView

        val onlyNumbers = Regex("[^0-9]")
        val weeksCount : Int = onlyNumbers.replace(weeksSpiner.selectedItem.toString(), "").toInt()
        tableDatesNote.text = "* Comparison to previous " + weeksCount + " weeks"
    }
}

