package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.limits

import java.math.BigDecimal;
import java.util.*

class CategoryLimit(
        var categoryName : String,
        var notificationEnabled : Boolean,
        var limitAmount : BigDecimal,
        var limitAmountCurrency : Currency
) {
}