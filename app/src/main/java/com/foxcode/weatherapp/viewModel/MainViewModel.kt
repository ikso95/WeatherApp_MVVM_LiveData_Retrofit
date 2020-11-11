package com.foxcode.weatherapp.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foxcode.weatherapp.model.WeatherResponse
import com.foxcode.weatherapp.network.WeatherService
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.log
import kotlin.math.roundToInt

class MainViewModel : ViewModel() {

    private val API_KEY = "f1841f7fc4d6a06bc1ab2349674309da"
    private val BASE_URL = "https://api.openweathermap.org/"

    private val mutableTemperature = MutableLiveData<String>()
    private val mutableHumidity = MutableLiveData<String>()
    private val mutablePressure = MutableLiveData<String>()
    private val mutableSunrise = MutableLiveData<String>()
    private val mutableSunset = MutableLiveData<String>()
    private val mutableWind = MutableLiveData<String>()


    fun getTemperature(): LiveData<String> {
        return mutableTemperature
    }

    fun getHumidity(): LiveData<String> {
        return mutableHumidity
    }

    fun getPressure(): LiveData<String> {
        return mutablePressure
    }

    fun getSunrise(): LiveData<String> {
        return mutableSunrise
    }

    fun getSunset(): LiveData<String> {
        return mutableSunset
    }

    fun getWind(): LiveData<String> {
        return mutableWind
    }

    fun setTemperature(value: String) {
        mutableTemperature.value = value
    }

    fun setHumidity(value: String) {
        mutableHumidity.value = value
    }

    fun setPressure(value: String) {
        mutablePressure.value = value
    }

    fun setSunrise(value: String) {
        mutableSunrise.value = value
    }

    fun setSunset(value: String) {
        mutableSunset.value = value
    }

    fun setWind(value: String) {
        mutableWind.value = value
    }


    var job : Job? = null

    fun getData(city:String){
        stopGettingData();
        job = viewModelScope.launch {
            while(isActive)
            {
                getDataFromAPI(city)
                delay(10000)
            }

            Log.d("ex", "getData: ")
        }
    }

    fun stopGettingData(){
        job?.cancel()
    }



    private fun getDataFromAPI(city : String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: WeatherService = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherDataCity(city, API_KEY)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    Log.d("response", "onResponse: " + weatherResponse)
                    val temperature = weatherResponse!!.main.temp
                    val temperatureCelcius = temperature.minus(273.15).roundToInt()
                    setTemperature(temperatureCelcius.toString() + "\u2103")

                    setPressure(weatherResponse.main.pressure.toString() + " hPa")

                    setHumidity(weatherResponse.main.humidity.toString() + "%")

                    setSunrise(getDateTime(weatherResponse.sys.sunrise.toString()))

                    setSunset(getDateTime(weatherResponse.sys.sunset.toString()))

                    setWind(weatherResponse.wind.speed.toString() + "km/h")
                } else {

                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("tag", "onFailure: " + t)
            }
        })
    }

    private fun getDateTime(s: String): String {
        try {
            val sdf = SimpleDateFormat("hh:mm a")
            val netDate = Date(s.toLong() * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }



}