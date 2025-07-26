package com.bringe.calckmp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private val calculator = Calculator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.resultTextView)

        // Set up click listeners for number buttons
        setNumberButtonClickListeners()

        // Set up click listeners for operator buttons
        setOperatorButtonClickListeners()

        // Set up click listeners for other buttons
        setOtherButtonClickListeners()

        // Initialize display
        updateDisplay()
    }

    private fun setNumberButtonClickListeners() {
        val numberButtons = listOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9
        )

        for (id in numberButtons) {
            findViewById<Button>(id).setOnClickListener { onDigitClick(it) }
        }
    }

    private fun setOperatorButtonClickListeners() {
        val operatorButtons = listOf(
            R.id.buttonPlus, R.id.buttonMinus, R.id.buttonMultiply, R.id.buttonDivide
        )

        for (id in operatorButtons) {
            findViewById<Button>(id).setOnClickListener { onOperatorClick(it) }
        }

        // Handle percent button separately
        findViewById<Button>(R.id.buttonPercent).setOnClickListener { onPercentClick() }
    }

    private fun setOtherButtonClickListeners() {
        findViewById<Button>(R.id.buttonClear).setOnClickListener { onClearClick() }
        findViewById<Button>(R.id.buttonEquals).setOnClickListener { onEqualsClick() }
        findViewById<Button>(R.id.buttonDecimal).setOnClickListener { onDecimalClick() }
        findViewById<Button>(R.id.buttonToggleSign).setOnClickListener { onToggleSignClick() }
    }

    private fun onDigitClick(view: View) {
        val digit = (view as Button).text.toString()
        calculator.addDigit(digit)
        updateDisplay()
    }

    private fun onOperatorClick(view: View) {
        val operator = getOperatorFromButton(view as Button)
        calculator.setOperator(operator)
        updateDisplay()
    }

    private fun getOperatorFromButton(button: Button): String {
        return when (button.id) {
            R.id.buttonPlus -> "+"
            R.id.buttonMinus -> "-"
            R.id.buttonMultiply -> "×"
            R.id.buttonDivide -> "÷"
            else -> ""
        }
    }

    private fun onClearClick() {
        calculator.clear()
        updateDisplay()
    }

    private fun onEqualsClick() {
        calculator.calculate()
        updateDisplay()
    }

    private fun onDecimalClick() {
        calculator.addDecimal()
        updateDisplay()
    }

    private fun onPercentClick() {
        calculator.calculatePercent()
        updateDisplay()
    }

    private fun onToggleSignClick() {
        calculator.toggleSign()
        updateDisplay()
    }

    private fun updateDisplay() {
        resultTextView.text = calculator.getDisplayText()
    }
}
