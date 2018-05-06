package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.home

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.loyaltycards.LoyaltyCardsActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.CategoriesActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.limits.LimitsActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.login.LoginActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.login.UserData
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.loyaltycards.LoyaltyCard
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.planning.PlannedTransactionsActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics.StatisticsActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.TransactionActivity
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.math.BigDecimal
import java.util.*
import android.widget.Toast

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val LOGIN_ACTIVITY_RESULT_CODE = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        nav_view.setNavigationItemSelectedListener(this)

        startLoginIntent()

        fab.setOnClickListener {
            startTransactionActivity()
        }

        //todo load data
        val accounts = listOf(
            Account("My Cash Account", Currency.getInstance("CZK"), BigDecimal("1000.0"))
        )
        main_accounts_list.adapter = AccountsAdapter(this, accounts)

        main_account_stats.adapter = AccountStatsAdapter(this, listOf(AccountStats("A", 1.1f),
                AccountStats("B", 25.1f),
                AccountStats("C", 56.3f), AccountStats("D", 99.9f)))
    }

    private fun startLoginIntent() {
        if(!UserData.isUserLoggedIn()) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivityForResult(loginIntent, LOGIN_ACTIVITY_RESULT_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGIN_ACTIVITY_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            setUserDetails()
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
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                //do nothing
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
                    //todo
                    Toast.makeText(this, "Logged out successfully.", Toast.LENGTH_LONG).show()
                }
    }

    private fun startTransactionActivity() {
        startActivity(Intent(this, TransactionActivity::class.java))
    }
}
