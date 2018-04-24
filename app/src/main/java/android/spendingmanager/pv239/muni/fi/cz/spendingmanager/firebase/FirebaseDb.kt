package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseDb {
    companion object {

        private var firebaseDb : FirebaseDatabase? = null

        private fun getDatabase() : FirebaseDatabase? {
            if(firebaseDb == null) {
                firebaseDb = FirebaseDatabase.getInstance()
            }

            return firebaseDb
        }

        public fun getReference(ref : String) : DatabaseReference? {
            return getDatabase()?.getReference(ref)
        }
    }
}