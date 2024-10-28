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

        val currencies = listOf("EUR", "USD", "JPY", "VND")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        baseCurrencySpinner.adapter = adapter
        targetCurrencySpinner.adapter = adapter

        amountInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val amount = amountInput.text.toString().toDouble()
                val convertedAmount = amount * currency
                amountOutput.setText(convertedAmount.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

//        amountOutput.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                val amount = amountOutput.text.toString().toDouble()
//                val convertedAmount = amount * 1/currency
//                amountInput.setText(convertedAmount.toString())
//            }
//            override fun afterTextChanged(s: Editable?) {}
//        })

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
        if(amountInput.text.isNotEmpty()){
            when(baseCurrency){
                "VND" -> {
                    when(targetCurrency){
                        "EUR" -> {
                            RetrofitInstance.apiService.getCurrencyDataFromVndToEur().enqueue(object : Callback<ApiResponse> {
                                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                                    if (response.isSuccessful) {
                                        val apiResponse = response.body()
                                        apiResponse?.let {
                                            val lastUpdatedAt = it.meta.lastUpdatedAt
                                            currency = it.data["EUR"]?.value ?: 1.0
                                            val convertedAmount = amountInput.text.toString().toDouble() * currency
                                            amountOutput.setText(convertedAmount.toString())
                                            Log.d("API Response", "Last Updated At: $lastUpdatedAt, Converted Value: $currency")
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
                                            val lastUpdatedAt = it.meta.lastUpdatedAt
                                            currency = it.data["USD"]?.value ?: 1.0
                                            val convertedAmount = amountInput.text.toString().toDouble() * currency
                                            amountOutput.setText(convertedAmount.toString())
                                            Log.d("API Response", "Last Updated At: $lastUpdatedAt, Converted Value: $currency")
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
                                            val lastUpdatedAt = it.meta.lastUpdatedAt
                                            currency = it.data["JPY"]?.value ?: 1.0
                                            val convertedAmount = amountInput.text.toString().toDouble() * currency
                                            amountOutput.setText(convertedAmount.toString())
                                            Log.d("API Response", "Last Updated At: $lastUpdatedAt, Converted Value: $currency")
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
                "EUR" -> {
                    when(targetCurrency){
                        "VND" -> {
                            RetrofitInstance.apiService.getCurrencyDataFromEurToVnd().enqueue(object : Callback<ApiResponse> {
                                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                                    if (response.isSuccessful) {
                                        val apiResponse = response.body()
                                        apiResponse?.let {
                                            val lastUpdatedAt = it.meta.lastUpdatedAt
                                            currency = it.data["VND"]?.value ?: 1.0
                                            val convertedAmount = amountInput.text.toString().toDouble() * currency
                                            amountOutput.setText(convertedAmount.toString())
                                            Log.d("API Response", "Last Updated At: $lastUpdatedAt, Converted Value: $currency")
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
                "USD" -> {
                    when(targetCurrency){
                        "VND" -> {
                            RetrofitInstance.apiService.getCurrencyDataFromUsdToVnd().enqueue(object : Callback<ApiResponse> {
                                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                                    if (response.isSuccessful) {
                                        val apiResponse = response.body()
                                        apiResponse?.let {
                                            val lastUpdatedAt = it.meta.lastUpdatedAt
                                            currency = it.data["VND"]?.value ?: 1.0
                                            val convertedAmount = amountInput.text.toString().toDouble() * currency
                                            amountOutput.setText(convertedAmount.toString())
                                            Log.d("API Response", "Last Updated At: $lastUpdatedAt, Converted Value: $currency")
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
                "JPY" -> {
                    when(targetCurrency){
                        "VND" -> {
                            RetrofitInstance.apiService.getCurrencyDataFromJpyToVnd().enqueue(object : Callback<ApiResponse> {
                                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                                    if (response.isSuccessful) {
                                        val apiResponse = response.body()
                                        apiResponse?.let {
                                            val lastUpdatedAt = it.meta.lastUpdatedAt
                                            currency = it.data["VND"]?.value ?: 1.0
                                            val convertedAmount = amountInput.text.toString().toDouble() * currency
                                            amountOutput.setText(convertedAmount.toString())
                                            Log.d("API Response", "Last Updated At: $lastUpdatedAt, Converted Value: $currency")
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
                else -> currency = 0.86
            }
        }
    }
}