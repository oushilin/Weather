package com.weather.android.logic.network

import com.weather.android.logic.model.WeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import retrofit2.http.Query
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object WeatherNetwork {
    //创建一个placeservice接口的动态代理对象
    private val placeService=ServiceCreator.create(PlaceService::class.java)
    //调用PlaceService接口中定义的searchPlace（）方法
    suspend fun searchPlacee(query: String)= placeService.searchPlaces(query).await()
    //创建一个WeatherServ接口的动态代理对象
    private val weatherService=ServiceCreator.create(WeatherService::class.java)
    //调用weatherService接口定义的getDailyWeather（）方法
    suspend fun getDailyWeather(lng:String,lat:String)= weatherService.getDailytimeWeather(lng, lat).await()
    //调用weatherService接口定义的getRealtimeWeather（）方法
    suspend fun getRealtimeWeather(lng:String,lat:String)= weatherService.getRealtimeWeather(lng, lat).await()

    //定义了await（）函数
    private suspend fun <T> Call<T>.await(): T{
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>){
                    val body = response.body()
                    if (body!=null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null"))
                }
                override fun onFailure(call: Call<T>,t:Throwable){
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}