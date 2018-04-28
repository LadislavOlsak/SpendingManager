package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.account

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R.id.account_details_list_lv
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.CategoryType
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.home.Account
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.TransactionType
import android.view.View
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.account_details.*
import kotlinx.android.synthetic.main.account_last_transactions.*
import kotlinx.android.synthetic.main.activity_account.*
import java.text.SimpleDateFormat
import java.util.*
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.home.MainActivity
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng


class AccountActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    var account : Account? = null
    var transactionFrom : Date? = null
    var transactionTo : Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        initAccount()
        setTitle()

        initTransactionSection()

        account_details_list_lv.adapter = AccountDetailsAdapter(this, getDetailsMockData())

        account_details_col_exp_tbtn.setOnCheckedChangeListener { _, checked ->
            val visibility = if(checked) View.GONE else View.VISIBLE
            account_details_content_layout.visibility = visibility
        }
    }

    private fun initTransactionSection() {
        val calendar = Calendar.getInstance()
        val now = calendar.time
        calendar.add(Calendar.MONTH, -1)
        val monthAgo = calendar.time

        setTransactionDateRange(now, monthAgo)

        account_last_filter_iv.setOnClickListener { displayTransactionDateFilterDialog() }
        account_last_transactions_lv.adapter = LastTransactionsAdapter(this, getTransactionMockedData())
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

        //TODO: filter data
    }

    private fun setTitle() {
        supportActionBar?.title = account?.accountName
    }

    private fun initAccount() {
        account = Gson().fromJson(intent.getStringExtra("account"), Account::class.java)
        if(account == null) {
            throw IllegalArgumentException("Variable 'account' is not specified.")
        }
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
            //todo: fix - better message, not toast
            Toast.makeText(this, "Invalid Dates: from date must be before end date.", Toast.LENGTH_LONG).show()
        }else {
            setTransactionDateRange(fromDate, toDate)
        }
    }

    private fun getDetailsMockData() : List<AccountDetail> {
        return listOf(
                AccountDetail("Balance", "7000 CZK"),
                AccountDetail("Planned Expenses", "3000 CZK"),
                AccountDetail("Monthly Expenses", "23000 CZK"),
                AccountDetail("Monthly Income", "33000 CZK")
        )
    }

    private fun getTransactionMockedData() : List<Transaction> {

        val categories = listOf(
                Category("food", getString(R.string.food_drinks), CategoryType.DEFAULT),
                Category("housing", getString(R.string.housing), CategoryType.DEFAULT),
                Category("entertainment", getString(R.string.entertainment), CategoryType.DEFAULT),
                Category("others",getString(R.string.others), CategoryType.DEFAULT),
                Category("shopping",getString(R.string.shopping), CategoryType.DEFAULT)
        )

        return listOf(
                Transaction(TransactionType.EXPENDITURE, -250, categories.get(0), "Some meet and fruits", GregorianCalendar(2018, 3, 12, 12, 34) , LatLng(49.3, 16.6), Currency.getInstance("CZK" )),
                Transaction(TransactionType.EXPENDITURE, -80, categories.get(1), "Some meet and fruits", GregorianCalendar(2018, 3, 13, 11, 21) ,LatLng(49.3, 16.6), Currency.getInstance("CZK" )),
                Transaction(TransactionType.EXPENDITURE, -40, categories.get(2), "Bread and rolls", GregorianCalendar(2018, 3, 13, 11, 22) ,LatLng(49.3, 16.6) , Currency.getInstance("CZK" )),
                Transaction(TransactionType.EXPENDITURE, -850, categories.get(3), "Everything", GregorianCalendar(2018, 3, 16, 13, 50) ,LatLng(49.3, 16.6), Currency.getInstance("CZK" )),
                Transaction(TransactionType.EXPENDITURE, -563, categories.get(4), "Something...", GregorianCalendar(2018, 3, 25, 12, 40) ,LatLng(49.3, 16.67), Currency.getInstance("CZK" ))
          )

    }
}
