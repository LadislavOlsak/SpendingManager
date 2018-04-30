package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase

import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.login.UserData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseDb {

    fun <T> createObject(path : String, obj : T) {
        val reference = getUserReference()?.child(path)

        val key = reference?.push()?.key

        reference?.child(key)?.setValue(obj)
    }

    fun <T> updateObject(path : String, key : String?, obj : T, listener: DatabaseReference.CompletionListener? = null) {
        val reference = getUserReference()?.child(path)

        reference?.child(key)?.setValue(obj, listener)
    }

    fun deleteObject(path : String, key : String?) {
        getUserReference()?.child(path)?.child(key)?.setValue(null)
    }

    companion object {

        private var firebaseDb : FirebaseDatabase? = null

        private fun getDatabase() : FirebaseDatabase? {
            if(firebaseDb == null) {
                firebaseDb = FirebaseDatabase.getInstance()
                firebaseDb?.setPersistenceEnabled(true)
            }

            return firebaseDb
        }

        fun getReference(ref : String) : DatabaseReference? {
            return getDatabase()?.getReference(ref)
        }

        fun getUserReference() : DatabaseReference? {
            return getReference("users")?.child(UserData.getFirebaseUser()?.uid)
        }

        fun getUserReference(path : String) : DatabaseReference? {
            return getUserReference()?.child(path)
        }
    }
}