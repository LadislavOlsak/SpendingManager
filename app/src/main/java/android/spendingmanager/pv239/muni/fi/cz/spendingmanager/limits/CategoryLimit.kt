package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.limits

import java.math.BigDecimal;
import java.util.*

class CategoryLimit {
    lateinit var categoryName: String
    var notificationEnabled : Boolean = false
    var limitAmount : Int = 0
    //lateinit var limitAmountCurrency : Currency

    var key : String? = null

    constructor()

    constructor(categoryName: String, notificationEnabled: Boolean, limitAmount: Int) {
        this.categoryName = categoryName
        this.notificationEnabled = notificationEnabled
        this.limitAmount = limitAmount
        //this.limitAmountCurrency = limitAmountCurrency
    }
}