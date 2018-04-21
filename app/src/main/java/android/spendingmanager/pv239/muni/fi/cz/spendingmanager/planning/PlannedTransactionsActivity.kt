package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.planning

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import kotlinx.android.synthetic.main.planned_transaction_expenses.*
import kotlinx.android.synthetic.main.planned_transaction_income.*

class PlannedTransactionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planned_transactions)

        planned_transaction_expenses_lv.adapter = PlannedTransactionAdapter(this, createMockData())
        planned_transaction_income_lv.adapter = PlannedTransactionAdapter(this, createMockData())
    }

    private fun createMockData() : List<PlannedTransaction> {
        return listOf(
                PlannedTransaction(TransactionFrequency.Daily),
                PlannedTransaction(TransactionFrequency.Monthly)
        )
    }
}
