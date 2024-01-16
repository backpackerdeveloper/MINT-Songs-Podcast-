package com.shubhamtripz.mint

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiSearchService {

    @GET("exec?action=getUsers")
    fun getUsersByTitle(@Query("title") title: String): Call<List<Song>>

    companion object {
        const val BASE_URL = "https://script.google.com/macros/s/AKfycbwDF1ZUyLvTZi7z5pF_P6pqu6oDzqxuvCjKMNJCN8U797hnNH2csUzer9hepmKQb8CPLQ/"

        fun create(): ApiSearchService {
            val retrofit = retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiSearchService::class.java)
        }
    }
}
