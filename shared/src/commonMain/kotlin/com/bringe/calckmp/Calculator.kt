package com.bringe.calckmp

/**
 * A multiplatform calculator that handles basic arithmetic operations.
 */
class Calculator {
    private var currentInput = "0"
    private var currentOperator = ""
    private var firstOperand = 0.0
    private var operationPending = false
    private var lastInputWasOperator = false
    private var expressionText = ""

    /**
     * Adds a digit to the current input.
     */
    fun addDigit(digit: String) {
        if (currentInput == "0" || operationPending) {
            currentInput = digit
            operationPending = false
        } else {
            currentInput += digit
        }

        lastInputWasOperator = false
    }

    /**
     * Sets an operator for calculation.
     */
    fun setOperator(operator: String) {
        if (lastInputWasOperator) {
            // Replace the previous operator
            currentOperator = operator
            return
        }

        if (operationPending) {
            currentOperator = operator
            return
        }

        if (currentOperator.isNotEmpty()) {
            // Perform the pending operation first
            calculate()
        }

        firstOperand = currentInput.toDouble()
        expressionText = currentInput
        currentOperator = operator
        operationPending = true
        lastInputWasOperator = true
    }

    /**
     * Clears all calculator state.
     */
    fun clear() {
        currentInput = "0"
        currentOperator = ""
        firstOperand = 0.0
        operationPending = false
        lastInputWasOperator = false
        expressionText = ""
    }

    /**
     * Performs the calculation based on the current operator.
     */
    fun calculate(): String {
        if (currentOperator.isEmpty()) {
            return currentInput
        }

        val secondOperand = currentInput.toDouble()
        val result = when (currentOperator) {
            "+" -> firstOperand + secondOperand
            "-" -> firstOperand - secondOperand
            "×", "*" -> firstOperand * secondOperand
            "÷", "/" -> if (secondOperand != 0.0) firstOperand / secondOperand else Double.NaN
            else -> secondOperand
        }

        // Store the complete expression before clearing it
        val completeExpression = "$expressionText $currentOperator $currentInput = "

        currentInput = formatResult(result)
        currentOperator = ""
        expressionText = ""  // Clear the expression after calculation
        operationPending = false
        lastInputWasOperator = false

        return currentInput
    }

    /**
     * Formats a double result, removing decimal part if it's a whole number.
     */
    fun formatResult(result: Double): String {
        return if (result == result.toInt().toDouble()) {
            result.toInt().toString()
        } else {
            result.toString()
        }
    }

    /**
     * Adds a decimal point to the current input.
     */
    fun addDecimal() {
        if (operationPending) {
            currentInput = "0."
            operationPending = false
        } else if (!currentInput.contains(".")) {
            currentInput += "."
        }

        lastInputWasOperator = false
    }

    /**
     * Converts the current input to a percentage.
     */
    fun calculatePercent() {
        if (currentInput != "0") {
            val value = currentInput.toDouble() / 100
            currentInput = formatResult(value)
            // If we're in the middle of an expression, we should update the expression
            if (expressionText.isNotEmpty() && currentOperator.isNotEmpty()) {
                // We're in the middle of an operation, so we're modifying the second operand
            } else {
                // We're just modifying the current input
                expressionText = ""
            }
        }
    }

    /**
     * Toggles the sign of the current input.
     */
    fun toggleSign() {
        if (currentInput != "0") {
            val value = currentInput.toDouble() * -1
            currentInput = formatResult(value)
            // If we're in the middle of an expression, we should update the expression
            if (expressionText.isNotEmpty() && currentOperator.isNotEmpty()) {
                // We're in the middle of an operation, so we're modifying the second operand
            } else {
                // We're just modifying the current input
                expressionText = ""
            }
        }
    }

    /**
     * Gets the current display text based on the calculator state.
     */
    fun getDisplayText(): String {
        return if (expressionText.isEmpty()) {
            currentInput
        } else if (lastInputWasOperator) {
            // Only show the expression and operator when an operator was just pressed
            "$expressionText $currentOperator".trim()
        } else {
            "$expressionText $currentOperator $currentInput".trim()
        }
    }

    /**
     * Gets the current input value.
     */
    fun getCurrentInput(): String = currentInput
}