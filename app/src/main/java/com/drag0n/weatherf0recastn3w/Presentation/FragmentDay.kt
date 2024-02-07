package com.drag0n.weatherf0recastn3w.Presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.room.Room
import com.bumptech.glide.Glide
import com.drag0n.weatherf0recastn3w.Const
import com.drag0n.weatherf0recastn3w.Data.RoomWeather.WeatherDayNowDB
import com.drag0n.weatherf0recastn3w.Data.RoomWeather.WeatherDayNowDbImp
import com.drag0n.weatherf0recastn3w.DialogManager
import com.drag0n.weatherf0recastn3w.MainViewModel
import com.drag0n.weatherf0recastn3w.R

import com.drag0n.weatherf0recastn3w.databinding.FragmentDayBinding
import com.yandex.mobile.ads.common.AdError
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


class FragmentDay : Fragment() {
    private lateinit var binding: FragmentDayBinding
    private lateinit var model: MainViewModel
    private lateinit var weatherDB: WeatherDayNowDB
    private lateinit var date: String
    private lateinit var inAnimation: Animation
    private lateinit var outAnimation: Animation
    private var interstitialAd: InterstitialAd? = null
    private var interstitialAdLoader: InterstitialAdLoader? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherDB = Room.databaseBuilder(view.context,
            WeatherDayNowDB::class.java,
            "Текущая погода на день").build()
        model = MainViewModel()
        date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM"))
        inAnimation = AnimationUtils.loadAnimation(view.context, R.anim.alpha_in)
        outAnimation = AnimationUtils.loadAnimation(view.context, R.anim.alpha_out)
        interstitialAdLoader = InterstitialAdLoader(view.context).apply {
            setAdLoadListener(object : InterstitialAdLoadListener {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    // The ad was loaded successfully. Now you can show loaded ad.
                }

                override fun onAdFailedToLoad(adRequestError: AdRequestError) {
                    // Ad failed to load with AdRequestError.
                    // Attempting to load a new ad from the onAdFailedToLoad() method is strongly discouraged.
                }
            })
        }
        loadInterstitialAd()
        model.liveDataDayNow.observe(viewLifecycleOwner) {
            Thread{
                if(weatherDB.CourseDao().getAll() == null){
                    weatherDB.CourseDao()
                        .insertAll(
                            WeatherDayNowDbImp(1,
                            date,
                            it.weather[0].icon,
                            it.name,it.main.temp.toString(),
                            it.main.feels_like.toString(),
                            it.weather[0].description)
                        )
                } else weatherDB.CourseDao().
                update(
                    WeatherDayNowDbImp(1,
                    date,
                    it.weather[0].icon,
                    it.name,it.main.temp.toString(),
                    it.main.feels_like.toString(),
                    it.weather[0].description)
                )

            }.start()

            val tempMinMax = "Ощущается как: ${(it.main.feels_like * 10.0).roundToInt() / 10.0}°C."
            val tempCurent = "${(it.main.temp * 10.0).roundToInt() / 10.0}°C"

            val url = it.weather[0].icon
            binding.tvCity.text = it.name
            binding.tvData.text = date
            binding.TvMinMax.text = tempMinMax
            binding.tvCurrentTemp.text = tempCurent
            binding.tvCondition.text = "За окном: ${it.weather[0].description}."
            binding.tvPasc.text = "Давление: ${(it.main.pressure * 10.0).roundToInt() / 10} гПа."
            binding.tvVlaz.text = "Влажность: ${(it.main.humidity * 10.0).roundToInt() / 10} %."
            Glide
                .with(this)
                .load("https://openweathermap.org/img/wn/$url@2x.png")
                .into(binding.imWeather)
            binding.tvWind.text = "Скорость ветра: ${it.wind.speed} метр/сек."
        } // Заполнение погоды на сегодняшний день
        insert()

        binding.ibSync.setOnClickListener {
            binding.ibSync.playAnimation()
            binding.root.startAnimation(outAnimation)
            showAd()

        }

        binding.ibSearch.setOnClickListener {
            DialogManager.nameSitySearchDialog(view.context, object : DialogManager.Listener {
                override fun onClick(city: String?) {
                    if (city != null) {
                        model.getApiNameCitiNow(city, view.context)
                        model.getApiNameCitiWeek(city, view.context)
                        binding.root.startAnimation(outAnimation)
                    }
                }
            })

        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = FragmentDay()
    }
    @SuppressLint("SetTextI18n")
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
                    binding.tvCondition.text = "За окном: ${insert.description}."
                }

            }.start()
            Glide
                .with(this)
                .load("https://openweathermap.org/img/wn/$url@2x.png")
                .into(binding.imWeather)



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
            activity?.let { show(it) }
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
}