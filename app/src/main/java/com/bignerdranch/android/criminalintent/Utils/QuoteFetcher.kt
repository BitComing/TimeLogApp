package com.bignerdranch.android.criminalintent.Utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuoteFetcher {
    private val quoteApi : QuoteApi
    init{
        val retrofit : Retrofit = Retrofit.Builder()
            //.baseUrl("https://unsplash.com/")
            .baseUrl("https://api.unsplash.com/")
            //.addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        quoteApi  = retrofit.create(QuoteApi::class.java)
    }
}