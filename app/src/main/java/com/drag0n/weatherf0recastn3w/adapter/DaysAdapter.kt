package com.drag0n.weatherf0recastn3w.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drag0n.weatherf0recastn3w.Data.WeatherWeek.Spisok
import com.drag0n.weatherf0recastn3w.Data.WeatherWeek.WeatherWeek
import com.drag0n.weatherf0recastn3w.DialogManager
import com.drag0n.weatherf0recastn3w.R
import com.drag0n.weatherf0recastn3w.databinding.ItemDaysAdapterBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class DaysAdapter(private val weather: WeatherWeek, val listener: Listener) : RecyclerView.Adapter<DaysAdapter.Holder>() {

    class Holder(item: View) : RecyclerView.ViewHolder(item), AnimationListener {
        val binding = ItemDaysAdapterBinding.bind(item)
        val context = item.context
        private var flag: Boolean = false
        private lateinit var inAnimation: Animation
        private lateinit var outAnimation: Animation



        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(day: Spisok, listener: Listener, weather: WeatherWeek, pos: Int) = with(binding) {
            inAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_in)
           outAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_out)
            outAnimation.setAnimationListener(this@Holder)
            val url = day.weather[0].icon
            val temp = "${(day.main.temp* 10.0).roundToInt() / 10.0}°C"
            tvCond.text = day.weather[0].description

            val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(day.dt_txt)
            val time = SimpleDateFormat(" HH:mm").format(dateTime)

            val dateString = day.dt_txt
            val readingFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val date = LocalDate.parse(dateString, readingFormatter)
            val writingFormatter = DateTimeFormatter.ofPattern("dd MMM")
            val formattedDate = date.format(writingFormatter)

            tvDate.text = formattedDate
            tvTime.text = time


            tvMinMax.text = temp
            Glide
                .with(context)
                .load("https://openweathermap.org/img/wn/$url@2x.png")
                .into(imDec)

            tvMinTemp.text = "Мин. прогнозируемая температура: ${(weather.list[pos].main.temp_min* 10.0).roundToInt() / 10.0}°C"
            tvMaxTemp.text = "Макс. прогнозируемая температура: ${(weather.list[pos].main.temp_max* 10.0).roundToInt() / 10.0}°C"
            tvPressure.text = "Давление: ${weather.list[pos].main.pressure} гПа"
            tvHumidity.text = "Влажность: ${weather.list[pos].main.humidity} %"
            tvSpeedWind.text = "Скорость ветра: ${weather.list[pos].wind.speed} метр/сек"
            val timeSunrise = SimpleDateFormat(" HH : mm ").format(weather.city.sunrise*1000L)
            val timeSunset = SimpleDateFormat(" HH : mm ").format(weather.city.sunset*1000L)

            tvSunrise.text = "Время восхода солнца: $timeSunrise"
            tvSunset.text = "Время захода солнца: $timeSunset"
            tvPopulation.text = "Население: ${weather.city.population} чел."

            root.setOnClickListener {

                listener.onClick(weather, pos)
                when {
                    !flag -> {
                        card.visibility = View.VISIBLE
                        card.startAnimation(inAnimation)
                        flag = true
                    }
                    flag -> {
                        card.startAnimation(outAnimation)
                        flag = false
                    }
                }


            }
        }

        override fun onAnimationStart(p0: Animation?) {

        }

        override fun onAnimationEnd(p0: Animation?) {
            binding.card.visibility = View.GONE

        }

        override fun onAnimationRepeat(p0: Animation?) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_days_adapter, parent, false)
        return Holder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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