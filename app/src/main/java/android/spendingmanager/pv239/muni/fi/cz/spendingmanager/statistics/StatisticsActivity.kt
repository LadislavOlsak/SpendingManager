package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.CategoryType
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.DefaultCategories
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics.StatisticsActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.TransactionType
import java.util.GregorianCalendar;
import java.time.Month
import com.google.gson.Gson
import android.support.design.widget.TabLayout
import android.view.View
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class StatisticsActivity : AppCompatActivity() {

    private var adapter : StatisticsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        val tabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Date stats"))
        tabLayout.addTab(tabLayout.newTab().setText("Day time stats"))
        tabLayout.addTab(tabLayout.newTab().setText("Location stats"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val categoriesList : MutableList<Category> = mutableListOf<Category>()
        val categoriesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Category>()
                snapshot.children.mapNotNullTo(list) {
                    val category = it.getValue<Category>(Category::class.java)
                    category?.key = it.key
                    category
                }
                list.forEach { x -> categoriesList.add(x) }
                DefaultCategories.getDefaultCategories().forEach { x -> categoriesList.add(x) }

                val transactionsListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val transactions = mutableListOf<Transaction>()
                        dataSnapshot.children.mapNotNullTo(transactions) {
                            val transaction = it.getValue<Transaction>(Transaction::class.java)
                            transaction?.key = it.key
                            transaction
                        }
                        val expenseOnlyList = transactions.filter { x ->
                            x.type == TransactionType.EXPENDITURE
                        }

                        val viewPager = findViewById<View>(R.id.pager) as ViewPager
                        adapter = StatisticsPagerAdapter(supportFragmentManager, tabLayout.tabCount, expenseOnlyList, categoriesList)
                        viewPager.adapter = adapter
                        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
                        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                            override fun onTabSelected(tab: TabLayout.Tab) {
                                viewPager.currentItem = tab.position
                            }
                            override fun onTabUnselected(tab: TabLayout.Tab) { }
                            override fun onTabReselected(tab: TabLayout.Tab) { }
                        })
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        println("loadPost:onCancelled ${databaseError.toException()}")
                    }
                }
                FirebaseDb.getUserReference("transactions")?.addValueEventListener(transactionsListener)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        FirebaseDb.getUserReference("categories")?.addValueEventListener(categoriesListener)


    }


}
