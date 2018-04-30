package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.loyaltycards.LoyaltyCard
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        val context = this

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

                val customListView = findViewById<View>(R.id.categories_custom_list) as ListView

                registerForContextMenu(customListView)

                customListView.adapter = CategoriesAdapter(context, allCategories)

                val categoryCustomListView = findViewById<View>(R.id.categories_custom_list) as ListView
                val paramsCustom = categoryCustomListView.getLayoutParams()
                paramsCustom.height = 140 * allCategories.count()
                categoryCustomListView.setLayoutParams(paramsCustom)
                categoryCustomListView.requestLayout()

                val categoryDefaultListView = findViewById<View>(R.id.categories_default_list) as ListView
                val paramsDefault = categoryDefaultListView.getLayoutParams()
                paramsDefault.height = 700
                categoryDefaultListView.setLayoutParams(paramsDefault)
                categoryDefaultListView.requestLayout()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        //val defaultInput = findViewById<View>(R.id.categories_add_default) as EditText
        //val defaultButton = findViewById<View>(R.id.categories_add_default_button) as Button
        //defaultButton.setOnClickListener {
        //    val newItem = defaultInput.text.toString()
        //    val newCategory = Category(newItem, newItem, CategoryType.DEFAULT)
        //    FirebaseDb().createObject("categories", newCategory)
        //    finish()
        //}

        val customInput = findViewById<View>(R.id.categories_add_custom) as EditText
        val customButton = findViewById<View>(R.id.categories_add_custom_button) as Button
        customButton.setOnClickListener {
            val newItem = customInput.text.toString()
            val newCategory = Category(newItem, newItem, CategoryType.CUSTOM)
            FirebaseDb().createObject("categories", newCategory)
            finish()
        }

    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val listView = findViewById<View>(R.id.categories_custom_list) as ListView
        if (v.id == listView.getId()) {

            //menu.setHeaderTitle("Context Menu Example")
            menu.add(0, 0, 0, "Delete")
            menu.add(0, 0, 0, "Edit (not implemented yet)")
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.getTitle() === "Delete") {
            //Code To Handle deletion
            val menuInfo: AdapterView.AdapterContextMenuInfo = item.menuInfo as AdapterView.AdapterContextMenuInfo
            val index = menuInfo.position
            var category = allCategories.get(index)
            FirebaseDb.getUserReference("categories")?.child(category.key)?.removeValue()
            //FirebaseDb().deleteObject("categories", category.id)
            finish()
        } else {
            return false
        }
        return true
    }
}
