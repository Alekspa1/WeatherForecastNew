package com.drag0n.weatherf0recastn3w.adapter


import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drag0n.weatherf0recastn3w.R
import com.drag0n.weatherf0recastn3w.data.forecast.Forecastday
import com.drag0n.weatherf0recastn3w.data.forecast.Hour
import com.drag0n.weatherf0recastn3w.databinding.ItemWeekBinding
import java.text.SimpleDateFormat
import java.util.Locale

class DaysAdapter(private val weatherWeek: List<Forecastday>) : RecyclerView.Adapter<DaysAdapter.Holder>() {

    class Holder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemWeekBinding.bind(item)
        val context = item.context!!
        private lateinit var adapter: HoursAdapter


        fun bind(day: Forecastday, position: Int) = with(binding) {
            val calendar = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().time)
            val currentFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(day.date)
            val convertDate = currentFormat?.let {
                SimpleDateFormat("dd MMM", Locale.getDefault()).format(
                    it
                )
            }
            val fo = mutableListOf<Hour>()
            day.hour.forEach {
                val currentFormatTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(it.time)
                val convertDateTime = currentFormatTime?.let { time->
                    SimpleDateFormat("HH:mm", Locale.getDefault()).format(
                        time
                    )
                }
                if (position == 0 && calendar < convertDateTime!!) fo.add(it)
                if (position == 1 || position == 2) fo.add(it)

            }
            if(fo.isEmpty()) {
                binding.cardDay.visibility = View.GONE
                binding.cardView3.visibility = View.GONE
            }
            tvDateDay.text = convertDate
            rcViewHour.layoutManager = LinearLayoutManager(context)
            adapter = HoursAdapter(fo)
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
        holder.bind(weatherWeek[position], position)

    }


}