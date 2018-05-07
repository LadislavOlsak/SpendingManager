package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.account

import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.home.Account
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.TransactionType
import android.view.View
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.account_details.*
import kotlinx.android.synthetic.main.account_last_transactions.*
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class AccountActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    var account : Account? = null
    var transactionFrom : Date? = null
    var transactionTo : Date? = null
    private var lastTransactionAdapter : LastTransactionsAdapter? = null
    private var accountDetailsAdapter : AccountDetailsAdapter? = null
    private var transactionsListener : ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        initTransactionSection()
    }

    private fun initTransactionSection() {
        getTransactionMockedData()

        val calendar = Calendar.getInstance()
        val now = calendar.time
        calendar.add(Calendar.MONTH, -1)
        val monthAgo = calendar.time

        setTransactionDateRange(monthAgo, now)

        account_last_filter_iv.setOnClickListener { displayTransactionDateFilterDialog() }
        lastTransactionAdapter = LastTransactionsAdapter(this, mutableListOf())
        account_last_transactions_lv.adapter = lastTransactionAdapter
        account_last_transaction_col_exp_tbtn.setOnCheckedChangeListener { _, checked ->
            val visibility = if(checked) View.GONE else View.VISIBLE
            account_last_transaction_content_layout.visibility = visibility
        }
    }

    private fun setTransactionDateRange(fromDate : Date, toDate: Date) {
        val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val toDateString = dateFormatter.format(toDate)
        val fromDateString = dateFormatter.format(fromDate)
        account_last_transaction_date_range_tv.text = "$fromDateString - $toDateString"

        transactionFrom = fromDate
        transactionTo = toDate

        FirebaseDb.getUserReference("transactions")?.orderByChild("datetime")?.addListenerForSingleValueEvent(transactionsListener)
    }

    private fun displayTransactionDateFilterDialog() {
        val calendar = Calendar.getInstance()
        calendar.time = transactionFrom

        val dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                this@AccountActivity,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        )
        dpd.maxDate = Calendar.getInstance()
        dpd.show(fragmentManager, "DatePickerDialog")
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int, yearEnd: Int, monthOfYearEnd: Int, dayOfMonthEnd: Int) {
        val calendar = Calendar.getInstance()

        calendar.set(year, monthOfYear, dayOfMonth, 0, 0)
        val fromDate = calendar.time

        calendar.set(yearEnd, monthOfYearEnd, dayOfMonthEnd, 0, 0)
        val toDate = calendar.time
        if(fromDate.after(toDate)) {
            Toast.makeText(this, "Invalid Dates: from date must be before end date.", Toast.LENGTH_LONG).show()
        }else {
            setTransactionDateRange(fromDate, toDate)
        }
    }

    private fun getTransactionMockedData() {

        transactionsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val transactions = mutableListOf<Transaction>()
                dataSnapshot.children.mapNotNullTo(transactions) {
                    val transaction = it.getValue<Transaction>(Transaction::class.java)
                    transaction?.key = it.key
                    transaction
                }
                val transactionsFromRange = transactions.filter { x -> !(x.datetime.before(transactionFrom) || x.datetime.after(transactionTo)) }

                lastTransactionAdapter?.update(transactionsFromRange.reversed())
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        FirebaseDb.getUserReference("transactions")?.orderByChild("datetime")?.addValueEventListener(transactionsListener)
    }

    private fun isThisMonth(testDate: Date): Boolean {
        val testCalendar = Calendar.getInstance()
        testCalendar.time = testDate
        val thisMonth = Calendar.getInstance().get(Calendar.MONTH)
        return testCalendar.get(Calendar.MONTH) == thisMonth
    }
}
