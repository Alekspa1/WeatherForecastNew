package com.drag0n.weatherf0recastn3w.presentation

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.drag0n.weatherf0recastn3w.MainViewModel
import com.drag0n.weatherf0recastn3w.adapter.DaysAdapter

import com.drag0n.weatherf0recastn3w.databinding.FragmentWeekBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
@AndroidEntryPoint
class FragmentWeek : Fragment() {
    private lateinit var binding: FragmentWeekBinding
    private val model: MainViewModel by activityViewModels()
    private lateinit var adapter: DaysAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeekBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rc = binding.rcDay
        rc.layoutManager = LinearLayoutManager(view.context)
        model.responseForecast.observe(viewLifecycleOwner) {
            adapter = DaysAdapter(it.forecast.forecastday)
            rc.adapter = adapter
        } // Заполнение погоды на неделю
    }

    companion object {
        fun newInstance() = FragmentWeek()
    }
}