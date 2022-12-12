package com.weather.android.logic.network

import android.telecom.Call
import com.weather.android.WeatherApplication
import com.weather.android.logic.model.PlaceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    @GET("v2/place?&token=${WeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query:String): retrofit2.Call<PlaceResponse>

}