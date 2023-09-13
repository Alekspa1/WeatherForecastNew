package com.drag0n.weatherf0recastn3w


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import android.widget.Toast
import com.drag0n.weatherf0recastn3w.Data.WeatherWeek.WeatherWeek
import com.drag0n.weatherf0recastn3w.databinding.InfoForecastItemBinding
import java.text.SimpleDateFormat
import kotlin.math.roundToInt


object DialogManager {
    fun locationSettingsDialog(context: Context, listener: Listener){
        val builred = AlertDialog.Builder(context)
        val dialog = builred.create()
        dialog.setIcon(R.drawable.ic_location)
        dialog.setTitle("Проверка местоположения")
        dialog.setMessage("Местоположение выключено, хотите включить?")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Да"){_,_->
            listener.onClick(null)
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Нет"){_,_->
            Toast.makeText(context, "Тогда вы не сможете получать информацию о погоде", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()

    }
    fun nameSitySearchDialog(context: Context, listener: Listener){
        val builred = AlertDialog.Builder(context)
        val edName = EditText(context)
        builred.setView(edName)
        val dialog = builred.create()
        dialog.setIcon(R.drawable.ic_search)
        dialog.setTitle("Введите название города")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Поиск"){_,_->
            listener.onClick(edName.text.toString().trim())
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Назад"){_,_->
            dialog.dismiss()
        }

        dialog.show()

    }
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun infoForecact(context: Context, weatherWeek: WeatherWeek, pos: Int){
        val builrder = AlertDialog.Builder(context)
        val binding = InfoForecastItemBinding.inflate(builrder.create().layoutInflater)

        builrder.setView(binding.root)
        val dialog = builrder.create()
        binding.tvMinTemp.text = "Мин. прогнозируемая температура: ${(weatherWeek.list[pos].main.temp_min* 10.0).roundToInt() / 10.0}°C"
        binding.tvMaxTemp.text = "Макс. прогнозируемая температура: ${(weatherWeek.list[pos].main.temp_max* 10.0).roundToInt() / 10.0}°C"
        binding.tvPressure.text = "Давление: ${weatherWeek.list[pos].main.pressure} гПа"
        binding.tvHumidity.text = "Влажность: ${weatherWeek.list[pos].main.humidity} %"
        binding.tvSpeedWind.text = "Скорость ветра: ${weatherWeek.list[pos].wind.speed} метр/сек"
        val timeSunrise = SimpleDateFormat(" HH : mm ").format(weatherWeek.city.sunrise*1000L)
        val timeSunset = SimpleDateFormat(" HH : mm ").format(weatherWeek.city.sunset*1000L)

        binding.tvSunrise.text = "Время восхода солнца: $timeSunrise"
        binding.tvSunset.text = "Время захода солнца: $timeSunset"
        binding.tvPopulation.text = "Население: ${weatherWeek.city.population} чел."

        dialog.show()
    }
    interface Listener{
        fun onClick(city: String?)
    }
}