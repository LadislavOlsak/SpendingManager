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
import android.util.TypedValue
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.home.MainActivity
import android.widget.*
import java.util.*


class StatisticsDateGeneral : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.statistics_date_general, container, false)

        val weeksSpiner = view.findViewById<View>(R.id.weeksSpinner) as Spinner
        val items = arrayOf("1", "2", "3", "4", "5", "6","7", "8", "9", "10", "20", "52")
        val weeksSpinerAdapter = ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items)
        weeksSpiner.setAdapter(weeksSpinerAdapter)
        val spinnerPosition = weeksSpinerAdapter.getPosition("4")
        weeksSpiner.setSelection(spinnerPosition)

        //val txtTest = view.findViewById<View>(R.id.buttonTest) as TextView
        var btnWeekSpinner = view.findViewById<View>(R.id.btnWeeksSpinner) as Button
        btnWeekSpinner.setOnClickListener {
            //txtTest.setText(weeksSpiner.getSelectedItem().toString())
            // místo toho přepočítat hodnoty categorie - hodnoty za časové období
        }

        val categoriesList =  StatisticsHelper().GetCategories()
        val categoriesListIterator = categoriesList.iterator()

        var tableDatesOverview = view.findViewById<View>(R.id.tableDatesOverview) as TableLayout
        tableDatesOverview.isClickable = false
        val row = TableRow(getActivity())
        val txt1 = EditText(getActivity())
        txt1.setText("Category")
        val txt2 = EditText(getActivity())
        txt2.setText("Value")
        val txt3 = EditText(getActivity())
        txt3.setText("+/-")
        row.addView(txt1)
        row.addView(txt2)
        row.addView(txt3)
        row.setBackgroundColor(Color.rgb(220,220,220))
        tableDatesOverview.addView(row)

        var i: Int = 1
        while (categoriesListIterator.hasNext()) {
            val category = categoriesListIterator.next()
            val row = TableRow(getActivity())
            if (i % 2 === 0) {
                row.setBackgroundColor(Color.rgb(220,220,220))
            } else {
                row.setBackgroundColor(Color.rgb(180,180,180))
            }

            val txt1 = EditText(getActivity())
            txt1.setText(category.categoryName)
            row.addView(txt1)

            val value = StatisticsHelper().CalculateValueTransactions(category, weeksSpiner.getSelectedItem().toString().toInt(), GregorianCalendar.getInstance())
            val txt2 = EditText(getActivity())
            txt2.setText(value.toString())
            row.addView(txt2)

            // TODO: Vypočítat minulé období
            val txt3 = EditText(getActivity())
            txt3.setText("0")
            row.addView(txt3)

            tableDatesOverview.addView(row)
            i++
        }

        return view
    }

}