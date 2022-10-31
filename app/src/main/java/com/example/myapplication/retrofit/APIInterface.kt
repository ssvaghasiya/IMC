package com.example.myapplication.retrofit

import com.example.myapplication.pojo.MusicData
import retrofit2.Response
import retrofit2.http.GET

interface APIInterface {

    @GET("albums.json")
    suspend fun getContacts(): Response<MusicData>
}