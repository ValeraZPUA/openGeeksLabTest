package com.example.opengeekslabtest.network

import com.example.opengeekslabtest.Constants
import com.example.opengeekslabtest.network.responses.LinkCheckedResponse
import com.example.opengeekslabtest.network.responses.SayHelloResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface Requests {
    @GET
    fun checkUrl(@Url url: String): Call<LinkCheckedResponse>

    @Headers("Domain-Name: ${Constants.KEY_BASE_URL}")
    @GET("/hello")
    fun sayHello(): Call<SayHelloResponse>
}