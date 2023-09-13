package com.drag0n.weatherf0recastn3w


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.drag0n.weatherf0recastn3w.Data.WeatherWeek.Spisok
import com.drag0n.weatherf0recastn3w.databinding.TestBinding


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
    fun alertDialog(context: Context, day: Spisok){
        val binding: TestBinding
        val builrder = AlertDialog.Builder(context)
        val dialog = builrder.create()
        binding = TestBinding.inflate(dialog.layoutInflater)

        builrder.setView(binding.root)
        //Log.d("MyLog", binding.root.toString())


        dialog.show()
    }
    interface Listener{
        fun onClick(city: String?)
    }
}