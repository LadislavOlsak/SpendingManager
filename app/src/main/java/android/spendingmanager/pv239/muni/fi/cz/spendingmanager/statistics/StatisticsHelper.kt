package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.CategoryType
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.TransactionType
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*



public class StatisticsHelper {

    constructor()  {
        categories = listOf(
                Category("food", "food_and_drinks", CategoryType.DEFAULT),
                Category("housing", "housing", CategoryType.DEFAULT),
                Category("entertainment", "entertainment", CategoryType.DEFAULT),
                Category("others", "others", CategoryType.DEFAULT),
                Category("shopping", "shopping", CategoryType.DEFAULT))

        val date = Calendar.getInstance().time
        transactions = mutableListOf(
                Transaction(TransactionType.EXPENDITURE, 2500.0, categories[1], "Something...", date, LatLng(49.247, 16.685), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 1500.0, categories[1], "Something...", date, LatLng(49.385, 16.665), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 1800.0, categories[1], "Something...", date, LatLng(49.225, 16.889), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 3000.0, categories[1], "Something...", date, LatLng(49.213, 16.789), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 250.0, categories[0], "Some meet and fruits", date, LatLng(52.248, 17.685), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 80.0, categories[0], "Some meet and fruits", date, LatLng(60.247, 15.669), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 40.0, categories[0], "Bread and rolls", date, LatLng(49.247, 16.458), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 850.0, categories[0], "Everything", date, LatLng(49.269, 16.447), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 563.0, categories[0], "Something...", date, LatLng(49.651, 16.569), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 85.0, categories[0], "Something...", date, LatLng(49.374, 16.786), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 420.0, categories[0], "Something...", date, LatLng(49.295, 16.366), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 236.0, categories[0], "Something...", date, LatLng(49.418, 16.346), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 2500.0, categories[1], "Something...", date, LatLng(49.247, 16.685), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 1500.0, categories[1], "Something...", date, LatLng(49.385, 16.665), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 1800.0, categories[1], "Something...", date, LatLng(49.225, 16.889), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 3000.0, categories[1], "Something...", date, LatLng(49.213, 16.789), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 250.0, categories[0], "Some meet and fruits", date, LatLng(52.248, 17.685), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 80.0, categories[0], "Some meet and fruits", date, LatLng(60.247, 15.669), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 40.0, categories[0], "Bread and rolls", date, LatLng(49.247, 16.458), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 850.0, categories[0], "Everything", date, LatLng(49.269, 16.447), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 563.0, categories[0], "Something...", date, LatLng(49.651, 16.569), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 85.0, categories[0], "Something...", date, LatLng(49.374, 16.786), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 420.0, categories[0], "Something...", date, LatLng(49.295, 16.366), "CZK"),
                Transaction(TransactionType.EXPENDITURE, 236.0, categories[0], "Something...", date, LatLng(49.418, 16.346), "CZK")
        )

        val transactionsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(transactions) {
                    val transaction = it.getValue<Transaction>(Transaction::class.java)
                    transaction?.key = it.key
                    transaction
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        FirebaseDb.getUserReference("transactions")?.addValueEventListener(transactionsListener)

    }

    var categories : List<Category>
    var transactions : MutableList<Transaction> = mutableListOf()

    public fun GetCategories () : List<Category>
    {
        return categories;
    }

    public fun GetCategoriesName () : List<String>
    {
        val categories : MutableList<String> = mutableListOf<String>()
        val categoriesList : List<Category> = StatisticsHelper().GetCategories()
        categoriesList.forEachIndexed { index, category ->
            categories.add(category.categoryName)
        }
        return categories;
    }

    public fun GetTransactions () : List<Transaction>
    {
        return transactions;
    }

    public fun GetTransactions (category : Category, weeks : Int, endDate : Calendar, hour : Int?) : List<Transaction>
    {
        var transactionsList  : MutableList<Transaction>  = mutableListOf<Transaction>()

        val transactionsAll = transactions
        val transactionsListIterator = transactionsAll.iterator()
        while (transactionsListIterator.hasNext()) {
            val transaction = transactionsListIterator.next()
            val date = CalculateStartDate(weeks, endDate, 0)


            if (transaction.category.id == category.id && transaction.datetime.after(date.time) && transaction.datetime.before(endDate.time))
            {
                //todo deprecated
                if (hour == null || transaction.datetime.hours == hour)
                {
                    transactionsList.add(transaction)
                }
            }
        }
        return transactionsList;
    }

    public fun CalculateValueTransactions (category : Category, weeks : Int, date : Calendar) : Double
    {
        var value = 0.0;
        val transactionsList = GetTransactions(category, weeks, date, null)

        val transactionsListIterator = transactionsList.iterator()
        while (transactionsListIterator.hasNext()) {
            val transaction = transactionsListIterator.next()
            value += transaction.price
        }
        return value
    }

    public fun CalculateTimeTransactions (category : Category, weeks : Int, date : Calendar, hour: Int) : Int
    {
        val transactionsList = GetTransactions(category, weeks, date, hour)
        return transactionsList.count();
    }

    public fun CalculateStartDate (weeks : Int, endDate : Calendar, additionalDay: Int) : GregorianCalendar
    {
        var date : GregorianCalendar = endDate.clone() as GregorianCalendar // nutná kopie, jinak se referenčně  pořád čas posouvá
        val noOfDays = ((7 * weeks) + additionalDay)*(-1)
        date.add(Calendar.DAY_OF_YEAR, noOfDays)
        return date;
    }

    fun getColors () : List<Int>
    {
        return mutableListOf<Int>(
            Color.BLACK, Color.BLUE, Color.GREEN,
            Color.RED, Color.YELLOW, Color.CYAN,
            Color.DKGRAY, Color.MAGENTA, Color.GRAY,
            Color.LTGRAY)
    }
}