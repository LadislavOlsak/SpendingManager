package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.content.res.Resources
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.CategoryType
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.TransactionType
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
                Transaction(TransactionType.EXPENDITURE, 250, categories.get(0), "Some meet and fruits", GregorianCalendar(2018, 3, 12, 12, 34) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 80, categories.get(0), "Some meet and fruits", GregorianCalendar(2018, 3, 13, 11, 21) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 40, categories.get(0), "Bread and rolls", GregorianCalendar(2018, 3, 13, 11, 22) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 850, categories.get(0), "Everything", GregorianCalendar(2018, 3, 16, 13, 50) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 563, categories.get(0), "Something...", GregorianCalendar(2018, 3, 25, 12, 40) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 85, categories.get(0), "Something...", GregorianCalendar(2018, 3, 28, 16, 23) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 420, categories.get(0), "Something...", GregorianCalendar(2018, 4, 2, 9, 20) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 236, categories.get(0), "Something...", GregorianCalendar(2018, 4, 6, 12, 20) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 2500, categories.get(1), "Something...", GregorianCalendar(2018, 1, 18, 19, 20) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 1500, categories.get(1), "Something...", GregorianCalendar(2018, 2, 25, 18, 30) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 1500, categories.get(1), "Something...", GregorianCalendar(2018, 3, 25, 18, 30) ,"" ),
                Transaction(TransactionType.EXPENDITURE, 3000, categories.get(1), "Something...", GregorianCalendar(2018, 4, 10, 18, 55) ,"" )
        )
    }

    var categories : List<Category>
    var transactions : List<Transaction>

    public fun GetCategories () : List<Category>
    {
        return categories;
    }

    public fun GetTransactions () : List<Transaction>
    {
        return transactions;
    }

    public fun GetTransactions (category : Category, weeks : Int, date : Calendar) : List<Transaction>
    {
        var transactionsList  : MutableList<Transaction>  = mutableListOf<Transaction>()

        val transactionsAll = transactions;
        val transactionsListIterator = transactionsAll.iterator()
        while (transactionsListIterator.hasNext()) {
            val transaction = transactionsListIterator.next()

            val noOfDays = (7 * weeks)*(-1)
            date.add(Calendar.DAY_OF_YEAR, noOfDays)
            if (transaction.category.id == category.id && transaction.datetime >= date)
            {
                transactionsList.add(transaction)
            }
        }
        return transactionsList;
    }

    public fun CalculateValueTransactions (category : Category, weeks : Int, date : Calendar) : Int
    {
        var value = 0;
        val transactionsList = GetTransactions(category, weeks, date)

        val transactionsListIterator = transactionsList.iterator()
        while (transactionsListIterator.hasNext()) {
            val transaction = transactionsListIterator.next()
            value += transaction.amount
        }
        return value;
    }
}