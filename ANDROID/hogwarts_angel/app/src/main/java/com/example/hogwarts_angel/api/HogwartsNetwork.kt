package com.example.hogwarts_angel.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HogwartsNetwork {
    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Parametros.url+ ":" + Parametros.puerto)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HogwartsAPI::class.java)
    }
}