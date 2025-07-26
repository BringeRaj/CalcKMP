package com.bringe.calckmp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private var currentInput = "0"
    private var currentOperator = ""
    private var firstOperand = 0.0
    private var operationPending = false
    private var lastInputWasOperator = false
    private var expressionText = ""

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

        if (currentInput == "0" || operationPending) {
            currentInput = digit
            operationPending = false
        } else {
            currentInput += digit
        }

        lastInputWasOperator = false
        updateDisplay()
    }

    private fun onOperatorClick(view: View) {
        if (lastInputWasOperator) {
            // Replace the previous operator
            currentOperator = getOperatorFromButton(view as Button)
            updateDisplay()
            return
        }

        if (operationPending) {
            currentOperator = getOperatorFromButton(view as Button)
            updateDisplay()
            return
        }

        if (currentOperator.isNotEmpty()) {
            // Perform the pending operation first
            onEqualsClick()
        }

        firstOperand = currentInput.toDouble()
        expressionText = currentInput
        currentOperator = getOperatorFromButton(view as Button)
        operationPending = true
        lastInputWasOperator = true
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
        currentInput = "0"
        currentOperator = ""
        firstOperand = 0.0
        operationPending = false
        lastInputWasOperator = false
        expressionText = ""
        updateDisplay()
    }

    private fun onEqualsClick() {
        if (currentOperator.isEmpty()) {
            return
        }

        val secondOperand = currentInput.toDouble()
        val result = when (currentOperator) {
            "+" -> firstOperand + secondOperand
            "-" -> firstOperand - secondOperand
            "×" -> firstOperand * secondOperand
            "÷" -> if (secondOperand != 0.0) firstOperand / secondOperand else Double.NaN
            else -> secondOperand
        }

        // Store the complete expression before clearing it
        val completeExpression = "$expressionText $currentOperator $currentInput = "

        currentInput = formatResult(result)
        currentOperator = ""
        expressionText = ""  // Clear the expression after calculation
        operationPending = false
        lastInputWasOperator = false

        // Show the complete expression with the result
        resultTextView.text = currentInput
    }

    private fun formatResult(result: Double): String {
        return if (result == result.toInt().toDouble()) {
            result.toInt().toString()
        } else {
            result.toString()
        }
    }

    private fun onDecimalClick() {
        if (operationPending) {
            currentInput = "0."
            operationPending = false
        } else if (!currentInput.contains(".")) {
            currentInput += "."
        }

        lastInputWasOperator = false
        updateDisplay()
    }

    private fun onPercentClick() {
        if (currentInput != "0") {
            val value = currentInput.toDouble() / 100
            currentInput = formatResult(value)
            // If we're in the middle of an expression, we should update the expression
            if (expressionText.isNotEmpty() && currentOperator.isNotEmpty()) {
                // We're in the middle of an operation, so we're modifying the second operand
                updateDisplay()
            } else {
                // We're just modifying the current input
                expressionText = ""
                updateDisplay()
            }
        }
    }

    private fun onToggleSignClick() {
        // This method toggles the sign of the current number (positive/negative)
        // The button is labeled "+/-" in the UI
        if (currentInput != "0") {
            val value = currentInput.toDouble() * -1
            currentInput = formatResult(value)
            // If we're in the middle of an expression, we should update the expression
            if (expressionText.isNotEmpty() && currentOperator.isNotEmpty()) {
                // We're in the middle of an operation, so we're modifying the second operand
                updateDisplay()
            } else {
                // We're just modifying the current input
                expressionText = ""
                updateDisplay()
            }
        }
    }

    private fun updateDisplay() {
        if (expressionText.isEmpty()) {
            resultTextView.text = currentInput
        } else if (lastInputWasOperator) {
            // Only show the expression and operator when an operator was just pressed
            resultTextView.text = "$expressionText $currentOperator".trim()
        } else {
            resultTextView.text = "$expressionText $currentOperator $currentInput".trim()
        }
    }
}
