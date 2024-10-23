package com.drag0n.weatherf0recastn3w.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.drag0n.weatherf0recastn3w.R
import com.drag0n.weatherf0recastn3w.data.forecast.Forecastday
import com.drag0n.weatherf0recastn3w.databinding.ItemWeekBinding
import com.sdkit.paylib.payliblogging.impl.logging.c
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DaysAdapter(private val weatherWeek: List<Forecastday>) : RecyclerView.Adapter<DaysAdapter.Holder>() {

    class Holder(item: View) : RecyclerView.ViewHolder(item), AnimationListener {
        private val binding = ItemWeekBinding.bind(item)
       // private val binding = ItemDaysAdapterBinding.bind(item)
        val context = item.context!!
        private var flag: Boolean = false
        private lateinit var inAnimation: Animation
        private lateinit var outAnimation: Animation
        private lateinit var inAnimationRotate: Animation


        fun bind(day: Forecastday) = with(binding) {
            inAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_in)
            outAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_out)
            inAnimationRotate = AnimationUtils.loadAnimation(context, R.anim.rotate_in)
            outAnimation.setAnimationListener(this@Holder)

            val currentFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(day.date)
            val convertDate = currentFormat?.let {
                SimpleDateFormat("dd MMM", Locale.getDefault()).format(
                    it
                )
            }

            tvDateDay.text = convertDate

//            val url = day.weather[0].icon
//            val temp = "${day.main.temp.roundToInt()}°C"
//            val minTemp = "${context.getString(R.string.WeekFragment_min_current)} ${day.main.temp_min.roundToInt()}°C."
//            val maxTemp = "${context.getString(R.string.WeekFragment_max_current)} ${day.main.temp_max.roundToInt()}°C."
//
//            val pressure = context.getString(R.string.dayFragment_pressure) +
//                    " ${(day.main.pressure/1.33).roundToInt()} " +
//                    context.getString(R.string.dayFragment_pressure_mm_rt_st)
//
//            val vlaz = "${context.getString(R.string.dayFragment_humidity)} ${day.main.humidity} %."
//            val precipitation = "${context.getString(R.string.WeekFragment_precipitation)} ${(day.pop * 100).roundToInt()}%."
//            val windSpeed = context.getString(R.string.dayFragment_windSpeed) +
//                    " ${day.wind.speed.roundToInt()}" +
//                    " ${context.getString(R.string.dayFragment_windSpeed_ms)}"
//
//            val dateString = day.dt_txt
//            val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateString)
//            val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(dateTime!!)
//            val readingFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//            val date = LocalDate.parse(dateString, readingFormatter)
//            val writingFormatter = DateTimeFormatter.ofPattern("EEEE dd MMM")
//            val formattedDate = date.format(writingFormatter)
//
//            tvCond.text = day.weather[0].description
//            tvDateDay.text = formattedDate.replaceFirstChar {
//                if (it.isLowerCase()) it.titlecase(
//                    Locale.getDefault()
//                ) else it.toString()
//            }
//            tvTime.text = time
//            tvMinMax.text = temp
//            tvMinTemp.text = minTemp
//            tvMaxTemp.text = maxTemp
//            tvPressure.text = pressure
//            tvHumidity.text = vlaz
//            tvSpeedWind.text = windSpeed
//            tvSunset.text = precipitation
//            Glide
//                .with(context)
//                .load("https://openweathermap.org/img/wn/$url@2x.png")
//                .into(imDec)



//            cardView3.setOnClickListener {
//
//                cardView3.startAnimation(inAnimationRotate)
//                when {
//                    !flag -> {
//                        card.visibility = View.VISIBLE
//                        card.startAnimation(inAnimation)
//                        flag = true
//                    }
//
//                    flag -> {
//                        card.startAnimation(outAnimation)
//                        flag = false
//                    }
//                }
//            }

        }

        override fun onAnimationStart(p0: Animation?) {

        }

        override fun onAnimationEnd(p0: Animation?) {

        }

        override fun onAnimationRepeat(p0: Animation?) {

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