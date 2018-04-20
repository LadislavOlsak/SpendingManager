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
import android.widget.Spinner
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import java.util.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData


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

        var btnWeekSpinner = view.findViewById<View>(R.id.btnWeeksSpinner) as Button
        btnWeekSpinner.setOnClickListener {
            //Generate Graph
        }

        //Generate Graph
        val chart = view.findViewById<View>(R.id.chart) as LineChart

        val xVals : MutableList<Int> = mutableListOf<Int>()
        for (i in 0..(weeksSpiner.getSelectedItem().toString().toInt() - 1)) {
            xVals.add(i + 1)
        }


        // Graph Data
        val dataSets : MutableList<LineDataSet> = mutableListOf<LineDataSet>()

        val currentDate = GregorianCalendar.getInstance()
        var categories : List<Category> = StatisticsHelper().GetCategories()
        val categoriesListIterator = categories.iterator()
        while (categoriesListIterator.hasNext()) {
            val category = categoriesListIterator.next()

            val yVals : MutableList<Entry> = mutableListOf<Entry>()

            var categoryValues: List<Int>
            for (i in 0..(weeksSpiner.getSelectedItem().toString().toInt() - 1)) {

                var date : GregorianCalendar = currentDate.clone() as GregorianCalendar // nutná kopie, jinak se referenčně  pořád čas posouvá
                val noOfDays = (7 * i)*(-1)
                date.add(Calendar.DAY_OF_YEAR, noOfDays)

                yVals.add(Entry(i.toFloat(), StatisticsHelper().CalculateValueTransactions(category, 1, date).toFloat()))
            }

            val set: LineDataSet
            set = LineDataSet(yVals, category.categoryName)
            set.setFillAlpha(110)

            set.setColor(Color.BLACK)
            set.setCircleColor(Color.BLACK)
            set.setLineWidth(1f)
            set.setCircleRadius(3f)
            set.setDrawCircleHole(false)
            set.setValueTextSize(9f)
            set.setDrawFilled(true)

            dataSets.add(set)
        }

        val lineData = LineData(dataSets.toList())

        chart.setData(lineData)

        return view
    }

}