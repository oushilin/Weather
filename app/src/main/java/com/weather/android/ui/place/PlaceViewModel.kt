package com.weather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.weather.android.logic.Repository
import com.weather.android.logic.model.Place
import retrofit2.http.Query

class PlaceViewModel:ViewModel() {
    private val searchLiveData= MutableLiveData<String>()
    val placeList=ArrayList<Place>()
    val placeLiveData=Transformations.switchMap(searchLiveData){query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query: String){
        searchLiveData.value=query
    }
    //对repository的接口再一次进行封装
    fun savePlace(place: Place)=Repository.savePlace(place)
    fun getSavedPlace()=Repository.getSavedPlace()
    fun isPlaceSaved()=Repository.isPlaceSaved()
}