package com.drag0n.weatherf0recastn3w.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drag0n.weatherf0recastn3w.R
import com.drag0n.weatherf0recastn3w.data.forecast.Forecastday
import com.drag0n.weatherf0recastn3w.databinding.ItemWeekBinding
import java.text.SimpleDateFormat
import java.util.Locale

class DaysAdapter(private val weatherWeek: List<Forecastday>) : RecyclerView.Adapter<DaysAdapter.Holder>() {

    class Holder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemWeekBinding.bind(item)
        val context = item.context!!
        private lateinit var adapter: HoursAdapter


        fun bind(day: Forecastday) = with(binding) {

            val currentFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(day.date)
            val convertDate = currentFormat?.let {
                SimpleDateFormat("dd MMM", Locale.getDefault()).format(
                    it
                )
            }
            tvDateDay.text = convertDate
            rcViewHour.layoutManager = LinearLayoutManager(context)
            adapter = HoursAdapter(day.hour)
            rcViewHour.adapter = adapter

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_week, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return weatherWeek.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(weatherWeek[position])

    }


}