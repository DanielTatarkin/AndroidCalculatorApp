package com.danieltatarkin.calculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"

class MainActivity : AppCompatActivity() {

    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)


        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()

            if (op == "Clear"){
                newNumber.setText("")
                result.setText("")
                pendingOperation = ""
                operation.text = ""
                operand1 = null
                return@OnClickListener
            }

            if (op == "NEG"){
                if (newNumber.text.toString().isEmpty()){
                    newNumber.setText("-")
                } else if (newNumber.text.toString() == "-"){
                    newNumber.setText("")
                } else if (newNumber.text.toString().isNotEmpty()) {
                    var checkText = newNumber.text.toString()
                    if (checkText.startsWith("-")){
                        newNumber.text.delete(0,1)
                    } else newNumber.text.insert(0,"-")
                }
                return@OnClickListener
            }

            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }

            pendingOperation = op
            operation.text = pendingOperation
        }


        buttonEquals.setOnClickListener(opListener)
        buttonDiv.setOnClickListener(opListener)
        buttonMul.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
        buttonClear.setOnClickListener(opListener)
        buttonNeg.setOnClickListener(opListener)


    }


    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        } else {


            if (pendingOperation == "=") {
                pendingOperation = operation
            }

            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN       // handle attempt to divide by zero
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(STATE_PENDING_OPERATION,pendingOperation)
        if (operand1 != null) outState?.putDouble(STATE_OPERAND1, operand1!!)           //to fix
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        operation?.text = savedInstanceState?.getString(STATE_PENDING_OPERATION)
        pendingOperation = savedInstanceState?.getString(STATE_PENDING_OPERATION)!!
        if (operand1 != null) operand1 = savedInstanceState.getDouble(STATE_OPERAND1)   //to fix

    }
}