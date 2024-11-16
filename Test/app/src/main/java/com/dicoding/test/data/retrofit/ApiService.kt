package com.dicoding.aplikasidicodingeventnavigationdanapi.data.retrofit

import com.dicoding.test.data.responese.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active") active: Int = 0, // Menampilkan semua event
        @Query("q") query: String? = null,
        @Query("limit") limit: Int = 40 // default 40
    ): Call<EventResponse>
}