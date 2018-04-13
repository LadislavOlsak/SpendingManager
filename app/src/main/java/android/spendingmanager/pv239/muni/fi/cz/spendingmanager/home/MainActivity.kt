package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.home

import android.content.Intent
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.loyaltycards.LoyaltyCardsActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.CategoriesActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.limits.LimitsActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics.StatisticsActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.TransactionActivity
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.math.BigDecimal
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            startTransactionActivity()
        }

        //todo load data
        val accounts = listOf(
            Account("My Cash Account", Currency.getInstance("CZK"), BigDecimal("1000.0")),
            Account("EUR Cash Account", Currency.getInstance("EUR"), BigDecimal("120.54")),
            Account("Polish Cash", Currency.getInstance("PLN"), BigDecimal("680.54")),
            Account("Bank Account", Currency.getInstance("CZK"), BigDecimal("-28.00"))
        )

        main_accounts_list.adapter = AccountsAdapter(this, accounts)

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
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
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun startTransactionActivity() {
        startActivity(Intent(this, TransactionActivity::class.java))
    }
}
