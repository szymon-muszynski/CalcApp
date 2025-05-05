package com.example.calcapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SimpleCalcActivity : AppCompatActivity() {

    private lateinit var tvDisplay: TextView
    private lateinit var btnSign: Button
    private lateinit var btnCE: Button

    private var currentInput = ""
    private var operator: String? = null
    private var firstNumber: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_calc)

        tvDisplay = findViewById(R.id.tvDisplay)
        btnSign = findViewById(R.id.btnSign)
        btnCE = findViewById(R.id.btnCE)

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
        findViewById<Button>(R.id.btnDot).setOnClickListener { addDecimalPoint() }

        findViewById<Button>(R.id.btnEqual).setOnClickListener { calculateResult() }
        findViewById<Button>(R.id.btnAC).setOnClickListener { resetCalculator() }

        btnSign.setOnClickListener {
            if (currentInput.isNotEmpty()) {
                if (currentInput.startsWith("-")) {
                    currentInput = currentInput.substring(1) // Usuwamy "-"
                } else {
                    currentInput = "-" + currentInput // Dodajemy "-"
                }
                tvDisplay.text = currentInput
            }
        }

        btnCE.setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput = ""
                tvDisplay.text = "0"
            } else {
                resetCalculator()
            }
        }
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
        val secondNumber = currentInput.toDoubleOrNull()

        if (num1 != null && secondNumber != null) {
            if (operator == "/" && secondNumber == 0.0) {
                showToast("Błąd: Dzielenie przez 0!")
                resetCalculator()
                return
            }

            val result = when (operator) {
                "+" -> num1 + secondNumber
                "-" -> num1 - secondNumber
                "*" -> num1 * secondNumber
                "/" -> num1 / secondNumber
                else -> 0.0
            }

            tvDisplay.text = result.toString()
            currentInput = result.toString()
            firstNumber = null
        }
    }

    private fun resetCalculator() {
        currentInput = ""
        firstNumber = null
        operator = null
        tvDisplay.text = "0"
    }

    private fun addDecimalPoint() {
        if (!currentInput.contains(".")) {
            if (currentInput.isEmpty()) {
                currentInput = "0." // Jeśli użytkownik zaczyna od ".", to dodajemy "0."
            } else {
                currentInput += "."
            }
            tvDisplay.text = currentInput
        }
        else {
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