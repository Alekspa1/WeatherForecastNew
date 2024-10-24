package com.drag0n.weatherf0recastn3w

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.drag0n.weatherf0recastn3w.Const.APIKEY
import com.drag0n.weatherf0recastn3w.Const.LANGUAGE
import com.drag0n.weatherf0recastn3w.data.astrology.NewAstrology
import com.drag0n.weatherf0recastn3w.data.current.NewCurrent
import com.drag0n.weatherf0recastn3w.data.forecast.NewForecast
import com.drag0n.weatherf0recastn3w.domane.API.ApiWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject constructor(
        private val api:ApiWeather,
        private val application: Application,
    )
    : AndroidViewModel(application) {



    val load = MutableLiveData<Boolean>()
    val premium = MutableLiveData<Boolean>()

    private val _responseCurrent = MutableLiveData<NewCurrent>()
    val responseCurrent: LiveData<NewCurrent> = _responseCurrent

    private val _responseForecast = MutableLiveData<NewForecast>()
    val responseForecast: LiveData<NewForecast> = _responseForecast

    private val _responseAstronomy = MutableLiveData<NewAstrology>()
    val responseAstronomy: LiveData<NewAstrology> = _responseAstronomy

    fun getCurrent(latLon: String){
        viewModelScope.launch {
            try {
                api.getCurrent(APIKEY, latLon, LANGUAGE).let { responseApi ->
                    if (responseApi.isSuccessful) _responseCurrent.postValue(responseApi.body())
                    else {
                        falseLoad()
                        Toast.makeText(
                            application,
                            application.getString(R.string.repository_error_data_onFailure),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                api.getAstronomy(APIKEY, latLon, LANGUAGE).let { responseApi ->
                    if (responseApi.isSuccessful) _responseAstronomy.postValue(responseApi.body())
                }
                api.getForecast(APIKEY, latLon, LANGUAGE).let { responseApi ->
                    if (responseApi.isSuccessful) _responseForecast.postValue(responseApi.body())
                    else {
                        falseLoad()
                        Toast.makeText(
                            application,
                            application.getString(R.string.repository_error_data_onFailure),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                falseLoad()
                Toast.makeText(
                    application,
                    application.getString(R.string.repository_error_data_onResponse),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

//    fun getForecast(latLon: String){
//        viewModelScope.launch {
//            try {
//                api.getForecast(APIKEY, latLon, LANGUAGE).let { responseApi ->
//                    if (responseApi.isSuccessful) _responseForecast.postValue(responseApi.body())
//                    else {
//                        falseLoad()
//                        Toast.makeText(
//                            application,
//                            application.getString(R.string.repository_error_data_onFailure),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            } catch (e: Exception) {
//                falseLoad()
//                Toast.makeText(
//                    application,
//                    application.getString(R.string.repository_error_data_onResponse),
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//
//    }

    fun falseLoad() {
        load.value = false }
    fun trueLoad() {
        load.value = true }


}