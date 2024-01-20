package com.drag0n.weatherf0recastn3w

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.view.ViewGroup
import android.widget.RemoteViews
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.room.Room
import com.drag0n.weatherf0recastn3w.Data.RoomWeather.WeatherDayNowDB

/**
 * Implementation of App Widget functionality.
 */
class WeatherWidget : AppWidgetProvider() {
    private lateinit var weatherDB: WeatherDayNowDB
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)

        }
        weatherDB = Room.databaseBuilder(context,
            WeatherDayNowDB::class.java,
            "Текущая погода на день").build()
        val insert = weatherDB.CourseDao().getAll()
        val view = RemoteViews(context.packageName,R.layout.weather_widget)
        Thread{
            view.setTextViewText(R.id.appwidget_text, insert.name)

        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.weather_widget)

    views.setTextViewText(R.id.appwidget_text, "widgetText")

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)

}