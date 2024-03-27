package com.drag0n.weatherf0recastn3w.Presentation

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.drag0n.weatherf0recastn3w.Const
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt


class FragmentDay : Fragment() {
    private lateinit var binding: FragmentDayBinding
    private lateinit var model: MainViewModel
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = MainViewModel()
        date = SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date())
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
            val timeSunrise =
                SimpleDateFormat(" HH : mm ",
                    Locale.getDefault()).format(it.sys.sunrise * 1000L)
            val timeSunset
            = SimpleDateFormat(" HH : mm ",
                Locale.getDefault()).format(it.sys.sunset * 1000L)

            val feltTemp = getString(R.string.dayFragment_felt) +
                    " ${it.main.feels_like.roundToInt()}°C."
            val tempCurent = "${it.main.temp.roundToInt()}°C"
            val url = it.weather[0].icon
            val condition = getString(R.string.dayFragment_condition) +
                    " ${it.weather[0].description}."
            val pasc = getString(R.string.dayFragment_pressure) +
                    " ${(it.main.pressure.toInt() / 1.33).roundToInt()} ${getString(R.string.dayFragment_pressure_mm_rt_st)}"
            val vlaz = getString(R.string.dayFragment_humidity) +
                    " ${it.main.humidity.roundToInt()} %."
            val sunset = getString(R.string.dayFragment_sunrise) +
                    " $timeSunrise"
            val sunrise = getString(R.string.dayFragment_sunset) +
                    " $timeSunset"
            val windSpeed = getString(R.string.dayFragment_windSpeed) +
                    " ${it.wind.speed.roundToInt()} " +
                    getString(R.string.dayFragment_windSpeed_ms)

            binding.tvCity.text = it.name
            binding.tvData.text = date
            binding.TvMinMax.text = feltTemp
            binding.tvCurrentTemp.text = tempCurent
            binding.tvCondition.text = condition
            binding.tvPasc.text = pasc
            binding.tvVlaz.text = vlaz
            binding.tvSunset.text = sunset
            binding.tvSunrise.text = sunrise
            binding.tvWind.text = windSpeed


            Glide
                .with(this)
                .load("https://openweathermap.org/img/wn/$url@2x.png")
                .into(binding.imWeather)

        } // Заполнение погоды на сегодняшний день
        binding.ibSync.setOnClickListener {
            binding.ibSync.playAnimation()
            binding.root.startAnimation(outAnimation)

            if (binding.tvCity.text == getText(R.string.dayFragment_loading).toString()
                || interstitialAd == null) (activity as MainActivity).chekLocation()
            else showAd()


        }
        binding.ibSearch.setOnClickListener {
            DialogManager.nameSitySearchDialog(view.context, object : DialogManager.Listener {
                override fun onClick(city: String?) {
                    if (!city.isNullOrEmpty()) {
                        model.getApiNameCitiNow(city, view.context)
                        model.getApiNameCitiWeek(city, view.context)
                        binding.root.startAnimation(outAnimation)
                        (activity as MainActivity).findViewById<ProgressBar>(R.id.progressBar2).visibility =
                            View.VISIBLE
                    } else {
                        Toast.makeText(
                            view.context,
                            getString(R.string.main_isNotEmpty),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })

        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = FragmentDay()
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