package com.drag0n.weatherf0recastn3w.Presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.drag0n.weatherf0recastn3w.MainViewModel
import com.drag0n.weatherf0recastn3w.adapter.DaysAdapter
import com.drag0n.weatherf0recastn3w.databinding.FragmentWeekBinding

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
        initRcView()


        model.liveDataWeek.observe(viewLifecycleOwner) {
            adapter.submitList(it.list)
        } // Заполнение погоды на неделю
    }

    companion object {
        fun newInstance() = FragmentWeek()
    }
    private fun initRcView(){
        val rc = binding.rcDay
        rc.layoutManager = LinearLayoutManager(view?.context)
        adapter = DaysAdapter()
        rc.adapter = adapter
    }

}