package com.drag0n.weatherf0recastn3w.Presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.drag0n.weatherf0recastn3w.R

class FragmentMap : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentMap()
    }
}