package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.loyaltycards.LoyaltyCardsActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.account.AccountActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.account.AccountDetail
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.account.AccountDetailsAdapter
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.account.LastTransactionsAdapter
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.AllCategories
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.CategoriesActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.DefaultCategories
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.limits.CategoryLimit
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.limits.FirebaseLimits
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.limits.LimitsActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.login.LoginActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.login.UserData
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.planning.PlannedTransactionsActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics.StatisticsActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.TransactionActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.TransactionType
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*
import android.widget.Toast
import az.plainpie.animation.PieAngleAnimation
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.account_details.*
import kotlinx.android.synthetic.main.account_graphs.*
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        DatePickerDialog.OnDateSetListener  {

    private val LOGIN_ACTIVITY_RESULT_CODE = 12
    var transactionFrom : Date? = null
    var transactionTo : Date? = null
    private var lastTransactionAdapter : LastTransactionsAdapter? = null
    private var accountDetailsAdapter : AccountDetailsAdapter? = null
    private var transactionsListener : ValueEventListener? = null
    private var currentLimit : CategoryLimit? = null

    private var monthExpenses = 0.0
    private var monthIncome = 0.0
    private var totalBalance = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nav_view.setNavigationItemSelectedListener(this)

        startLoginIntent()

        fab.setOnClickListener {
            startTransactionActivity()
        }

        accountDetailsAdapter = AccountDetailsAdapter(this, getDetailsMockData())
        account_details_list_lv.adapter = accountDetailsAdapter
    }

    private fun startLoginIntent() {
        if(!UserData.isUserLoggedIn()) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivityForResult(loginIntent, LOGIN_ACTIVITY_RESULT_CODE)
        } else {
            initTransactionSection()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGIN_ACTIVITY_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            setUserDetails()
            initTransactionSection()
        }
    }

    private fun setUserDetails() {
        val headerView = nav_view.getHeaderView(0)

        val navEmail = headerView.findViewById<TextView>(R.id.nav_signed_in_email)
        navEmail.text = UserData.getFirebaseUser()?.email

        val navName = headerView.findViewById<TextView>(R.id.nav_signed_in_name)
        navName.text = UserData.getFirebaseUser()?.displayName
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                //do nothing
            }
            R.id.nav_transactions -> {
                startActivity(Intent(this, AccountActivity::class.java))
            }
            R.id.nav_loyalty_cards -> {
                startActivity(Intent(this, LoyaltyCardsActivity::class.java))
            }
            R.id.nav_categories -> {
                startActivity(Intent(this, CategoriesActivity::class.java))
            }
            R.id.nav_limits -> {
                startActivity(Intent(this, LimitsActivity::class.java))
            }
            R.id.nav_statistics -> {
                startActivity(Intent(this, StatisticsActivity::class.java))
            }
            R.id.nav_planned_transactions -> {
                startActivity(Intent(this, PlannedTransactionsActivity::class.java))
            }
            R.id.nav_logout -> {
                logoutUser()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logoutUser() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    UserData.setFirebaseUser(null)
                    startLoginIntent()
                    Toast.makeText(this, "Logged out successfully.", Toast.LENGTH_LONG).show()
                }
    }

    private fun startTransactionActivity() {
        startActivity(Intent(this, TransactionActivity::class.java))
    }

    private fun initTransactionSection() {
        getTransactionMockedData()

        val calendar = Calendar.getInstance()
        val now = calendar.time
        calendar.add(Calendar.MONTH, -1)
        val monthAgo = calendar.time

        lastTransactionAdapter = LastTransactionsAdapter(this, mutableListOf())

        setTransactionDateRange(monthAgo, now)

        initListenersForCollapseExpand()

        initDropdowns()
    }

    private fun initDropdowns() {
        account_graphs_timeframe_sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (account_graphs_categories_sp.selectedItem == "Overall") {
                    monthIncome = if (monthIncome == 0.0) 0.0001 else monthIncome
                    setPercentage(((monthExpenses / monthIncome) * 100.0f).toFloat())
                } else {
                    setPieChart(currentLimit)
                }
            }
        }
        account_graphs_categories_sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                val selectedCatName = account_graphs_categories_sp.adapter.getItem(position) as String
                if ("Overall" == selectedCatName) {
                    monthIncome = if (monthIncome == 0.0) 0.0001 else monthIncome
                    setPercentage(((monthExpenses / monthIncome) * 100.0f).toFloat())
                }
                else {
                    if(FirebaseLimits.limits == null || FirebaseLimits.limits?.isEmpty() == true) {
                        loadLimits(selectedCatName)
                    } else {
                        FirebaseLimits.limits?.forEach { x ->
                            if (x.categoryName == selectedCatName) {
                                setPieChart(x)
                            }
                        }
                    }
                }
            }
        }
        account_graphs_categories_sp.setSelection(0)
        loadCategoriesToDropdown()
    }

    private fun loadLimits(selectedCatName : String) {
        FirebaseDb.getUserReference("categorylimits")?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                val list = mutableListOf<CategoryLimit>()
                p0.children.mapNotNullTo(list) {
                    val limit = it.getValue<CategoryLimit>(CategoryLimit::class.java)
                    if (limit?.categoryName == selectedCatName) {
                        setPieChart(limit)
                    }
                    limit
                }
                FirebaseLimits.limits = list
            }
        })
    }

    private fun setPieChart(limit : CategoryLimit?) {
        if(limit == null) {
            return
        }
        val frequency = getFrequency(account_graphs_timeframe_sp.selectedItem as String)
        val totalLimit = limit.limitAmount.toFloat() / frequency

        //todo load real transaction based on date today/this week/ this month etc.
        val percentage = if(totalLimit == 0.0f) 0.0001f else (3000.0f / totalLimit) * 100.0f
        setPercentage(percentage)
        currentLimit = limit
    }

    private fun setPercentage(percentage: Float) {
        val frequency = getFrequency(account_graphs_timeframe_sp.selectedItem as String)
        val progressPercentage = (getDayNumber(frequency).toFloat() / getTotalDaysInFrequency(frequency).toFloat()) * 100.0f
        chart_total_progress_pieView.percentage = percentage - progressPercentage
        chart_total_progress_pieView.setPercentageBackgroundColor(getProgressColor(chart_total_progress_pieView.percentage))
        val animation1 = PieAngleAnimation(chart_total_progress_pieView)
        animation1.duration = 2000
        chart_total_progress_pieView.startAnimation(animation1)

        chart_total_allowance_pieView.percentage = percentage
        chart_total_allowance_pieView.setPercentageBackgroundColor(getColor(chart_total_allowance_pieView.percentage))
        val animation2 = PieAngleAnimation(chart_total_allowance_pieView)
        animation2.duration = 2000
        chart_total_allowance_pieView.startAnimation(animation2)
    }

    private fun getDayNumber(frequency : Int) : Int {
        val c = Calendar.getInstance()
        return when(frequency) {
            1 -> c.get(Calendar.DAY_OF_MONTH)
            4 -> c.get(Calendar.DAY_OF_WEEK)
            30 -> 1
            else -> 30
        }
    }

    private fun getTotalDaysInFrequency(frequency: Int) : Int {
        return when(frequency) {
            1 -> 30
            4 -> 7
            30 -> 1
            else -> 30
        }
    }

    private fun getFrequency(frequency : String) : Int {
        return when (frequency.toLowerCase()) {
            "daily" -> 30
            "weekly" -> 4
            //"biweekly" -> 2
            "monthly" -> 1
            else -> 1
        }
    }

    private fun loadCategoriesToDropdown() {
        AllCategories.getCustomCategories(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categories = mutableListOf("Overall")
                categories.addAll(DefaultCategories.getDefaultCategories().map { x -> x.categoryName }.toMutableList())
                snapshot.children.forEach { postSnapshot ->
                    val category = postSnapshot.getValue<Category>(Category::class.java) as Category
                    category.key = postSnapshot.key
                    categories.add(category.categoryName)
                }
                account_graphs_categories_sp.adapter = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, categories)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun initListenersForCollapseExpand() {
        account_details_col_exp_tbtn.setOnCheckedChangeListener { _, checked ->
            val visibility = if (checked) View.GONE else View.VISIBLE
            account_details_content_layout.visibility = visibility
        }
        account_graphs_col_exp_tbtn.setOnCheckedChangeListener { _, checked ->
            val visibility = if (checked) View.GONE else View.VISIBLE
            account_graphs_content_layout.visibility = visibility
        }
    }

    private fun setTransactionDateRange(fromDate : Date, toDate: Date) {
        transactionFrom = fromDate
        transactionTo = toDate

        FirebaseDb.getUserReference("transactions")?.orderByChild("datetime")?.addListenerForSingleValueEvent(transactionsListener)
    }

    private fun displayTransactionDateFilterDialog() {
        val calendar = Calendar.getInstance()
        calendar.time = transactionFrom

        val dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                this@MainActivity,
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

    private fun getDetailsMockData() : List<AccountDetail> {
        return listOf(
                AccountDetail("Balance", ""),
                AccountDetail("Planned Expenses", ""),
                AccountDetail("Monthly Expenses", ""),
                AccountDetail("Monthly Income", "")
        )
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
                updateAccountDetails()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        FirebaseDb.getUserReference("transactions")?.orderByChild("datetime")?.addValueEventListener(transactionsListener)
    }

    private fun updateAccountDetails() {
        val plannedMonthExpenses = 0.0
        monthExpenses = 0.0
        totalBalance = 0.0
        monthIncome = 0.0

        lastTransactionAdapter?.transactions?.stream()?.forEach { x ->
            if(x.type == TransactionType.EXPENDITURE) {
                totalBalance -= x.price
                if(isThisMonth(x.datetime)) {
                    monthExpenses += x.price
                }
            } else if (x.type == TransactionType.INCOME) {
                totalBalance += x.price
                if(isThisMonth(x.datetime)) {
                    monthIncome += x.price
                }
            }
        }
        val currencySuffix = " CZK"

        accountDetailsAdapter?.details?.get(0)?.value = totalBalance.toString() + currencySuffix
        accountDetailsAdapter?.details?.get(1)?.value = plannedMonthExpenses.toString() + currencySuffix
        accountDetailsAdapter?.details?.get(2)?.value = monthExpenses.toString() + currencySuffix
        accountDetailsAdapter?.details?.get(3)?.value = monthIncome.toString() + currencySuffix
        accountDetailsAdapter?.notifyDataSetChanged()
    }

    private fun isThisMonth(testDate: Date): Boolean {
        val testCalendar = Calendar.getInstance()
        testCalendar.time = testDate
        val thisMonth = Calendar.getInstance().get(Calendar.MONTH)
        return testCalendar.get(Calendar.MONTH) == thisMonth
    }

    private fun getColor(percentage : Float) : Int {
        return when (percentage) {
            in 0.0..25.0 -> ContextCompat.getColor(this, R.color.pie_green)
            in 25.0..50.0 -> ContextCompat.getColor(this, R.color.pie_yellow)
            in 50.0..75.0 -> ContextCompat.getColor(this, R.color.pie_orange)
            else -> ContextCompat.getColor(this, R.color.pie_red)
        }
    }

    private fun getProgressColor(percentage : Float) : Int {
        return if ( percentage <= 0.0) ContextCompat.getColor(this, R.color.pie_green)
            else ContextCompat.getColor(this, R.color.pie_red)
    }
}
