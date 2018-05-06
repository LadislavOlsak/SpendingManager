package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.limits.CategoryLimit
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.loyaltycards.LoyaltyCard
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.planning.TransactionFrequency
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import android.widget.AdapterView
import android.view.ContextMenu
import android.view.MenuItem


class CategoriesActivity : AppCompatActivity() {

    val allCategories : MutableList<Category> = mutableListOf()
    var adapter : CategoriesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        val context = this

        AllCategories.getCustomCategories(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allCategories.clear()
                for (postSnapshot in snapshot.children) {
                    val category = postSnapshot.getValue<Category>(Category::class.java)
                    category?.key = postSnapshot.key
                    if (category != null && category.id != "")
                    {
                        allCategories.add(category)
                    }
                }
                adapter?.update(allCategories)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        val customListView = findViewById<View>(R.id.categories_custom_list) as ListView
        adapter = CategoriesAdapter(context, allCategories)
        customListView.adapter = adapter
        registerForContextMenu(customListView)

        val customInput = findViewById<View>(R.id.categories_add_custom) as EditText
        val customButton = findViewById<View>(R.id.categories_add_custom_button) as Button
        customButton.setOnClickListener {
            val newItem = customInput.text.toString()
            customInput.setText("")
            val newCategory = Category(newItem, newItem, CategoryType.CUSTOM)
            FirebaseDb().createObject("categories", newCategory)
            val categoryLimit = CategoryLimit(newCategory.categoryName, 0.0, "CZK", false, TransactionFrequency.Monthly)
            FirebaseDb().createObject("categorylimits", categoryLimit)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val listView = findViewById<View>(R.id.categories_custom_list) as ListView
        if (v.id == listView.id) {

            //menu.setHeaderTitle("Context Menu Example")
            menu.add(0, 0, 0, "Delete")
            //menu.add(0, 0, 0, "Edit (not implemented yet)")
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.title === "Delete") {
            val menuInfo: AdapterView.AdapterContextMenuInfo = item.menuInfo as AdapterView.AdapterContextMenuInfo
            val index = menuInfo.position
            var category = allCategories.get(index)
            FirebaseDb.getUserReference("categories")?.child(category.key)?.removeValue()
            deleteCategoryLimit(category.categoryName)
        } else {
            return false
        }
        return true
    }

    private fun deleteCategoryLimit(categoryName: String) {
        FirebaseDb.getUserReference("categorylimits")?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                val limits = mutableListOf<CategoryLimit>()
                snapshot.children.mapNotNullTo(limits) {
                    val cl = it.getValue<CategoryLimit>(CategoryLimit::class.java)
                    if (cl?.categoryName == categoryName) {
                        FirebaseDb().deleteObject("categorylimits", it.key)
                    }
                    cl
                }
            }
        })
    }
}
