package com.example.currency

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.currency.controller.RetrofitInstance
import com.example.currency.model.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var amountInput: EditText
    private lateinit var baseCurrencySpinner: Spinner
    private lateinit var targetCurrencySpinner: Spinner
    private lateinit var amountOutput: EditText
    private var currency = 1.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        amountInput = findViewById(R.id.amountInput)
        baseCurrencySpinner = findViewById(R.id.baseCurrencySpinner)
        targetCurrencySpinner = findViewById(R.id.targetCurrencySpinner)
        amountOutput = findViewById(R.id.amountOutput)

        var isTextChanging = false
        val currencies = listOf("EUR", "USD", "JPY", "VND")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        baseCurrencySpinner.adapter = adapter
        targetCurrencySpinner.adapter = adapter

        amountInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isTextChanging) {
                    isTextChanging = true
                    updateAmountOutput()
                    isTextChanging = false
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        amountOutput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isTextChanging) {
                    isTextChanging = true
                    val amount = amountOutput.text.toString().toDoubleOrNull() ?: 0.0
                    val convertedAmount = amount / currency
                    amountInput.setText(String.format(Locale.ENGLISH,"%.0f", convertedAmount))
                    isTextChanging = false
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        baseCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                fetchCurrencyData()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        targetCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                fetchCurrencyData()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

    }

    private fun fetchCurrencyData() {
        val baseCurrency = baseCurrencySpinner.selectedItem.toString()
        val targetCurrency = targetCurrencySpinner.selectedItem.toString()
        when(baseCurrency){
            "VND" -> {
                when(targetCurrency){
                    "EUR" -> {
                        RetrofitInstance.apiService.getCurrencyDataFromVndToEur().enqueue(object : Callback<ApiResponse> {
                            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                                if (response.isSuccessful) {
                                    val apiResponse = response.body()
                                    apiResponse?.let {
                                        currency = it.data["EUR"]?.value ?: 1.0
                                        updateAmountOutput()
                                    }
                                } else {
                                    Log.e("API Error", "Response Code: ${response.code()}")
                                }
                            }

                            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                                Log.e("API Failure", "Error: ${t.message}")
                            }
                        })
                    }
                    "USD" -> {
                        RetrofitInstance.apiService.getCurrencyDataFromVndToUsd().enqueue(object : Callback<ApiResponse> {
                            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                                if (response.isSuccessful) {
                                    val apiResponse = response.body()
                                    apiResponse?.let {
                                        currency = it.data["USD"]?.value ?: 1.0
                                        updateAmountOutput()
                                    }
                                } else {
                                    Log.e("API Error", "Response Code: ${response.code()}")
                                }
                            }

                            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                                Log.e("API Failure", "Error: ${t.message}")
                            }
                        })
                    }
                    "JPY" -> {
                        RetrofitInstance.apiService.getCurrencyDataFromVndToJpy().enqueue(object : Callback<ApiResponse> {
                            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                                if (response.isSuccessful) {
                                    val apiResponse = response.body()
                                    apiResponse?.let {
                                        currency = it.data["JPY"]?.value ?: 1.0
                                        updateAmountOutput()
                                    }
                                } else {
                                    Log.e("API Error", "Response Code: ${response.code()}")
                                }
                            }

                            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                                Log.e("API Failure", "Error: ${t.message}")
                            }
                        })
                    }
                }
            }
            targetCurrency -> {
                currency = 1.0
                updateAmountOutput()
            }//same currency
            else -> {
                currency = 1.1234
                updateAmountOutput()
            }//example value
        }
    }

    private fun updateAmountOutput() {
        val amount = amountInput.text.toString().toDoubleOrNull() ?: 0.0
        val convertedAmount = amount * currency
        amountOutput.setText(String.format(Locale.ENGLISH,"%.2f", convertedAmount))
    }
}