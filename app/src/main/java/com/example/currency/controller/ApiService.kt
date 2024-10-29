package com.example.currency.controller

import com.example.currency.model.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ApiService {
    @GET("v3/latest?apikey=cur_live_7hwRQttMhJx5gtBVeUoJHeWbR2ebrn1dsQKOSTfP&currencies=EUR&base_currency=VND")
    fun getCurrencyDataFromVndToEur(): Call<ApiResponse>

    @GET("v3/latest?apikey=cur_live_7hwRQttMhJx5gtBVeUoJHeWbR2ebrn1dsQKOSTfP&currencies=USD&base_currency=VND")
    fun getCurrencyDataFromVndToUsd(): Call<ApiResponse>

    @GET("v3/latest?apikey=cur_live_7hwRQttMhJx5gtBVeUoJHeWbR2ebrn1dsQKOSTfP&currencies=JPY&base_currency=VND")
    fun getCurrencyDataFromVndToJpy(): Call<ApiResponse>
}

object RetrofitInstance {
    private const val BASE_URL = "https://api.currencyapi.com/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}