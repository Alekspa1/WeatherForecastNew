package com.drag0n.weatherf0recastn3w.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drag0n.weatherf0recastn3w.Data.WeatherWeek.Spisok
import com.drag0n.weatherf0recastn3w.R
import com.drag0n.weatherf0recastn3w.databinding.ItemDaysAdapterBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

class DaysAdapter(private val weatherWeek: List<Spisok>) : RecyclerView.Adapter<DaysAdapter.Holder>() {

    class Holder(item: View) : RecyclerView.ViewHolder(item), AnimationListener {
        private val binding = ItemDaysAdapterBinding.bind(item)
        val context = item.context!!
        private var flag: Boolean = false
        private lateinit var inAnimation: Animation
        private lateinit var outAnimation: Animation
        private lateinit var inAnimationRotate: Animation


        fun bind(day: Spisok) = with(binding) {
            inAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_in)
            outAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_out)
            inAnimationRotate = AnimationUtils.loadAnimation(context, R.anim.rotate_in)
            outAnimation.setAnimationListener(this@Holder)

            val url = day.weather[0].icon
            val temp = "${day.main.temp.roundToInt()}°C"
            val minTemp = "Мин. прогнозируемая температура: ${day.main.temp_min.roundToInt()}°C"
            val maxTemp = "Макс. прогнозируемая температура: ${day.main.temp_max.roundToInt()}°C"
            val pressure = "Давление: ${(day.main.pressure/1.33).roundToInt()} мм рт.ст."
            val vlaz = "Влажность: ${day.main.humidity} %"
            val precipitation = "Вероятность осадков: ${(day.pop * 100).roundToInt()}%"
            val windSpeed = "Скорость ветра: ${day.wind.speed.roundToInt()} метр/сек"
            val dateString = day.dt_txt
            val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateString)
            val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(dateTime!!)
            val readingFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val date = LocalDate.parse(dateString, readingFormatter)
            val writingFormatter = DateTimeFormatter.ofPattern("EEEE dd MMM")
            val formattedDate = date.format(writingFormatter)

            tvCond.text = day.weather[0].description
            tvDateDay.text = formattedDate.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
            tvTime.text = time
            tvMinMax.text = temp
            tvMinTemp.text = minTemp
            tvMaxTemp.text = maxTemp
            tvPressure.text = pressure
            tvHumidity.text = vlaz
            tvSpeedWind.text = windSpeed
            tvSunset.text = precipitation
            Glide
                .with(context)
                .load("https://openweathermap.org/img/wn/$url@2x.png")
                .into(imDec)
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
            if(tvTime.text.toString() == "00:00")  cardDay.visibility = View.VISIBLE
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

    override fun getItemCount(): Int {
        return weatherWeek.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.bind(weatherWeek[position])

    }


}