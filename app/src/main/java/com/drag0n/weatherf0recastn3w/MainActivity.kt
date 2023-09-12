package com.drag0n.weatherf0recastn3w

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.drag0n.weatherf0recastn3w.Data.WeatherDayNow.WeatherDayNow
import com.drag0n.weatherf0recastn3w.Data.WeatherWeek.WeatherWeek
import com.drag0n.weatherf0recastn3w.adapter.DaysAdapter
import com.drag0n.weatherf0recastn3w.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DaysAdapter
    lateinit var pLauncher: ActivityResultLauncher<String>
    lateinit var fLocotionClient: FusedLocationProviderClient
    val model: MainViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){}
        fLocotionClient = LocationServices.getFusedLocationProviderClient(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val rc = binding.rcDay
        rc.layoutManager = LinearLayoutManager(this)
        model.liveDataCurrentWeek.observe(this) {
            adapter = DaysAdapter(it)
            rc.adapter = adapter
        } // Заполнение погоды на неделю
        model.liveDataCurrent.observe(this) {
            when(it.weather[0].id){
                200,201,202,210,211,212,221,230,231,232 -> binding.root.setBackgroundResource(R.drawable.img_1) // гроза
                300,301,302,310,311,312,313,314,321 -> binding.root.setBackgroundResource(R.drawable.img_2) // морось
                500,501,502,503,504,511,520,521,522,531 -> binding.root.setBackgroundResource(R.drawable.img_3) // дождь
                600,601,602,611,612,613,615,616,620,621,622 -> binding.root.setBackgroundResource(R.drawable.img_3) // снег
                800 -> binding.root.setBackgroundResource(R.drawable.img_5) // Чистое небо
                else -> binding.root.setBackgroundResource(R.drawable.img)
            } // Меняет фон
            val tempMinMax = "Ощущается как: ${(it.main.feels_like * 10.0).roundToInt()/ 10.0}°C"
            val tempCurent = "${(it.main.temp * 10.0).roundToInt() / 10.0}°C"
            val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM"))
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
            chekPermissionLocation()
        }
        chekPermissionLocation()

    } // OnCreate

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        chekPermissionLocation()

    }
    private fun getLocation(){
        val ct = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fLocotionClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token )
            .addOnCompleteListener{
                //Const.city ="lat=${it.result.latitude}&lon=${it.result.longitude}"
                Const.lat = it.result.latitude.toString()
                Const.lon = it.result.longitude.toString()
                getApiWeekLocation(Const.lat, Const.lon)
                getApiDayNowLocation(Const.lat, Const.lon)
            }
    } // Функция для получения геолокации
    private fun getApiDayNowLocation(lat: String, lon: String) {
        val apiInterface = ApiWeather.create().getWeatherDayNowLocation(lat, lon, Const.APIKEY)
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

            }
        })


    } // Функция для запроса данных о текущей погоде
    private fun getApiWeekLocation(lat: String, lon: String) {
        val apiInterface = ApiWeather.create().getWeatherWeekLocation(lat, lon, Const.APIKEY)
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
                Toast.makeText(this@MainActivity, "Данные недоступны, попробуйте позже", Toast.LENGTH_SHORT).show()
            }
        })

    } // Функция для погоды за неделю
    @RequiresApi(Build.VERSION_CODES.O)
    private fun chekPermissionLocation(){
        if(Const.isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            getLocation()
        } else{
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    } // проверяет есть ли разрешение геолокации
    private fun getApiWeekCity(city: String) {
        val apiInterface = ApiWeather.create().getWeatherWeekCity(city, Const.APIKEY)
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
                Toast.makeText(this@MainActivity, "Данные недоступны, попробуйте позже", Toast.LENGTH_SHORT).show()
            }
        })

    } // Функция для погоды за неделю для другого города
    private fun getApiDayNowCity(city: String) {
        val apiInterface = ApiWeather.create().getWeatherDayNowCity(city, Const.APIKEY)
        apiInterface.enqueue(object : Callback<WeatherDayNow> {

            override fun onResponse(call: Call<WeatherDayNow>, response: Response<WeatherDayNow>) {
                val data = response.body()
                if (data != null) model.liveDataCurrent.value = data
                else Toast.makeText(
                    this@MainActivity,
                    "Ошибка получения данных",
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onFailure(call: Call<WeatherDayNow>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Данные недоступны, попробуйте позже", Toast.LENGTH_SHORT).show()
            }
        })

    } // Функция для погоды для другого города

} // Заканчивает MainActivity

