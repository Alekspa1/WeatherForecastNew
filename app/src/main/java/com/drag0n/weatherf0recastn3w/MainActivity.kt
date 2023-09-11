package com.drag0n.weatherf0recastn3w

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.drag0n.weatherf0recastn3w.Data.WeatherDayNow.WeatherDayNow
import com.drag0n.weatherf0recastn3w.Data.WeatherWeek.WeatherWeek
import com.drag0n.weatherf0recastn3w.adapter.DaysAdapter
import com.drag0n.weatherf0recastn3w.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: DaysAdapter
    val model: MainViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getApiDayNow(Const.city)
        init()
        getApiWeek(Const.city)


    } // OnCreate


    private fun getApiDayNow(city: String) {
        val apiInterface = ApiWeather.create().getWeatherDayNow(city, Const.APIKEY)
        apiInterface.enqueue(object : Callback<WeatherDayNow> {

            override fun onResponse(call: Call<WeatherDayNow>, response: Response<WeatherDayNow>) {
                val data = response.body()
                if (data != null)
                    model.liveDataCurrent.value = data
                else Toast.makeText(
                    this@MainActivity,
                    "Ошибка получения данных",
                    Toast.LENGTH_SHORT
                ).show()


            }

            override fun onFailure(call: Call<WeatherDayNow>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Данные недоступны", Toast.LENGTH_SHORT).show()
            }
        })


    } // Функция для запроса данных о текущей погоде

    private fun getApiWeek(city: String) {
        val apiInterface = ApiWeather.create().getWeatherWeek(city, Const.APIKEY)
        apiInterface.enqueue(object : Callback<WeatherWeek> {

            override fun onResponse(call: Call<WeatherWeek>, response: Response<WeatherWeek>) {
                val data = response.body()
                if (data != null) model.liveDataCurrentWeek.value = data
                else Toast.makeText(
                    this@MainActivity,
                    "Ошибка получения данных",
                    Toast.LENGTH_SHORT
                ).show()

            }

            override fun onFailure(call: Call<WeatherWeek>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Данные недоступны, попробуйе позже", Toast.LENGTH_SHORT).show()
            }
        })

    } // Функция для погоды за неделю

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init() {
        val rc = binding.rcDay
        rc.layoutManager = LinearLayoutManager(this)
        model.liveDataCurrentWeek.observe(this) {
            adapter = DaysAdapter(it)
            rc.adapter = adapter
        }


        model.liveDataCurrent.observe(this) {
            val tempMinMax =
                "Min: ${(it.main.temp_min * 10.0).roundToInt() / 10.0}°C/Max: ${(it.main.temp_max * 10.0).roundToInt() / 10.0}°C"
            val tempCurent = "${(it.main.temp * 10.0).roundToInt() / 10.0}°C"
            val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd"))
            val url = it.weather[0].icon
            binding.tvCity.text = it.name
            binding.tvData.text = date.toString()
            binding.TvMinMax.text = tempMinMax
            binding.tvCurrentTemp.text = tempCurent
            binding.tvCondition.text = it.weather[0].description
            Glide
                .with(this)
                .load("https://openweathermap.org/img/wn/$url@2x.png")
                .into(binding.imWeather)
        } // Заполнение погоды на сегодняшний день
        binding.ibSync.setOnClickListener {
            getApiDayNow(Const.city)
        }
    }
} // Заканчивает MainActivity

