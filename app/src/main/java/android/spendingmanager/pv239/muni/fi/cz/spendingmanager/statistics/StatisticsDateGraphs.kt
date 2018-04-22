package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.graphics.Color
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.components.XAxis


class StatisticsDateGraphs : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.statistics_date_graphs, container, false)

        val weeksSpiner = view.findViewById<View>(R.id.weeksSpinner) as Spinner
        val items = arrayOf("1", "2", "3", "4", "5", "6","7", "8", "9", "10", "20", "52")
        val weeksSpinerAdapter = ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items)
        weeksSpiner.setAdapter(weeksSpinerAdapter)
        val spinnerPosition = weeksSpinerAdapter.getPosition("4")
        weeksSpiner.setSelection(spinnerPosition)

        val categoryListView = view.findViewById<View>(R.id.categories_list) as ListView
        val categories : MutableList<String> = mutableListOf<String>()
        val categoriesList : List<Category> = StatisticsHelper().GetCategories()
        categoriesList.forEachIndexed { index, category ->
            categories.add(category.categoryName)
        }
        val categoriesAdapter = ArrayAdapter<String>(getActivity(), R.layout.statistics_date_graphs_catlist, categories)
        categoryListView.setAdapter(categoriesAdapter)
        categoryListView.setItemsCanFocus(false)
        categoryListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE)
        categoriesList.forEachIndexed { index, category ->
            categoryListView.setItemChecked(index,true)
        }
        // Set categoryListView height (not working automatically)
        val params = categoryListView.getLayoutParams()
        params.height = 120 * categoriesList.count()
        categoryListView.setLayoutParams(params)
        categoryListView.requestLayout()

        var btnWeekSpinner = view.findViewById<View>(R.id.btnWeeksSpinner) as Button
        btnWeekSpinner.setOnClickListener {
            GenerateGraphs(view, weeksSpiner, categoryListView)
        }
        GenerateGraphs(view, weeksSpiner, categoryListView)

        return view
    }

    private fun GenerateGraphs(view: View, weeksSpiner: Spinner, categoryListView : ListView)
    {
        val weeksIterator : Int = (weeksSpiner.getSelectedItem().toString().toInt() - 1)

        val chart = view.findViewById<View>(R.id.chart) as LineChart
        chart.removeAllViews()

        val xVals : MutableList<String> = mutableListOf<String>()
        for (i in 0..weeksIterator) {
            val iterator : Int = i + 1;
            xVals.add(iterator.toString())
        }

        var colorsList : List<Int> = StatisticsHelper().GetColors()

        // Graph Data
        val dataSets : MutableList<LineDataSet> = mutableListOf<LineDataSet>()

        val currentDate = GregorianCalendar.getInstance()
        val categories : List<Category> = StatisticsHelper().GetCategories()
        categories.forEachIndexed { index, category ->
            if (categoryListView.isItemChecked(index))
            {
                val yVals : MutableList<Entry> = mutableListOf<Entry>()

                var categoryValues: List<Int>
                for (i in weeksIterator downTo 0) {

                    var date : GregorianCalendar = currentDate.clone() as GregorianCalendar // nutná kopie, jinak se referenčně  pořád čas posouvá
                    val noOfDays = (7 * i)*(-1)
                    date.add(Calendar.DAY_OF_YEAR, noOfDays)

                    yVals.add(Entry(weeksIterator - i.toFloat(), StatisticsHelper().CalculateValueTransactions(category, 1, date).toFloat()))
                }

                val set: LineDataSet
                set = LineDataSet(yVals, category.categoryName)
                set.setFillAlpha(110)

                set.setColor(colorsList.get(index % colorsList.count()))
                set.setCircleColor(colorsList.get(index % colorsList.count()))
                set.setLineWidth(1f)
                set.setCircleRadius(3f)
                set.setDrawCircleHole(false)
                set.setValueTextSize(9f)
                set.setDrawFilled(false)

                dataSets.add(set)
            }
        }

        val lineData = LineData(dataSets.toList())

        chart.setData(lineData)
        chart.getDescription().setEnabled(false)

        val xAxis = chart.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setDrawGridLines(false)
        xAxis.setLabelCount(xVals.count(), true)
        xAxis.setValueFormatter((IAxisValueFormatter { value, axis ->
            if (value.toInt() < xVals.count())
            {
                xVals.get(value.toInt())
            }
            else
            {
                ""
            }
        }))
    }

}