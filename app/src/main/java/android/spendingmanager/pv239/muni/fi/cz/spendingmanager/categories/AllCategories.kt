package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories

import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.Transaction
import android.view.View
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AllCategories {

    companion object {
        fun getCustomCategories(listener : ValueEventListener) {
            FirebaseDb.getUserReference("categories")?.addValueEventListener(listener)
        }
    }
}