package com.drag0n.weatherf0recastn3w.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.drag0n.weatherf0recastn3w.presentation.FragmentDay
import com.drag0n.weatherf0recastn3w.presentation.FragmentMap
import com.drag0n.weatherf0recastn3w.presentation.FragmentWeek

private val listFrag = listOf(
    FragmentDay.newInstance(),
    FragmentWeek.newInstance(),
    FragmentMap.newInstance()
)
class VpAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return listFrag.size
    }
    override fun createFragment(position: Int): Fragment {

        return listFrag[position]
    }
}