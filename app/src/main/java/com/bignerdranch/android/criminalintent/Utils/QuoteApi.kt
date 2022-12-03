package com.bignerdranch.android.criminalintent.Utils

import retrofit2.Call
import retrofit2.http.GET

interface QuoteApi {

    @GET("/")
    fun fetchContents() : Call<String>
    //JSON
//     https://stackoverflow.com/questions/24154917/retrofit-expected-begin-object-but-was-begin-array
    @GET(
        "photos/?client_id=ipLjiRmWcJ-jWn5uG8UhibNGiFgHxTVE_KeHHb8Oo3M"
    )
    fun fetchQuote(): Call<String>
//    fun fetchPhotosInfo(): Call<List<GalleryItem>>
}