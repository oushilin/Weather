package com.weather.android.logic

import androidx.lifecycle.liveData
import com.weather.android.logic.model.Place
import com.weather.android.logic.network.WeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.RuntimeException

object Repository  {
    fun searchPlaces(query: String)= liveData(Dispatchers.IO){
        val result=try{
            val placeResponse=WeatherNetwork.searchPlacee(query)
            if (placeResponse.status=="ok"){
                val  places=placeResponse.places
                Result.success(places)
            }
            else{
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }catch (e:Exception){
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }

}