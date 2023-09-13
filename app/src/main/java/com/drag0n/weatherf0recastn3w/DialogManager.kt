package com.drag0n.weatherf0recastn3w

import android.app.AlertDialog
import android.content.Context

object DialogManager {
    fun locationSettingsDialog(context: Context, listener: Listener){
        val builred = AlertDialog.Builder(context)
        val dialog = builred.create()
        dialog.setTitle("Проверка местоположения")
        dialog.setMessage("Местоположение выключено, хотите включить?")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok"){_,_->
            listener.onClick()
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cansel"){_,_->
            dialog.dismiss()
        }
        dialog.show()


    }
    interface Listener{
        fun onClick()
    }
}