package com.drag0n.weatherf0recastn3w


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import android.widget.Toast


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
    interface Listener{
        fun onClick(city: String?)
    }
}