package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories

import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import com.google.firebase.database.ValueEventListener

class AllCategories {

    companion object {
        fun getCustomCategories(listener : ValueEventListener) {
            FirebaseDb.getUserReference("categories")?.addValueEventListener(listener)
        }
    }
}