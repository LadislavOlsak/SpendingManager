package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.account

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.home.Account
import com.google.gson.Gson

class AccountActivity : AppCompatActivity() {

    var account : Account? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        initAccount()
        setTitle()
    }

    private fun setTitle() {
        supportActionBar?.title = account?.accountName
    }

    private fun initAccount() {
        account = Gson().fromJson(intent.getStringExtra("account"), Account::class.java)
        if(account == null) {
            throw IllegalArgumentException("Variable 'account' is not specified.")
        }
    }
}
