package com.zaketn.calculator

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var previousNumber: Float = 0f
    private var activeOperationButtonId: Int = 0
    private var newOutput: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun display(view: View) {
        val clickedButton: Button = view as Button
        val output: TextView = findViewById(R.id.textView_output)

        if (activeOperationButtonId != 0) {
            resetActiveStyle()
        }

        if(output.text.length >= 9) return

        if ((getOutput() == 0f) || newOutput) {
            output.text = clickedButton.text.toString()
        } else {
            output.text = output.text.toString() + clickedButton.text.toString()
        }

        if (activeOperationButtonId != 0) {
            resetActiveStyle()
        }
        newOutput = false
    }

    fun clearOutput(view: View) {
        previousNumber = 0f
        activeOperationButtonId = 0
        setOutput("0")
    }

    fun opposite(view: View) {
        val result = -getOutput()
        setOutput(result.toString())
    }

    fun module(view: View) {
        val result = getOutput() / 10
        setOutput(result.toString())
    }

    fun operation(view: View) {
        val currentButton: Button = findViewById(view.id)
        applyActiveStyle(currentButton)
        previousNumber = getOutput()
        activeOperationButtonId = view.id
        newOutput = true
    }

    fun calculate(view: View) {
        if(activeOperationButtonId == 0) return

        val operation: String = findViewById<Button>(activeOperationButtonId).text.toString()
        if (!validateResult(operation)) return

        var result = 0f
        when (operation) {
            "+" -> result = previousNumber + getOutput()
            "−" -> result = previousNumber - getOutput()
            "×" -> result = previousNumber * getOutput()
            "÷" -> result = previousNumber / getOutput()
        }
        setOutput(result.toString())
        activeOperationButtonId = 0
        newOutput = true
    }

    private fun setOutput(text: String) {
        val textView: TextView = findViewById(R.id.textView_output)

        val result = text.toFloatOrNull()
        if (result == null) {
            textView.text = text
        } else {
            if(result.toString().length >= 9) "%.7f".format(result)
            if (result % 1 == 0f) textView.text = result.toInt().toString()
            else textView.text = result.toString()
        }
    }

    private fun getOutput(): Float {
        val textView: TextView = findViewById(R.id.textView_output)
        val output = textView.text.toString().toFloatOrNull() ?: return 0f
        return textView.text.toString().toFloat()
    }

    private fun applyActiveStyle(operationButton: Button) {
        operationButton.setBackgroundColor(Color.WHITE)
        operationButton.setTextColor(Color.parseColor("#f2a33b"))
    }

    private fun resetActiveStyle() {
        val operationButton: Button = findViewById(activeOperationButtonId)
        operationButton.setTextColor(Color.WHITE)
        operationButton.setBackgroundColor(Color.parseColor("#f2a33b"))
    }

    private fun validateResult(operation: String): Boolean {
        if (activeOperationButtonId == 0) return false
        else if (operation == "÷" && getOutput() == 0f) {
            setOutput("Ошибка!")
            return false
        }

        return true
    }
}