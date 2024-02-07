package com.drag0n.weatherf0recastn3w.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drag0n.weatherf0recastn3w.Data.WeatherWeek.Spisok
import com.drag0n.weatherf0recastn3w.R
import com.drag0n.weatherf0recastn3w.databinding.ItemDaysAdapterBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class DaysAdapter() : ListAdapter<Spisok, DaysAdapter.Holder>(DiffCallback()) {

    class Holder(item: View) : RecyclerView.ViewHolder(item), AnimationListener {
        private val binding = ItemDaysAdapterBinding.bind(item)
        val context = item.context
        private var flag: Boolean = false
        private lateinit var inAnimation: Animation
        private lateinit var outAnimation: Animation
        private lateinit var inAnimationRotate: Animation


        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(day: Spisok) = with(binding) {

            inAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_in)
            outAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_out)
            inAnimationRotate = AnimationUtils.loadAnimation(context, R.anim.rotate_in)
            outAnimation.setAnimationListener(this@Holder)
            val url = day.weather[0].icon
            val temp = "${(day.main.temp * 10.0).roundToInt() / 10.0}°C"
            tvCond.text = day.weather[0].description

            val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(day.dt_txt)
            val time = SimpleDateFormat(" HH:mm").format(dateTime)

            val dateString = day.dt_txt
            val readingFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val date = LocalDate.parse(dateString, readingFormatter)
            val writingFormatter = DateTimeFormatter.ofPattern("dd MMM")
            val formattedDate = date.format(writingFormatter)

            tvDateDay.text = formattedDate
            tvTime.text = time
            tvMinMax.text = temp
            Glide
                .with(context)
                .load("https://openweathermap.org/img/wn/$url@2x.png")
                .into(imDec)

            tvMinTemp.text =
                "Мин. прогнозируемая температура: ${(day.main.temp_min * 10.0).roundToInt() / 10.0}°C"
            tvMaxTemp.text =
                "Макс. прогнозируемая температура: ${(day.main.temp_max * 10.0).roundToInt() / 10.0}°C"
            tvPressure.text = "Давление: ${day.main.pressure} гПа"
            tvHumidity.text = "Влажность: ${day.main.humidity} %"
            tvSpeedWind.text = "Скорость ветра: ${day.wind.speed} метр/сек"
            tvSunset.text = "Вероятность осадков: ${(day.pop * 1000.0).roundToInt() / 10.0}%"
            cardView3.setOnClickListener {

                cardView3.startAnimation(inAnimationRotate)
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
            if(tvTime.text.toString() == " 00:00")  cardDay.visibility = View.VISIBLE
            else cardDay.visibility = View.GONE


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

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))

    }


}