package com.drag0n.weatherf0recastn3w

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bumptech.glide.Glide
import com.drag0n.weatherf0recastn3w.Data.RoomWeather.WeatherDayNowDB
import com.drag0n.weatherf0recastn3w.Data.RoomWeather.WeatherDayNowDbImp
import com.drag0n.weatherf0recastn3w.adapter.DaysAdapter
import com.drag0n.weatherf0recastn3w.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() { // Заканчивает MainActivity

    private var interstitialAd: InterstitialAd? = null
    private var interstitialAdLoader: InterstitialAdLoader? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DaysAdapter
    private lateinit var inAnimation: Animation
    private lateinit var outAnimation: Animation
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var fLocotionClient: FusedLocationProviderClient
    private lateinit var weatherDB: WeatherDayNowDB
    val model: MainViewModel by viewModels()
    private lateinit var date: String


    override fun onCreate(savedInstanceState: Bundle?) {


        binding = ActivityMainBinding.inflate(layoutInflater)
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
        fLocotionClient = LocationServices.getFusedLocationProviderClient(this)
        super.onCreate(savedInstanceState)
        date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM"))
        setContentView(binding.root)
        weatherDB = Room.databaseBuilder(this,
            WeatherDayNowDB::class.java,
            "Текущая погода на день").build()
        insert()




// яндекс реклама
        binding.yaMob.setAdUnitId(Const.baner)
        binding.yaMob.setAdSize(BannerAdSize.stickySize(this, 350))
        val adRequest = AdRequest.Builder().build()
        binding.yaMob.loadAd(adRequest)

        interstitialAdLoader = InterstitialAdLoader(this).apply {
            setAdLoadListener(object : InterstitialAdLoadListener {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    this@MainActivity.interstitialAd = interstitialAd
                    // The ad was loaded successfully. Now you can show loaded ad.
                }

                override fun onAdFailedToLoad(adRequestError: AdRequestError) {
                    // Ad failed to load with AdRequestError.
                    // Attempting to load a new ad from the onAdFailedToLoad() method is strongly discouraged.
                }
            })
        }
        loadInterstitialAd()



        inAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_in)
        outAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_out)
        val rc = binding.rcDay
        rc.layoutManager = LinearLayoutManager(this)

        model.liveDataWeek.observe(this) {

            adapter = DaysAdapter()
            rc.adapter = adapter
            adapter.submitList(it.list)
        } // Заполнение погоды на неделю

        model.liveDataDayNow.observe(this) {
            Thread{
                if(weatherDB.CourseDao().getAll() == null){
                    weatherDB.CourseDao()
                        .insertAll(WeatherDayNowDbImp(1,
                            date,
                            it.weather[0].icon,
                            it.name,it.main.temp.toString(),
                            it.main.feels_like.toString(),
                            it.weather[0].description))
                } else weatherDB.CourseDao().
                update(WeatherDayNowDbImp(1,
                    date,
                    it.weather[0].icon,
                    it.name,it.main.temp.toString(),
                    it.main.feels_like.toString(),
                    it.weather[0].description))

            }.start()

            when (it.weather[0].id) {
                200, 201, 202, 210, 211, 212, 221, 230, 231, 232 -> binding.root.setBackgroundResource(
                    R.drawable.img_1
                ) // гроза
                300, 301, 302, 310, 311, 312, 313, 314, 321 -> binding.root.setBackgroundResource(R.drawable.img_2) // морось
                500, 501, 502, 503, 504, 511, 520, 521, 522, 531 -> binding.root.setBackgroundResource(
                    R.drawable.img_3
                ) // дождь
                600, 601, 602, 611, 612, 613, 615, 616, 620, 621, 622 -> binding.root.setBackgroundResource(
                    R.drawable.img_3
                ) // снег
                800 -> binding.root.setBackgroundResource(R.drawable.img_5) // Чистое небо
                else -> binding.root.setBackgroundResource(R.drawable.img)
            } // Меняет фон

            val tempMinMax = "Ощущается как: ${(it.main.feels_like * 10.0).roundToInt() / 10.0}°C"
            val tempCurent = "${(it.main.temp * 10.0).roundToInt() / 10.0}°C"

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
            binding.ibSync.playAnimation()
            binding.root.startAnimation(outAnimation)
            showAd()

        }

        binding.ibSearch.setOnClickListener {
            DialogManager.nameSitySearchDialog(this, object : DialogManager.Listener {
                override fun onClick(city: String?) {
                    if (city != null) {
                        model.getApiNameCitiNow(city, this@MainActivity)
                        model.getApiNameCitiWeek(city, this@MainActivity)
                        binding.root.startAnimation(outAnimation)
                    }
                }
            })

        }



    } // OnCreate

    override fun onResume() {
        super.onResume()
        chekPermissionLocation()
    }

    private fun loadInterstitialAd() {
        val adRequestConfiguration = AdRequestConfiguration.Builder(Const.mezstr).build()
        interstitialAdLoader?.loadAd(adRequestConfiguration)
    }

    private fun showAd() {
        interstitialAd?.apply {
            setAdEventListener(object : InterstitialAdEventListener {
                override fun onAdShown() {
                    // Called when ad is shown.
                }

                override fun onAdFailedToShow(adError: AdError) {
                    // Called when an InterstitialAd failed to show.
                }

                override fun onAdDismissed() {
                    // Called when ad is dismissed.
                    // Clean resources after Ad dismissed
                    interstitialAd?.setAdEventListener(null)
                    interstitialAd = null

                    // Now you can preload the next interstitial ad.
                    loadInterstitialAd()
                }

                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                }

                override fun onAdImpression(impressionData: ImpressionData?) {
                    // Called when an impression is recorded for an ad.
                }
            })
            show(this@MainActivity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        interstitialAdLoader?.setAdLoadListener(null)
        interstitialAdLoader = null
        destroyInterstitialAd()
    }

    private fun destroyInterstitialAd() {
        interstitialAd?.setAdEventListener(null)
        interstitialAd = null
    }



    private fun chekLocation() {
        if (isLocationEnabled()) {
            getLocation()
        } else {
            DialogManager.locationSettingsDialog(this, object : DialogManager.Listener {
                override fun onClick(city: String?) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }

            })
        }
    } // Функция проверяет включено ли GPS

    private fun isLocationEnabled(): Boolean {
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    } // Функция узнает включено ли GPS
    private fun chekPermissionLocation() {
        if (Const.isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            chekLocation()
        } else {
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    } // проверяет есть ли разрешение геолокации

    private fun getLocation() {
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
        fLocotionClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .addOnCompleteListener {
                Const.lat = it.result.latitude.toString()
                Const.lon = it.result.longitude.toString()
                model.getApiWeekLocation(Const.lat,Const.lon, this)
                model.getApiDayNowLocation(Const.lat,Const.lon, this)
            }
    } // Функция для получения геолокации


    fun insert(){
        var url = ""
        Thread{
            val insert = weatherDB.CourseDao().getAll()
            if (insert != null){
                val tempMinMax = "Ощущается как: ${insert.curent}°C"
                val tempCurent = "${insert.temp}°C"
                url = insert.url
                binding.tvCity.text = insert.name
                binding.tvData.text = date
                binding.TvMinMax.text = tempMinMax
                binding.tvCurrentTemp.text = tempCurent
                binding.tvCondition.text = insert.description
            }

        }.start()
        Glide
                .with(this)
                .load("https://openweathermap.org/img/wn/$url@2x.png")
                .into(binding.imWeather)

    }



}

