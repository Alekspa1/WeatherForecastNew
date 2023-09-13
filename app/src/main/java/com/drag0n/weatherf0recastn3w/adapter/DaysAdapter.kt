package com.drag0n.weatherf0recastn3w.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drag0n.weatherf0recastn3w.Data.WeatherWeek.Spisok
import com.drag0n.weatherf0recastn3w.Data.WeatherWeek.WeatherWeek
import com.drag0n.weatherf0recastn3w.DialogManager
import com.drag0n.weatherf0recastn3w.R
import com.drag0n.weatherf0recastn3w.databinding.ItemDaysAdapterBinding
import kotlin.math.roundToInt

class DaysAdapter(private val weather: WeatherWeek, val listener: Listener) : RecyclerView.Adapter<DaysAdapter.Holder>() {
    class Holder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = ItemDaysAdapterBinding.bind(item)
        val context = item.context


        fun bind(day: Spisok, listener: Listener, weather: WeatherWeek, pos: Int) = with(binding) {
            val url = day.weather[0].icon
            val temp = "${(day.main.temp* 10.0).roundToInt() / 10.0}°C"
            tvCond.text = day.weather[0].description
            tvDate.text = day.dt_txt
            tvMinMax.text = temp
            Glide
                .with(context)
                .load("https://openweathermap.org/img/wn/$url@2x.png")
                .into(imDec)
            root.setOnClickListener {
                listener.onClick(weather, pos)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_days_adapter, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(weather.list[position], listener, weather, position)

    }

    override fun getItemCount(): Int {
        return weather.list.size
    }
    interface Listener{
        fun onClick(weather: WeatherWeek, pos: Int)
    }
}