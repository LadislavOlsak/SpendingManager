package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.planning

import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.firebase.FirebaseDb
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.DateTimePickerDialog
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction.TransactionType
import android.support.v7.widget.AppCompatSpinner
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.planned_transaction_expenses.*
import kotlinx.android.synthetic.main.planned_transaction_income.*
import java.text.SimpleDateFormat
import java.util.*

class PlannedTransactionsActivity : AppCompatActivity() {

    private var dialogContentView : View? = null
    private var freqSpinner : AppCompatSpinner? = null
    private var dateTv : TextView? = null
    private var transactionType = TransactionType.EXPENDITURE

    private var plannedExpenseAdapter : PlannedTransactionAdapter = PlannedTransactionAdapter(this, mutableListOf())
    private var plannedIncomeAdapter : PlannedTransactionAdapter = PlannedTransactionAdapter(this, mutableListOf())

    private var dialog : AlertDialog? = null
    private var singleDateAndTimePickerDialog : SingleDateAndTimePickerDialog? = null

    companion object { var isDialogOpened = false }

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
        dialog = AlertDialog.Builder(this)
                                .setPositiveButton("Save", dialogListener)
                                .setNegativeButton("Cancel", dialogListener)
                                .create()

        dialogContentView = this.layoutInflater.inflate(R.layout.transaction_item, null) as View
        dialog?.setView(dialogContentView)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val frequencyLayout = dialogContentView?.findViewById(R.id.transaction_item_frequency_layout) as LinearLayout
        frequencyLayout.visibility = View.VISIBLE

        freqSpinner = dialogContentView?.findViewById(R.id.limits_item_frequency) as AppCompatSpinner
        freqSpinner?.adapter = ArrayAdapter<TransactionFrequency>(this, android.R.layout.simple_list_item_1, TransactionFrequency.values())

        dateTv = dialogContentView?.findViewById(R.id.transaction_item_date) as TextView
        setDateAndTime(Calendar.getInstance().time)

        val dateButton = dialogContentView?.findViewById<Button>(R.id.transaction_item_time_btn)
        dateButton?.setOnClickListener {
            hideDialog(dialog)
            singleDateAndTimePickerDialog = DateTimePickerDialog().create(this)
            singleDateAndTimePickerDialog?.setListener{ date ->
                setDateAndTime(date)
                openDialog(dialog)
            }?.display()
        }

        this.transactionType = tranType
        openDialog(dialog)
    }

    private fun openDialog(dialog : AlertDialog?) {
        isDialogOpened = true
        dialog?.show()
    }

    private fun hideDialog(dialog : AlertDialog?) {
        isDialogOpened = false
        dialog?.hide()
    }

    override fun onBackPressed() {
        if(!isDialogOpened) {
            singleDateAndTimePickerDialog?.close()
            openDialog(dialog)
        } else {
            super.onBackPressed()
        }
    }

    private fun setDateAndTime(date : Date) {
        dateTv?.text = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(date)
    }

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

    private var dialogListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                val frequency = freqSpinner?.selectedItem as TransactionFrequency
                val transaction = PlannedTransaction(frequency)
                transaction.price = (dialogContentView?.findViewById<EditText>(R.id.transaction_item_price))?.text.toString().toDouble()
                transaction.type = transactionType
                transaction.description = (dialogContentView?.findViewById<EditText>(R.id.transaction_item_note))?.text.toString()
                transaction.datetime = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).parse(dateTv?.text.toString())

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
