package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction

import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import com.google.android.gms.maps.model.LatLng
import java.math.BigDecimal
import java.util.*

open class Transaction (
        var type : TransactionType,
        var price : Int,
        var category: Category,
        var description: String,
        var datetime: GregorianCalendar,
        var position: LatLng,
        var priceCurrency : Currency
        ) {
}

enum class TransactionType {
    INCOME, EXPENDITURE
}