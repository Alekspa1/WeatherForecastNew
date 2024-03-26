package com.drag0n.weatherf0recastn3w



import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import android.widget.Toast


object DialogManager {
    fun locationSettingsDialog(context: Context, listener: Listener){
        val builred = AlertDialog.Builder(context)
        val dialog = builred.create()
        dialog.setIcon(R.drawable.ic_location)
        dialog.setTitle(context.getString(R.string.dialog_location_onn_off))
        dialog.setMessage(context.getString(R.string.dialog_location_turn_on))
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.dialog_location_yes)){_,_->
            listener.onClick(null)
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.dialog_location_no)){_,_->
            Toast.makeText(context, context.getString(R.string.dialog_location_toast), Toast.LENGTH_SHORT).show()
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
        dialog.setTitle(context.getString(R.string.dialog_search_city_title))
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.dialog_search_city_button_search)){_,_->
            listener.onClick(edName.text.toString().trim())
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.dialog_search_city_button_back)){_,_->
            dialog.dismiss()
        }

        dialog.show()

    }

    interface Listener{
        fun onClick(city: String?)
    }
}