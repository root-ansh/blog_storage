package work.curioustools.pdfwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews


class DocumentWidget : AppWidgetProvider() {

    /*
     * We don't have an option to customise widget creation process as it is done by the OS itself.
     * We can just update the widgets once they are created, or at regular intervals
     * */


    /* There may be multiple widgets active, so update all of them */
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { appWidgetManager.updateWidget(context,it) }
    }

    /* When the user deletes the widget, delete the preference associated with it. */
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        appWidgetIds.forEach {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
            prefs.remove(PREF_PREFIX_KEY + it)
            prefs.apply()
        }

    }

    /* Enter relevant functionality for when the first widget is created */
    override fun onEnabled(context: Context) {}

    /* Enter relevant functionality for when the last widget is disabled */
    override fun onDisabled(context: Context) {}

    companion object{
        const val PREFS_NAME = "pdf_widget_current_widgets"
        const val PREF_PREFIX_KEY = "appwidget_"

        fun AppWidgetManager.updateWidget(context: Context, appWidgetId: Int){
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            val titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null)
            val widgetText =  titleValue ?: context.getString(R.string.appwidget_text)

            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.document_widget)
            views.setTextViewText(R.id.appwidget_text, widgetText)

            // Instruct the widget manager to update the widget
            updateAppWidget(appWidgetId, views)
        }
    }

}




