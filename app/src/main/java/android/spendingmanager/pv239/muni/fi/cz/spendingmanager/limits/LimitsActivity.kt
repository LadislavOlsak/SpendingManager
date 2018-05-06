package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.limits

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.CategoriesAdapter
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.Category
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories.CategoryType
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.SwitchCompat
import android.view.View
import android.widget.EditText
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

    val allCategories : MutableList<Category> = mutableListOf()
    val allLimits : MutableList<CategoryLimit> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_limits)

        val context = this
        val list = findViewById<ListView>(R.id.limits_settings_list) as ListView

        FirebaseDb.getUserReference("categories")?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val category = postSnapshot.getValue<Category>(Category::class.java)
                    category?.key = postSnapshot.key
                    if (category != null && category.id != "")
                    {
                        allCategories.add(category)
                    }
                }

                val defaultCategories = context.getResources().getStringArray(R.array.default_categories)
                defaultCategories.forEach { category ->
                    allCategories.add(Category(category, category, CategoryType.DEFAULT))
                }

                FirebaseDb.getUserReference("limits")?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (postSnapshot in snapshot.children) {
                            val categoryLimit = postSnapshot.getValue<CategoryLimit>(CategoryLimit::class.java)
                            categoryLimit?.key = postSnapshot.key
                            if (categoryLimit != null && categoryLimit.categoryName != ""
                                    && allCategories.filter{ it.categoryName == categoryLimit.categoryName}.count() > 0
                                    && allLimits.filter{ it.categoryName == categoryLimit.categoryName}.count() == 0)
                            {
                                allLimits.add(categoryLimit)
                            }
                        }

                        allCategories.forEach { category ->
                            if (allLimits.filter{ it.categoryName == category.categoryName}.count() == 0) {
                                allLimits.add(CategoryLimit(category.categoryName, false, 0))
                            }
                        }

                        list.adapter = CategoryLimitsAdapter(context, allLimits)

                        limits_save_changed_btn.setOnClickListener { saveChanges(list, allLimits) }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    private fun saveChanges(list: ListView, limits : MutableList<CategoryLimit>) {
        val listCount = list.count
        limits.forEach { limit ->
            if (limit.key != null)
            {
                FirebaseDb.getUserReference("limits")?.child(limit.key)?.removeValue()
            }
        }
        finish()

        for (i in 0..listCount-1)
        {
            val item : View = getViewByPosition(i, list)

            val categoryNameTv = item.findViewById(R.id.limits_item_category_name) as TextView
            val categoryEnabledSw = item.findViewById(R.id.limits_item_switch) as SwitchCompat
            val categoryLimitEt = item.findViewById(R.id.limits_item_value) as EditText
            //val categoryLimitCurrencySp = item.findViewById(R.id.limits_item_currency) as AppCompatSpinner

            val newCategoryLimit = CategoryLimit(categoryNameTv.getText().toString(), categoryEnabledSw.isChecked, categoryLimitEt.getText().toString().toInt())
            FirebaseDb().createObject("limits", newCategoryLimit)
            finish()
        }
        Toast.makeText(this, "TODO: Changes are saved!", Toast.LENGTH_LONG).show()
    }

    fun getViewByPosition(position: Int, listView: ListView): View {
        val firstListItemPosition = listView.firstVisiblePosition
        val lastListItemPosition = firstListItemPosition + listView.childCount - 1

        if (position < firstListItemPosition || position > lastListItemPosition) {
            return listView.adapter.getView(position, listView.getChildAt(position), listView)
        } else {
            val childIndex = position - firstListItemPosition
            return listView.getChildAt(childIndex)
        }
    }
}
