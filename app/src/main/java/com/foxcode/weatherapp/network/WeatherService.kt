package com.foxcode.weatherapp.network

import com.foxcode.weatherapp.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {

    @GET("data/2.5/weather?")
    fun getCurrentWeatherDataLonLat(@Query("lat") lat : String, @Query("lon") lon : String, @Query("appid") api_id :String) : Call<WeatherResponse>

    @GET("data/2.5/weather?")
    fun getCurrentWeatherDataCity(@Query("q") city : String, @Query("appid") api_id :String) : Call<WeatherResponse>

}