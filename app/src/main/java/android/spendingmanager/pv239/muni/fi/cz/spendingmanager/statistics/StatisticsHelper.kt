package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.CategoryType
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.TransactionType
import com.google.android.gms.maps.model.LatLng
import java.util.*



public class StatisticsHelper {

    constructor()  {
        categories = listOf(
                Category("food", "food_and_drinks", CategoryType.DEFAULT),
                Category("housing", "housing", CategoryType.DEFAULT),
                Category("entertainment", "entertainment", CategoryType.DEFAULT),
                Category("others", "others", CategoryType.DEFAULT),
                Category("shopping", "shopping", CategoryType.DEFAULT))

        transactions = listOf(
                Transaction(TransactionType.EXPENDITURE, 2500, categories.get(1), "Something...", GregorianCalendar(2018, 1-1, 25, 19, 20), LatLng(49.247, 16.685) ),
                Transaction(TransactionType.EXPENDITURE, 1500, categories.get(1), "Something...", GregorianCalendar(2018, 2-1, 25, 18, 30), LatLng(49.385, 16.665) ),
                Transaction(TransactionType.EXPENDITURE, 1800, categories.get(1), "Something...", GregorianCalendar(2018, 3-1, 25, 18, 30), LatLng(49.225, 16.889) ),
                Transaction(TransactionType.EXPENDITURE, 3000, categories.get(1), "Something...", GregorianCalendar(2018, 4-1, 10, 18, 55), LatLng(49.213, 16.789) ),
                Transaction(TransactionType.EXPENDITURE, 250, categories.get(0), "Some meet and fruits", GregorianCalendar(2018, 3-1, 12, 12, 34),LatLng(52.248, 17.685) ),
                Transaction(TransactionType.EXPENDITURE, 80, categories.get(0), "Some meet and fruits", GregorianCalendar(2018, 3-1, 13, 11, 21),LatLng(60.247, 15.669) ),
                Transaction(TransactionType.EXPENDITURE, 40, categories.get(0), "Bread and rolls", GregorianCalendar(2018, 3-1, 13, 11, 22),LatLng(49.247, 16.458) ),
                Transaction(TransactionType.EXPENDITURE, 850, categories.get(0), "Everything", GregorianCalendar(2018, 3-1, 16, 13, 50),LatLng(49.269, 16.447) ),
                Transaction(TransactionType.EXPENDITURE, 563, categories.get(0), "Something...", GregorianCalendar(2018, 3-1, 25, 12, 40),LatLng(49.651, 16.569) ),
                Transaction(TransactionType.EXPENDITURE, 85, categories.get(0), "Something...", GregorianCalendar(2018, 3-1, 28, 16, 23),LatLng(49.374, 16.786) ),
                Transaction(TransactionType.EXPENDITURE, 420, categories.get(0), "Something...", GregorianCalendar(2018, 4-1, 2, 9, 20),LatLng(49.295, 16.366) ),
                Transaction(TransactionType.EXPENDITURE, 236, categories.get(0), "Something...", GregorianCalendar(2018, 4-1, 6, 12, 20),LatLng(49.418, 16.346) )

                ,Transaction(TransactionType.EXPENDITURE, 2500, categories.get(1), "Something...", GregorianCalendar(2018, 1-1, 25, 19, 20), LatLng(49.247, 16.685) ),
                Transaction(TransactionType.EXPENDITURE, 1500, categories.get(1), "Something...", GregorianCalendar(2018, 2-1, 25, 18, 30), LatLng(49.385, 16.665) ),
                Transaction(TransactionType.EXPENDITURE, 1800, categories.get(1), "Something...", GregorianCalendar(2018, 3-1, 25, 18, 30), LatLng(49.225, 16.889) ),
                Transaction(TransactionType.EXPENDITURE, 3000, categories.get(1), "Something...", GregorianCalendar(2018, 4-1, 10, 18, 55), LatLng(49.213, 16.789) ),
                Transaction(TransactionType.EXPENDITURE, 250, categories.get(0), "Some meet and fruits", GregorianCalendar(2018, 3-1, 12, 12, 34),LatLng(52.248, 17.685) ),
                Transaction(TransactionType.EXPENDITURE, 80, categories.get(0), "Some meet and fruits", GregorianCalendar(2018, 3-1, 13, 11, 21),LatLng(60.247, 15.669) ),
                Transaction(TransactionType.EXPENDITURE, 40, categories.get(0), "Bread and rolls", GregorianCalendar(2018, 3-1, 13, 11, 22),LatLng(49.247, 16.458) ),
                Transaction(TransactionType.EXPENDITURE, 850, categories.get(0), "Everything", GregorianCalendar(2018, 3-1, 16, 13, 50),LatLng(49.269, 16.447) ),
                Transaction(TransactionType.EXPENDITURE, 563, categories.get(0), "Something...", GregorianCalendar(2018, 3-1, 25, 12, 40),LatLng(49.651, 16.569) ),
                Transaction(TransactionType.EXPENDITURE, 85, categories.get(0), "Something...", GregorianCalendar(2018, 3-1, 28, 16, 23),LatLng(49.374, 16.786) ),
                Transaction(TransactionType.EXPENDITURE, 420, categories.get(0), "Something...", GregorianCalendar(2018, 4-1, 2, 9, 20),LatLng(49.295, 16.366) ),
                Transaction(TransactionType.EXPENDITURE, 236, categories.get(0), "Something...", GregorianCalendar(2018, 4-1, 6, 12, 20),LatLng(49.418, 16.346) )

        )
    }

    var categories : List<Category>
    var transactions : List<Transaction>

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

        val transactionsAll = transactions;
        val transactionsListIterator = transactionsAll.iterator()
        while (transactionsListIterator.hasNext()) {
            val transaction = transactionsListIterator.next()

            val date = CalculateStartDate(weeks, endDate, 0)
            if (transaction.category.id == category.id && transaction.datetime.time >= date.time && transaction.datetime.time < endDate.time)
            {
                if (hour == null || transaction.datetime.get(Calendar.HOUR_OF_DAY) == hour)
                {
                    transactionsList.add(transaction)
                }
            }
        }
        return transactionsList;
    }

    public fun CalculateValueTransactions (category : Category, weeks : Int, date : Calendar) : Int
    {
        var value = 0;
        val transactionsList = GetTransactions(category, weeks, date, null)

        val transactionsListIterator = transactionsList.iterator()
        while (transactionsListIterator.hasNext()) {
            val transaction = transactionsListIterator.next()
            value += transaction.amount
        }
        return value;
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

    public fun GetColors () : List<Int>
    {
        var colorList  : MutableList<Int>  = mutableListOf<Int>()
        colorList.add(Color.BLACK)
        colorList.add(Color.BLUE)
        colorList.add(Color.GREEN)
        colorList.add(Color.RED)
        colorList.add(Color.YELLOW)
        colorList.add(Color.CYAN)
        colorList.add(Color.DKGRAY)
        colorList.add(Color.MAGENTA)
        colorList.add(Color.GRAY)
        colorList.add(Color.LTGRAY)
        return colorList
    }
}