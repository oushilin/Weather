package com.weather.android.logic.dao

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.weather.android.WeatherApplication
import com.weather.android.logic.model.Place
import java.util.prefs.Preferences

object PlaceDao {
    fun savePlace(place:Place){
        //用SharedPreferences存储
        sharedPreferences().edit {
            putString("place",Gson().toJson(place))
        }
    }

    fun getSavedPlace():Place{
        val placeJson= sharedPreferences().getString("place","")
        return Gson().fromJson(placeJson,Place::class.java)
    }

    fun isPlaceSaved()= sharedPreferences().contains("place")

    private fun sharedPreferences()=WeatherApplication.context.getSharedPreferences("weather",Context.MODE_PRIVATE)
}