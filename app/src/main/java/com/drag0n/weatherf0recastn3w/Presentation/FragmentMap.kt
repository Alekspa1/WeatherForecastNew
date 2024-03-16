package com.drag0n.weatherf0recastn3w.Presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = MainViewModel()


    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onResume() {
        super.onResume()
        fun mapRadar(): String{
            val lat = model.liveDataDayNow.value?.coord?.lat
            val lon = model.liveDataDayNow.value?.coord?.lon
            return "https://goweatherradar.com/ru/radar-map?lat=$lat&lng=$lon&overlay=temp&zoom=5"
        }
        binding.map.apply {
            loadUrl(mapRadar())
            settings.javaScriptEnabled = true
            settings.allowContentAccess = true
            settings.domStorageEnabled = true
            settings.useWideViewPort = true
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentMap()
    }
}