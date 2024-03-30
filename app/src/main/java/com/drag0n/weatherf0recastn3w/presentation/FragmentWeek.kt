package com.drag0n.weatherf0recastn3w.presentation

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.drag0n.weatherf0recastn3w.MainViewModel
import com.drag0n.weatherf0recastn3w.adapter.DaysAdapter
import com.drag0n.weatherf0recastn3w.data.WeatherWeek.Spisok
import com.drag0n.weatherf0recastn3w.databinding.FragmentWeekBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentWeek : Fragment() {
    private lateinit var binding: FragmentWeekBinding
    private lateinit var model: MainViewModel
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
        model = MainViewModel()

        val rc = binding.rcDay
        rc.layoutManager = LinearLayoutManager(view.context)
        model.liveDataWeek.observe(viewLifecycleOwner) {
            adapter = DaysAdapter(it.list)
            rc.adapter = adapter
        } // Заполнение погоды на неделю
    }

    companion object {
        fun newInstance() = FragmentWeek()
    }
}