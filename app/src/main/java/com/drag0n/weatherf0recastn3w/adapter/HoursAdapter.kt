package com.drag0n.weatherf0recastn3w.adapter


import android.icu.util.Calendar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.drag0n.weatherf0recastn3w.R
import com.drag0n.weatherf0recastn3w.data.forecast.Hour
import com.drag0n.weatherf0recastn3w.databinding.ItemDaysAdapterBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

class HoursAdapter(private val weatherWeek: List<Hour>) : RecyclerView.Adapter<HoursAdapter.Holder>() {

    class Holder(item: View) : RecyclerView.ViewHolder(item), AnimationListener {

        private val binding = ItemDaysAdapterBinding.bind(item)
        val context = item.context!!
        private var flag: Boolean = false
        private lateinit var inAnimation: Animation
        private lateinit var outAnimation: Animation
        private lateinit var inAnimationRotate: Animation


        fun bind(hour: Hour) = with(binding) {
            inAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_in)
            outAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_out)
            inAnimationRotate = AnimationUtils.loadAnimation(context, R.anim.rotate_in)
            outAnimation.setAnimationListener(this@Holder)

            val currentFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(hour.time)
            val convertDate = currentFormat?.let {
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(
                    it
                )
            }
            val temp = "${hour.temp_c.roundToInt()}°C"
            val feltTemp = context.getString(R.string.dayFragment_felt) +
                    " ${hour.feelslike_c.roundToInt()}°C."
            val pasc = context.getString(R.string.dayFragment_pressure) +
                    " ${(hour.pressure_mb.toInt() / 1.33).roundToInt()} ${context.getString(R.string.dayFragment_pressure_mm_rt_st)}"

            val vlaz = context.getString(R.string.dayFragment_humidity) +
                    " ${hour.humidity} %."

            val windSpeed = context.getString(R.string.dayFragment_windSpeed) +
                    " ${(hour.wind_mph/2.2).roundToInt()} " +
                    context.getString(R.string.dayFragment_windSpeed_ms)
            tvTime.text = convertDate
            tvCond.text = hour.condition.text
            Picasso.get().load("https:${hour.condition.icon}").into(imDec)
            tvMinMax.text = temp
            tvMinTemp.text = feltTemp
            tvHumidity.text = vlaz
            tvPressure.text = pasc
            tvSpeedWind.text = windSpeed

            cardView31.setOnClickListener {

                cardView31.startAnimation(inAnimationRotate)
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

    override fun getItemCount(): Int {
        return weatherWeek.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(weatherWeek[position])

    }


}