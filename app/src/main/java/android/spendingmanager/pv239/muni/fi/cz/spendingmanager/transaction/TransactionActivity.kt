package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.transaction

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import me.rishabhkhanna.customtogglebutton.CustomToggleButton

class TransactionActivity : AppCompatActivity() {

    private var incomeBtn : CustomToggleButton? = null
    private var expenseBtn : CustomToggleButton? = null
    private var transferBtn : CustomToggleButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        //initElements()
    }

    private fun initElements() {
        incomeBtn = findViewById(R.id.transaction_income_btn)
        expenseBtn = findViewById(R.id.transaction_expense_btn)
        transferBtn = findViewById(R.id.transaction_transfer_btn)

        incomeBtn?.setOnTouchListener { view, motionEvent -> handleActionButtons(view as CustomToggleButton, motionEvent) }
        expenseBtn?.setOnTouchListener { view, motionEvent -> handleActionButtons(view as CustomToggleButton, motionEvent) }
        transferBtn?.setOnTouchListener { view, motionEvent -> handleActionButtons(view as CustomToggleButton, motionEvent) }
    }

    private fun handleActionButtons (view : CustomToggleButton, motionEvent : MotionEvent) : Boolean {
        Toast.makeText(this, "Touched", Toast.LENGTH_LONG).show()

        if(motionEvent.action != MotionEvent.ACTION_DOWN) {
            return false
        }

        if(!view.isChecked) {
            view.isChecked = !view.isChecked
        }

        when(view.id) {
            R.id.transaction_income_btn -> setCheckedState(view.isChecked, true, true)
            R.id.transaction_expense_btn -> setCheckedState(true, view.isChecked, true)
            R.id.transaction_transfer_btn -> setCheckedState(true, true, view.isChecked)
        }

        return false
    }

    private fun setCheckedState(incomeChecked: Boolean, expenseChecked: Boolean, transferChecked: Boolean) {
        incomeBtn?.isChecked = incomeChecked
        expenseBtn?.isChecked = expenseChecked
        transferBtn?.isChecked = transferChecked
    }
}
