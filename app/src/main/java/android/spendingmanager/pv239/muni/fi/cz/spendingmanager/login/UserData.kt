package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.login

import com.google.firebase.auth.FirebaseUser

class UserData {

    companion object {

        private var user : FirebaseUser? = null

        fun isUserLoggedIn() : Boolean {
            return getFirebaseUser() != null
        }

        fun setFirebaseUser(firebaseUser : FirebaseUser?) {
            user = firebaseUser
        }

        fun getFirebaseUser() : FirebaseUser? {
            return user
        }
    }
}