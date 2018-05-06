package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction

import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.Exclude
import java.util.*

open class Transaction {
    @Exclude
    var key : String? = null
    lateinit var type : TransactionType
    var price : Double = Double.NaN
    lateinit var category: Category
    lateinit var description: String
    lateinit var datetime: Date

    var longitude : Double? = null
    var latitude : Double? = null

    //lateinit var priceCurrency : Currency
    var priceCurrency : String = "CZK"

    constructor()

    constructor(type: TransactionType, price: Double, category: Category, description: String, datetime: Date, position: LatLng?, priceCurrency: String) {
        this.type = type
        this.price = price
        this.category = category
        this.description = description
        this.datetime = datetime
        this.longitude = position?.longitude
        this.latitude = position?.latitude
        this.priceCurrency = priceCurrency
    }
}

enum class TransactionType {
    INCOME, EXPENDITURE, TRANSFER
}