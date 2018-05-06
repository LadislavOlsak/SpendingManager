package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.limits

import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.planning.TransactionFrequency

class CategoryLimit(
        var categoryName : String,
        var limitAmount : Double,
        var limitAmountCurrency : String,
        var isActive : Boolean = false,
        var frequency : TransactionFrequency
) {

    var key :String = ""

    constructor() : this("", 0.0, "CZK", false, TransactionFrequency.Monthly)
}