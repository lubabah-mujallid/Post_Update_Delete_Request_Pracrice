package com.example.post_request_pracrice

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body


interface APIInterface {

    @GET("/test/")
    fun getPerson(): Call<List<Person.PersonDetails>>

    @POST("/test/")
    fun addPerson(@Body person : Person.PersonDetails): Call<List<Person.PersonDetails>>

}