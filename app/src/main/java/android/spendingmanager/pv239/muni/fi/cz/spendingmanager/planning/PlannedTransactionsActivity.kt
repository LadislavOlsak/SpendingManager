package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.planning

import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.general.DatePickerDialog
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.TransactionType
import android.support.v7.widget.AppCompatSpinner
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.planned_transaction_expenses.*
import kotlinx.android.synthetic.main.planned_transaction_income.*
import java.text.SimpleDateFormat
import java.util.*

class PlannedTransactionsActivity : AppCompatActivity(), android.app.DatePickerDialog.OnDateSetListener {

    private var dialogContentView : View? = null
    private var freqSpinner : AppCompatSpinner? = null
    private var dateEt : EditText? = null
    private var transactionType = TransactionType.EXPENDITURE

    private var plannedExpenseAdapter : PlannedTransactionAdapter = PlannedTransactionAdapter(this, mutableListOf())
    private var plannedIncomeAdapter : PlannedTransactionAdapter = PlannedTransactionAdapter(this, mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planned_transactions)

        planned_transaction_expenses_lv.adapter = plannedExpenseAdapter
        planned_transaction_income_lv.adapter = plannedIncomeAdapter
        planned_transaction_expenses_addNew_iv.setOnClickListener { createNewPlannedTransaction(TransactionType.EXPENDITURE) }
        planned_transaction_income_addNew_iv.setOnClickListener { createNewPlannedTransaction(TransactionType.INCOME) }

        initDataListeners()
    }

    private fun createNewPlannedTransaction(tranType: TransactionType) {
        val dialog = AlertDialog.Builder(this)
                                .setPositiveButton("Save", dialogListener)
                                .setNegativeButton("Cancel", dialogListener)
                                .create()

        dialogContentView = this.layoutInflater.inflate(R.layout.transaction_item, null) as View
        dialog.setView(dialogContentView)
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val frequencyLayout = dialogContentView?.findViewById(R.id.transaction_item_frequency_layout) as LinearLayout
        frequencyLayout.visibility = View.VISIBLE

        freqSpinner = dialogContentView?.findViewById(R.id.limits_item_frequency) as AppCompatSpinner
        freqSpinner?.adapter = ArrayAdapter<TransactionFrequency>(this, android.R.layout.simple_list_item_1, TransactionFrequency.values())

//        val deleteItem = dialogContentView?.findViewById(R.id.transaction_item_delete_iv) as ImageView
//        deleteItem.visibility = View.GONE

        dateEt = dialogContentView?.findViewById(R.id.transaction_item_date) as EditText
        dateEt?.setText(SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Calendar.getInstance().time))

        val dateButton = dialogContentView?.findViewById<Button>(R.id.transaction_item_date_btn)
        dateButton?.setOnClickListener { DatePickerDialog(this).create(this).show() }

        this.transactionType = tranType
        dialog.show()
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        val c = Calendar.getInstance()
        c.set(p1, p2, p3)
        dateEt?.setText(SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(c.time))
    }
//    override fun onDateChanged(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
//        val c = Calendar.getInstance()
//        c.set(p1, p2, p3)
//        dateEt?.setText(SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(c.time))
//    }

    private fun initDataListeners() {
        val expenseListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val plannedTransactions = mutableListOf<PlannedTransaction>()
                dataSnapshot.children.mapNotNullTo(plannedTransactions) {
                    val plannedTransaction = it.getValue<PlannedTransaction>(PlannedTransaction::class.java)
                    plannedTransaction?.key = it.key
                    plannedTransaction
                }
                plannedExpenseAdapter.update(plannedTransactions)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        FirebaseDb.getUserReference("plannedExpense")?.addValueEventListener(expenseListener)
        val incomeListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val plannedTransactions = mutableListOf<PlannedTransaction>()
                dataSnapshot.children.mapNotNullTo(plannedTransactions) {
                    val plannedTransaction = it.getValue<PlannedTransaction>(PlannedTransaction::class.java)
                    plannedTransaction?.key = it.key
                    plannedTransaction
                }
                plannedIncomeAdapter.update(plannedTransactions)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        FirebaseDb.getUserReference("plannedIncome")?.addValueEventListener(incomeListener)
    }

    private var dialogListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { dialog, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                val frequency = freqSpinner?.selectedItem as TransactionFrequency
                val transaction = PlannedTransaction(frequency)
                transaction.price = (dialogContentView?.findViewById<EditText>(R.id.transaction_item_price))?.text.toString().toDouble()
                transaction.type = transactionType
                transaction.description = (dialogContentView?.findViewById<EditText>(R.id.transaction_item_note))?.text.toString()

                if(transactionType == TransactionType.EXPENDITURE) {
                    FirebaseDb().createObject("plannedExpense", transaction)
                } else if (transactionType == TransactionType.INCOME) {
                    FirebaseDb().createObject("plannedIncome", transaction)
                }
            }
            DialogInterface.BUTTON_NEGATIVE -> {
                //do nothing
            }
        }
    }
}
