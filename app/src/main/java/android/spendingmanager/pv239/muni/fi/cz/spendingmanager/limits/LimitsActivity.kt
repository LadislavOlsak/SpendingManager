package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.limits

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.AllCategories
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.DefaultCategories
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.planning.TransactionFrequency
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_limits.*
import java.math.BigDecimal
import java.util.*

class LimitsActivity : AppCompatActivity() {

    private var adapter : CategoryLimitsAdapter? = null
    private var limitsMap = hashMapOf<String, CategoryLimit?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_limits)

        val limitsList = findViewById<ListView>(R.id.limits_settings_list)

        AllCategories.getCustomCategories(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Category>()
                snapshot.children.mapNotNullTo(list) {
                    val category = it.getValue<Category>(Category::class.java)
                    category?.key = it.key
                    category
                }
                limitsMap.clear()
                list.forEach { x -> limitsMap[x.categoryName] = null }
                DefaultCategories.getDefaultCategories().forEach { x -> limitsMap[x.categoryName] = null }
                loadLimits()
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        adapter = CategoryLimitsAdapter(this@LimitsActivity, emptyList())
        limitsList.adapter = adapter

        limits_save_changed_btn.setOnClickListener { saveChanges() }
    }

    private fun loadLimits() {
        FirebaseDb.getUserReference("categorylimits")?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) { }
            override fun onDataChange(snapshot: DataSnapshot) {
                val limits = mutableListOf<CategoryLimit>()
                snapshot.children.mapNotNullTo(limits) {
                    val cl = it.getValue<CategoryLimit>(CategoryLimit::class.java)
                    cl?.key = it.key
                    limitsMap[cl?.categoryName as String] = cl
                    cl
                }
                limitsMap.forEach { categName, categoryLimit ->
                    if (categoryLimit == null) {
                        limitsMap[categName] = CategoryLimit(categName, 0.0, "CZK", false, TransactionFrequency.Monthly)
                        FirebaseDb().createObject("categorylimits", limitsMap[categName])
                    }
                }
                adapter?.update(limitsMap.values.toList() as List<CategoryLimit>)
            }
        })
    }

    private fun saveChanges() {
        adapter?.categoryLimits?.forEach { x ->
            FirebaseDb().updateObject("categorylimits", x.key, x)
        }

        Toast.makeText(this, "TODO: Changes are saved!", Toast.LENGTH_LONG).show()
    }
}
