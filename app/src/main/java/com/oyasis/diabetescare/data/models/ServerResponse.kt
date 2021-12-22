package com.oyasis.diabetescare.data.models

data class ServerResponse(
    val message: String,
    val error: String,
    val data: User,
    val status_code: Int
)