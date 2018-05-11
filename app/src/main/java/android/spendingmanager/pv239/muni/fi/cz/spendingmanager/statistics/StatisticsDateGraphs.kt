package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.DefaultCategories
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import java.util.*
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.components.XAxis
import java.io.Serializable


class StatisticsDateGraphs : Fragment() {

    companion object {

        fun newInstance(transactions: Serializable, categories: Serializable): StatisticsDateGraphs {

            val args = Bundle()
            args.putSerializable("transactions", transactions)
            args.putSerializable("categories", categories)
            val fragment = StatisticsDateGraphs()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.statistics_date_graphs, container, false)

        val transactions = arguments?.getSerializable("transactions") as List<Transaction>
        val categoriesList = arguments?.getSerializable("categories") as List<Category>

        val weeksSpiner = view.findViewById<View>(R.id.weeksSpinner) as Spinner
        val items = arrayOf("1", "2", "3", "4 (month)", "52 (year)")
        val weeksSpinerAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, items)
        weeksSpiner.adapter = weeksSpinerAdapter
        val spinnerPosition = weeksSpinerAdapter.getPosition("4 (month)")
        weeksSpiner.setSelection(spinnerPosition)

        val categoryListView = view.findViewById<View>(R.id.categories_list) as ListView
        val categories : MutableList<String> = mutableListOf<String>()

        categoriesList.forEachIndexed { index, category ->
            categories.add(category.categoryName)
        }
        val categoriesAdapter = ArrayAdapter<String>(activity, R.layout.statistics_date_graphs_catlist, categories)
        categoryListView.adapter = categoriesAdapter
        categoryListView.itemsCanFocus = false
        categoryListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        categoriesList.forEachIndexed { index, category ->
            categoryListView.setItemChecked(index,true)
        }
        // Set categoryListView height (not working automatically)
        val params = categoryListView.layoutParams
        params.height = 120 * categoriesList.count()
        categoryListView.layoutParams = params
        categoryListView.requestLayout()

        var btnWeekSpinner = view.findViewById<View>(R.id.btnWeeksSpinner) as Button
        btnWeekSpinner.setOnClickListener {
            GenerateGraphs(view, weeksSpiner, categoryListView, categories, transactions)
        }
        GenerateGraphs(view, weeksSpiner, categoryListView, categories, transactions)


        return view
    }

    private fun GenerateGraphs(view: View, weeksSpiner: Spinner, categoryListView : ListView, categories: List<String>, transactions : List<Transaction>)
    {
        val onlyNumbers = Regex("[^0-9]")
        val weeksCount : Int = onlyNumbers.replace(weeksSpiner.selectedItem.toString(), "").toInt()
        val weeksIterator : Int = (weeksCount - 1)

        val chart = view.findViewById<View>(R.id.chart) as LineChart
        chart.removeAllViews()

        val xVals : MutableList<String> = mutableListOf<String>()
        for (i in 0..weeksIterator) {
            val iterator : Int = i + 1
            xVals.add(iterator.toString())
        }

        var colorsList : List<Int> = StatisticsHelper().getColors()

        // Graph Data
        val dataSets : MutableList<LineDataSet> = mutableListOf<LineDataSet>()

        val currentDate = GregorianCalendar.getInstance()
        categories.forEachIndexed { index, category ->
            if (categoryListView.isItemChecked(index))
            {
                val yVals : MutableList<Entry> = mutableListOf<Entry>()

                var categoryValues: List<Int>
                for (i in weeksIterator downTo 0) {

                    var date : GregorianCalendar = currentDate.clone() as GregorianCalendar // nutná kopie, jinak se referenčně  pořád čas posouvá
                    val noOfDays = (7 * i)*(-1)
                    date.add(Calendar.DAY_OF_YEAR, noOfDays)

                    yVals.add(Entry(weeksIterator - i.toFloat(), StatisticsHelper().CalculateValueTransactions(category, transactions, 1, date).toFloat()))
                }

                val set: LineDataSet
                set = LineDataSet(yVals, category)
                set.fillAlpha = 110

                set.color = colorsList[index % colorsList.count()]
                set.setCircleColor(colorsList[index % colorsList.count()])
                set.lineWidth = 1f
                set.circleRadius = 3f
                set.setDrawCircleHole(false)
                set.valueTextSize = 9f
                set.setDrawFilled(false)

                dataSets.add(set)
            }
        }

        val lineData = LineData(dataSets.toList())

        chart.data = lineData
        chart.description.isEnabled = false

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setLabelCount(xVals.count(), true)
        xAxis.valueFormatter = (IAxisValueFormatter { value, _ ->
            if (value.toInt() < xVals.count())
            {
                xVals[value.toInt()]
            }
            else
            {
                ""
            }
        })
    }

}