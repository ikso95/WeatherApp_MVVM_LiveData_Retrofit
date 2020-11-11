package com.foxcode.weatherapp.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.foxcode.weatherapp.R
import com.foxcode.weatherapp.model.WeatherResponse
import com.foxcode.weatherapp.network.WeatherService
import com.foxcode.weatherapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    val API_KEY = "f1841f7fc4d6a06bc1ab2349674309da"

    var mainViewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_main)

        setObservables()

        swipeRefreshLayout.setOnRefreshListener {
            mainViewModel.getData(editText_city.text.trim().toString())
            swipeRefreshLayout.isRefreshing = false
        }

        button.setOnClickListener {
            mainViewModel.getData(editText_city.text.trim().toString())
        }

        editText_city.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                mainViewModel.getData(editText_city.text.trim().toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })


    }

    override fun onResume() {
        super.onResume()
        if(editText_city.text.trim().toString()!="")
            mainViewModel.getData(editText_city.text.trim().toString())
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.stopGettingData()
    }

    fun setObservables(){
        mainViewModel.getTemperature().observe(this, object : Observer<String>{
            override fun onChanged(t: String?) {
                textView_temperature.setText(t)
            }
        })

        mainViewModel.getHumidity().observe(this, object : Observer<String>{
            override fun onChanged(t: String?) {
                textView_humidity.setText(t)
            }
        })

        mainViewModel.getPressure().observe(this, object : Observer<String>{
            override fun onChanged(t: String?) {
                textView_pressure.setText(t)
            }
        })

        mainViewModel.getSunrise().observe(this, object : Observer<String>{
            override fun onChanged(t: String?) {
                textView_sunRise.setText(t)
            }
        })

        mainViewModel.getSunset().observe(this, object : Observer<String>{
            override fun onChanged(t: String?) {
                textView_sunSet.setText(t)
            }
        })

        mainViewModel.getWind().observe(this, object : Observer<String>{
            override fun onChanged(t: String?) {
                textView_wind.setText(t)
            }
        })
    }


}