package com.example.calcapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.*

class AdvancedCalcActivity : AppCompatActivity() {

    private lateinit var tvDisplay: TextView
    private var currentInput = ""
    private var operator: String? = null
    private var firstNumber: Double? = null
    private var isNewOperation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced_calc)

        tvDisplay = findViewById(R.id.tvDisplay)

        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                currentInput += (it as Button).text
                tvDisplay.text = currentInput
            }
        }

        findViewById<Button>(R.id.btnAdd).setOnClickListener { setOperator("+") }
        findViewById<Button>(R.id.btnSub).setOnClickListener { setOperator("-") }
        findViewById<Button>(R.id.btnMul).setOnClickListener { setOperator("*") }
        findViewById<Button>(R.id.btnDiv).setOnClickListener { setOperator("/") }
        findViewById<Button>(R.id.btnXtoY).setOnClickListener { setOperator("^") }
        findViewById<Button>(R.id.btnPercent).setOnClickListener { setOperator("%") }

        findViewById<Button>(R.id.btnDot).setOnClickListener { addDecimalPoint() }
        findViewById<Button>(R.id.btnEqual).setOnClickListener { calculateResult() }

        findViewById<Button>(R.id.btnSin).setOnClickListener { calculateUnary("sin") }
        findViewById<Button>(R.id.btnCos).setOnClickListener { calculateUnary("cos") }
        findViewById<Button>(R.id.btnTan).setOnClickListener { calculateUnary("tan") }
        findViewById<Button>(R.id.btnLn).setOnClickListener { calculateUnary("ln") }
        findViewById<Button>(R.id.btnLog).setOnClickListener { calculateUnary("log") }
        findViewById<Button>(R.id.btnSqrt).setOnClickListener { calculateUnary("sqrt") }
        findViewById<Button>(R.id.btnXto2).setOnClickListener { calculateUnary("x^2") }

        findViewById<Button>(R.id.btnAC).setOnClickListener { resetCalculator() }
        findViewById<Button>(R.id.btnCE).setOnClickListener { clearEntry() }

        findViewById<Button>(R.id.btnSign).setOnClickListener { toggleSign() }

    }

    private fun setOperator(op: String) {
        if (currentInput.isEmpty()) {
            showToast("Błąd: Wpisz liczbę przed operatorem!")
            return
        }
        firstNumber = currentInput.toDoubleOrNull()
        currentInput = ""
        operator = op
    }

    private fun calculateResult() {
        if (firstNumber == null || currentInput.isEmpty()) {
            showToast("Błąd: Niepoprawne dane! Wpisz pełne równanie.")
            return
        }

        val num1 = firstNumber
        val num2 = currentInput.toDoubleOrNull()

        if (num1 != null && num2 != null) {
            val result = when (operator) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "*" -> num1 * num2
                "/" -> if (num2 != 0.0) num1 / num2 else {
                    showToast("Błąd: Dzielenie przez 0!")
                    resetCalculator()
                    return
                }

                "^" -> num1.pow(num2)
                "%" -> (num1 / 100.0) * num2

                else -> 0.0
            }

            tvDisplay.text = result.toString()
            currentInput = result.toString()
            firstNumber = null
            isNewOperation = true
        }
    }

    private fun calculateUnary(op: String) {
        if (currentInput.isEmpty()) {
            showToast("Błąd: Wprowadź liczbę!")
            return
        }

        val num = currentInput.toDoubleOrNull() ?: return

        val result = when (op) {
            "sin" -> sin(Math.toRadians(num))
            "cos" -> cos(Math.toRadians(num))
            "tan" -> tan(Math.toRadians(num))
            "ln" -> if (num > 0) ln(num) else {
                showToast("Błąd: logarytm z liczby ≤ 0!")
                return
            }

            "log" -> if (num > 0) log10(num) else {
                showToast("Błąd: logarytm z liczby ≤ 0!")
                return
            }

            "sqrt" -> if (num >= 0) sqrt(num) else {
                showToast("Błąd: Pierwiastek z liczby ujemnej!")
                return
            }

            "x^2" -> num.pow(2)
            else -> 0.0
        }

        tvDisplay.text = result.toString()
        currentInput = result.toString()
        isNewOperation = true
    }

    private fun resetCalculator() {
        currentInput = ""
        firstNumber = null
        operator = null
        tvDisplay.text = "0"
        isNewOperation = true
    }

    private fun clearEntry() {
        if (currentInput.isNotEmpty()) {
            currentInput = ""
            tvDisplay.text = "0"
        } else {
            resetCalculator()
        }
    }

    private fun toggleSign() {
        if (currentInput.isNotEmpty()) {
            if (currentInput.startsWith("-")) {
                currentInput = currentInput.substring(1) // Usuwamy "-"
            } else {
                currentInput = "-" + currentInput // Dodajemy "-"
            }
            tvDisplay.text = currentInput
        }
    }

    private fun addDecimalPoint() {
        if (!currentInput.contains(".")) {

            if (currentInput.isEmpty()) {
                currentInput = "0." // Jeśli użytkownik zaczyna od ".", to dodajemy "0."
            } else {
                currentInput += "."
            }
            tvDisplay.text = currentInput
        } else {
            showToast("Błąd: Już wprowadzono kropkę!")
            return
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("currentInput", currentInput)
        outState.putDouble("firstNumber", firstNumber ?: Double.NaN)
        outState.putString("operator", operator)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentInput = savedInstanceState.getString("currentInput", "")
        firstNumber = savedInstanceState.getDouble("firstNumber", Double.NaN).takeIf { !it.isNaN() }
        operator = savedInstanceState.getString("operator")

        tvDisplay.text = currentInput
    }
}