package com.example.post_request_pracrice

import retrofit2.Call
import retrofit2.http.*


interface APIInterface {

    @GET("/test/")
    fun getPerson(): Call<List<Person.PersonDetails>>

    @POST("/test/")
    fun addPerson(@Body person : Person.PersonDetails): Call<List<Person.PersonDetails>>

    @PUT("/test/{id}")
    fun updatePerson(@Path("id") id: Int, @Body person : Person.PersonDetails): Call<Person.PersonDetails>

    @DELETE("/test/{id}")
    fun deletePerson(@Path("id") id: Int): Call<Void>
}