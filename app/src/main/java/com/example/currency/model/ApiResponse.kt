package com.example.currency.model

data class ApiResponse(
    val meta: Meta,
    val data: Map<String, Currency>
)

data class Meta(
    val lastUpdatedAt: String
)

data class Currency(
    val code: String,
    val value: Double
)

