package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.home

import android.animation.ArgbEvaluator
import android.app.Activity
import android.graphics.Color
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import az.plainpie.PieView
import com.github.mikephil.charting.charts.PieChart
import az.plainpie.animation.PieAngleAnimation
import android.graphics.Color.colorToHSV




class AccountStatsAdapter(
        var context : Activity,
        var list : List<AccountStats>
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val stats = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.stats_item_piechart, parent, false)
        }

        val chart = view?.findViewById(R.id.chart_pieView) as PieView
        chart.percentage = stats.percentage
        chart.setPercentageBackgroundColor(getColor(chart.percentage))
        val animation = PieAngleAnimation(chart)
        animation.duration = 2000
        chart.startAnimation(animation)

        val textTv = view.findViewById(R.id.chart_text_tv) as TextView
        textTv.text = stats.text

        return view
    }

    override fun getItem(p0: Int): AccountStats = list[p0]
    override fun getItemId(p0: Int): Long = p0.toLong()
    override fun getCount(): Int = list.size

    private fun getColor(percentage : Float) : Int {
        return when (percentage) {
            in 0.0..25.0 -> ContextCompat.getColor(context, R.color.pie_green)
            in 25.0..50.0 -> ContextCompat.getColor(context, R.color.pie_yellow)
            in 50.0..75.0 -> ContextCompat.getColor(context, R.color.pie_orange)
            else -> ContextCompat.getColor(context, R.color.pie_red)
        }
    }
}