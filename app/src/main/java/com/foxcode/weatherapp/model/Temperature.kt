package com.foxcode.weatherapp.model

class Temperature(temp: Double) {

    val temperatureKelvin  = temp

    fun toCelcius() : Double{
        return temperatureKelvin-273.15
    }

    fun toFarenheit() : Double{
        return temperatureKelvin*9/5-459.67
    }

}