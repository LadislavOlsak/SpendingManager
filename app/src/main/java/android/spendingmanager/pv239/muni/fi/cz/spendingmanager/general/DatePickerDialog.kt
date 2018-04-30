package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.general

import android.app.Activity
import android.app.DatePickerDialog
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import java.util.*

class DatePickerDialog(
        private var activity : Activity)
    : DatePickerDialog(activity) {

    fun create(listener : DatePickerDialog.OnDateSetListener) : DatePickerDialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(activity, R.style.FullScreenDialog, listener, year, month, day)
    }
}