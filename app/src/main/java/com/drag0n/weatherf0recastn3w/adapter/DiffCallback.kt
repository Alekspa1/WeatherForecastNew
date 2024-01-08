package com.drag0n.weatherf0recastn3w.adapter

import androidx.recyclerview.widget.DiffUtil
import com.drag0n.weatherf0recastn3w.Data.WeatherWeek.Spisok


class DiffCallback: DiffUtil.ItemCallback<Spisok>() {
    override fun areItemsTheSame(oldItem: Spisok, newItem: Spisok): Boolean {
        return oldItem.main.temp == newItem.main.temp
    }

    override fun areContentsTheSame(oldItem: Spisok, newItem: Spisok): Boolean {
        return oldItem == newItem
    }


}