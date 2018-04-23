package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction

import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import com.google.android.gms.maps.model.LatLng
import java.util.GregorianCalendar;

open class Transaction (
        var type : TransactionType,
        var amount : Int,
        var category: Category,
        var description: String,
        var datetime: GregorianCalendar,
        var position: LatLng
) {
}

enum class TransactionType {
    INCOME, EXPENDITURE
}