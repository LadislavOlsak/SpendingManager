package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction

import java.math.BigDecimal
import java.util.*

class TransactionItem(
        var category : String,
        var price : BigDecimal,
        var priceCurrency : Currency
) {
}