package com.weather.android.logic

import androidx.lifecycle.liveData
import com.weather.android.logic.dao.PlaceDao
import com.weather.android.logic.model.Place
import com.weather.android.logic.model.Weather
import com.weather.android.logic.network.WeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository  {
    //搜索地址的仓库层代码
    fun searchPlaces(query: String)= liveData(Dispatchers.IO){
        val result=try{
            val placeResponse=WeatherNetwork.searchPlacee(query)
            if (placeResponse.status=="ok"){
                val  places=placeResponse.places
                Result.success(places)
            } else{
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }catch (e:Exception){
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }

    //更好的天气仓库层代码，增加了封装
    fun refreshWeather(lng: String,lat: String,placeName:String)= fire(Dispatchers.IO){
        coroutineScope {
            val deferredRealtime=async {
                WeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily=async {
                WeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse=deferredRealtime.await()
            val dailyResponse=deferredDaily.await()
            if (realtimeResponse.status=="ok" && dailyResponse.status=="ok"){
                val weather= Weather(realtimeResponse.result.realtime,dailyResponse.result.daily)
                Result.success(weather)
            }else{
                Result.failure(
                    RuntimeException(
                        "realtime response is ${realtimeResponse.status}"+
                                "daily response is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    //定义了fire()方法
    private fun <T> fire(context:CoroutineContext,block:suspend () -> Result<T>)=
            liveData<Result<T>>(context) {
        val result=try {
            block()
        }catch (e:Exception){
            Result.failure<T>(e)
        }
        emit(result)
    }

    //仓库层实现地点的存储
    fun savePlace(place: Place)=PlaceDao.savePlace(place)
    fun getSavedPlace()=PlaceDao.getSavedPlace()
    fun isPlaceSaved()=PlaceDao.isPlaceSaved()
}