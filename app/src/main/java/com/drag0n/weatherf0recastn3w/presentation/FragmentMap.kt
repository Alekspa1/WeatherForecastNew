package com.drag0n.weatherf0recastn3w.presentation

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.drag0n.weatherf0recastn3w.Const
import com.drag0n.weatherf0recastn3w.MainViewModel
import com.drag0n.weatherf0recastn3w.databinding.FragmentMapBinding

class FragmentMap : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private lateinit var model: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = MainViewModel()
        model.liveDataDayNow.observe(viewLifecycleOwner){
            val lat = model.liveDataDayNow.value?.coord?.lat
            val lon = model.liveDataDayNow.value?.coord?.lon
            val URL = "https://www.meteoblue.com/${Const.language}/weather/maps/widget/?windAnimation=0&windAnimation=1&gust=0&satellite=0&cloudsAndPrecipitation=0&temperature=0&temperature=1&sunshine=0&extremeForecastIndex=0&geoloc=fixed&tempunit=C&windunit=m%252Fs&lengthunit=metric&zoom=5&autowidth=auto#coords=5/$lat/$lon&map=windAnimation~coldwarm~auto~10%20m%20above%20gnd~none"

            binding.map.apply {
                loadUrl(URL)
                settings.javaScriptEnabled = true
                settings.allowContentAccess = true
                settings.domStorageEnabled = true
                settings.useWideViewPort = true
            }

        }


    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentMap()
    }
}