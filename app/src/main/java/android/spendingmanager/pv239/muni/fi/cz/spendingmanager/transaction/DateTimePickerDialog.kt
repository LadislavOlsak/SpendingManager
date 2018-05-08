package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction

import android.app.Activity
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog

class DateTimePickerDialog {

    fun create(context : Activity) : SingleDateAndTimePickerDialog {
        return SingleDateAndTimePickerDialog.Builder(context)
                .title("Simple")
                .build()
    }
}