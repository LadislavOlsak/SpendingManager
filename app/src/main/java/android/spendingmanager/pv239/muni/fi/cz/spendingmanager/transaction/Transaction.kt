package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction

import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.CategoryType
import com.google.android.gms.maps.model.LatLng
import java.math.BigDecimal
import java.util.*

open class Transaction {
    lateinit var type : TransactionType
    var price : Int = 0
    lateinit var category: Category
    lateinit var description: String
    lateinit var datetime: GregorianCalendar
    var position: LatLng? = null
    lateinit var priceCurrency : Currency

    constructor()

    constructor(type: TransactionType, price: Int, category: Category, description: String, datetime: GregorianCalendar, position: LatLng?, priceCurrency: Currency) {
        this.type = type
        this.price = price
        this.category = category
        this.description = description
        this.datetime = datetime
        this.position = position
        this.priceCurrency = priceCurrency
    }
}

enum class TransactionType {
    INCOME, EXPENDITURE, TRANSFER
}