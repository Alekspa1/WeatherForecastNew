package com.drag0n.weatherf0recastn3w.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.drag0n.weatherf0recastn3w.Const
import com.drag0n.weatherf0recastn3w.DialogManager
import com.drag0n.weatherf0recastn3w.MainViewModel
import com.drag0n.weatherf0recastn3w.R
import com.drag0n.weatherf0recastn3w.databinding.FragmentDayBinding
import com.squareup.picasso.Picasso
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt


@AndroidEntryPoint
class FragmentDay : Fragment() {
    private lateinit var binding: FragmentDayBinding
    private val model: MainViewModel by activityViewModels()
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
        model.responseAstronomy.observe(viewLifecycleOwner){
            val timeSunrise = it.astronomy.astro.sunrise
            val convertSunrise = SimpleDateFormat("hh:mm aa", Locale.getDefault()).parse(timeSunrise)
                ?.let { sunrise -> SimpleDateFormat("HH:mm", Locale.getDefault()).format(sunrise) }
            val sunrise = getString(R.string.dayFragment_sunrise) +
                    " $convertSunrise."

            val timeSunset = it.astronomy.astro.sunset
            val convertSunset = SimpleDateFormat("hh:mm aa").parse(timeSunset)
                ?.let { sunset -> SimpleDateFormat("HH:mm").format(sunset) }
            val sunset = getString(R.string.dayFragment_sunset) +
                    " $convertSunset."

            binding.tvSunrise.text = sunrise
            binding.tvSunset.text = sunset
        } // Заполнение рассвета/заката на сегодняшний день
        model.responseCurrent.observe(viewLifecycleOwner){
            model.falseLoad()
            with(binding){
                Picasso.get().load("https:${it.current.condition.icon}").into(imWeather)
                val tempCurent = "${it.current.temp_c.roundToInt()}°C"

                    val feltTemp = getString(R.string.dayFragment_felt) +
                    " ${it.current.feelslike_c.roundToInt()}°C."

                    val condition = getString(R.string.dayFragment_condition) +
                    " ${it.current.condition.text}."
                   val pasc = getString(R.string.dayFragment_pressure) +
                    " ${(it.current.pressure_mb.toInt() / 1.33).roundToInt()} ${getString(R.string.dayFragment_pressure_mm_rt_st)}"

                    val vlaz = getString(R.string.dayFragment_humidity) +
                    " ${it.current.humidity} %."
                    val windSpeed = getString(R.string.dayFragment_windSpeed) +
                    " ${(it.current.wind_mph/2.2).roundToInt()} " +
                    getString(R.string.dayFragment_windSpeed_ms)

                tvCity.text = it.location.name
                tvData.text = date
                tvCurrentTemp.text = tempCurent
                tvCondition.text = condition
                tvWind.text = windSpeed
                tvMinMax.text = feltTemp
                tvVlaz.text = vlaz
                tvPasc.text = pasc
            }
        } // Заполнение погоды на сегодняшний день


        binding.ibSync.setOnClickListener {
            binding.ibSync.playAnimation()

            if (binding.tvCity.text == getString(R.string.dayFragment_loading)

                || interstitialAd == null
                || (activity as MainActivity).model.premium.value == true
                )
            { (activity as MainActivity).chekLocation()

                model.load.value = true
            }
            else showAd()


        }
        binding.ibSearch.setOnClickListener {
                DialogManager.nameSitySearchDialog(requireContext(), object : DialogManager.Listener {
                override fun onClick(city: String?) {
                    if (!city.isNullOrEmpty()) {
                        model.trueLoad()
                        model.getCurrent(city)
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