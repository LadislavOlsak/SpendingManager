package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.graphics.Color
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.AllCategories
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.DefaultCategories
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*
import java.util.concurrent.Semaphore

public class StatisticsHelper {

    var categories : MutableList<Category> = mutableListOf()
    var transactions : MutableList<Transaction> = mutableListOf()

    constructor()  {
        Load()
    }

    private fun Load()
    {
        //val semaphore = Semaphore(0)
        val categoriesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Category>()
                snapshot.children.mapNotNullTo(list) {
                    val category = it.getValue<Category>(Category::class.java)
                    category?.key = it.key
                    category
                }
                categories.clear()
                list.forEach { x -> categories.add(x) }
                DefaultCategories.getDefaultCategories().forEach { x -> categories.add(x) }
                //semaphore.release()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }

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
        FirebaseDb.getUserReference("categories")?.addValueEventListener(categoriesListener)
        FirebaseDb.getUserReference("transactions")?.addValueEventListener(transactionsListener)
        //semaphore.acquire()
    }
    public fun GetCategories () : List<Category>
    {
        return categories
    }

    public fun GetTransactions (category : String, transactionsAll : List<Transaction>, weeks : Int, endDate : Calendar, hour : Int?) : List<Transaction>
    {
        var transactionsList  : MutableList<Transaction>  = mutableListOf<Transaction>()

        val transactionsListIterator = transactionsAll.iterator()
        while (transactionsListIterator.hasNext()) {
            val transaction = transactionsListIterator.next()
            val date = CalculateStartDate(weeks, endDate, 0)


            if (transaction.category.categoryName == category && transaction.datetime.after(date.time) && transaction.datetime.before(endDate.time))
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

    public fun GetTransactions (category : Category, weeks : Int, endDate : Calendar, hour : Int?) : List<Transaction>
    {
        var transactionsList  : MutableList<Transaction>  = mutableListOf<Transaction>()

        val transactionsAll = transactions
        val transactionsListIterator = transactionsAll.iterator()
        while (transactionsListIterator.hasNext()) {
            val transaction = transactionsListIterator.next()
            val date = CalculateStartDate(weeks, endDate, 0)


            if (transaction.category.key == category.key && transaction.datetime.after(date.time) && transaction.datetime.before(endDate.time))
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

    public fun CalculateValueTransactions (category : String, transactionsAll: List<Transaction>, weeks : Int, date : Calendar) : Double
    {
        var value = 0.0;
        val transactionsList = GetTransactions(category, transactionsAll, weeks, date, null)

        val transactionsListIterator = transactionsList.iterator()
        while (transactionsListIterator.hasNext()) {
            val transaction = transactionsListIterator.next()
            value += transaction.price
        }
        return value
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

    public fun CalculateTimeTransactions (category : String, transactionsAll: List<Transaction>, weeks : Int, date : Calendar, hour: Int) : Int
    {
        val transactionsList = GetTransactions(category, transactionsAll, weeks, date, hour)
        return transactionsList.count();
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