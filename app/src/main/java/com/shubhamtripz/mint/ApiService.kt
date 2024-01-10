package com.shubhamtripz.mint

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("exec?action=getUsers")
    fun getUsers(): Call<List<Song>>
}
