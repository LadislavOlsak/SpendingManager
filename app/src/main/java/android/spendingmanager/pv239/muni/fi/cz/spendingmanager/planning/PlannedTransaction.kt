package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.planning

import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.CategoryType
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.TransactionType
import com.google.android.gms.maps.model.LatLng
import java.util.*

class PlannedTransaction : Transaction {

    var frequency : TransactionFrequency? = null

    constructor()

    constructor(
            frequency : TransactionFrequency
    ) : super(TransactionType.EXPENDITURE, 0.0, Category("id", "Lunch", CategoryType.DEFAULT), "",
            Calendar.getInstance().time,
            null, "CZK") {
        this.frequency = frequency
    }
}