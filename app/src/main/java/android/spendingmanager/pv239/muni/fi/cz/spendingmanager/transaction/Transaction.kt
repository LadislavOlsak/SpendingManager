package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction

import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.Exclude
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

open class Transaction : Serializable{
    @Exclude
    var key : String? = null

    @SerializedName("TransactionType")
    lateinit var type : TransactionType

    @SerializedName("price")
    var price : Double = Double.NaN

    @SerializedName("category")
    lateinit var category: Category

    @SerializedName("description")
    lateinit var description: String

    @SerializedName("datetime")
    lateinit var datetime: Date

    @SerializedName("longitude")
    var longitude : Double? = null

    @SerializedName("latitude")
    var latitude : Double? = null

    //lateinit var priceCurrency : Currency
    @SerializedName("priceCurrency")
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