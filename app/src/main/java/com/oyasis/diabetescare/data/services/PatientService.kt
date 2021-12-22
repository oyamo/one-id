package com.oyasis.diabetescare.data.services

import com.oyasis.diabetescare.data.models.ServerResponse
import com.oyasis.diabetescare.data.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

public interface PatientService {
    @POST("patients")
    @Headers("Content-Type: application/json")
    fun addUser(@Body user: User): Call<ServerResponse>
}