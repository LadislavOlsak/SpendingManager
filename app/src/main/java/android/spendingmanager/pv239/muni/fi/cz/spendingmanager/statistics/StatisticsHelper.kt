package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.content.res.Resources
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.CategoryType
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.TransactionType
import java.util.*

class StatisticsHelper {


    val categories = listOf(
            Category("food", Resources.getSystem().getString(R.string.food_drinks), CategoryType.DEFAULT),
            Category("housing", Resources.getSystem().getString(R.string.housing), CategoryType.DEFAULT),
            Category("entertainment", Resources.getSystem().getString(R.string.entertainment), CategoryType.DEFAULT),
            Category("others", Resources.getSystem().getString(R.string.others), CategoryType.DEFAULT),
            Category("shopping", Resources.getSystem().getString(R.string.shopping), CategoryType.DEFAULT)
    )

    val transactions = listOf(
            Transaction(TransactionType.EXPENDITURE, 250, categories.get(1), "Some meet and fruits", GregorianCalendar(2018, 3, 12, 12, 34) ,"" ),
            Transaction(TransactionType.EXPENDITURE, 80, categories.get(1), "Some meet and fruits", GregorianCalendar(2018, 3, 13, 11, 21) ,"" ),
            Transaction(TransactionType.EXPENDITURE, 40, categories.get(1), "Bread and rolls", GregorianCalendar(2018, 3, 13, 11, 22) ,"" ),
            Transaction(TransactionType.EXPENDITURE, 850, categories.get(1), "Everything", GregorianCalendar(2018, 3, 16, 13, 50) ,"" ),
            Transaction(TransactionType.EXPENDITURE, 563, categories.get(1), "Something...", GregorianCalendar(2018, 3, 25, 12, 40) ,"" ),
            Transaction(TransactionType.EXPENDITURE, 85, categories.get(1), "Something...", GregorianCalendar(2018, 3, 28, 16, 23) ,"" ),
            Transaction(TransactionType.EXPENDITURE, 420, categories.get(1), "Something...", GregorianCalendar(2018, 4, 2, 9, 20) ,"" ),
            Transaction(TransactionType.EXPENDITURE, 236, categories.get(1), "Something...", GregorianCalendar(2018, 4, 6, 12, 20) ,"" ),
            Transaction(TransactionType.EXPENDITURE, 2500, categories.get(2), "Something...", GregorianCalendar(2018, 3, 18, 19, 20) ,"" ),
            Transaction(TransactionType.EXPENDITURE, 1500, categories.get(2), "Something...", GregorianCalendar(2018, 3, 25, 18, 30) ,"" ),
            Transaction(TransactionType.EXPENDITURE, 3000, categories.get(2), "Something...", GregorianCalendar(2018, 4, 10, 18, 55) ,"" )
    )

}