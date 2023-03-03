package work.curioustools.pdfwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.RemoteViews
import androidx.core.net.toFile
import work.curioustools.pdfwidget.utils.*
import java.io.File
import kotlin.concurrent.thread


/**
 * We don't have an option to customise widget creation process as it is done by the OS itself.
 * We can just update the widgets once they are created, or at regular intervals, and that too
 * using AppWidgetManager
 *
 * So essentially an instance of widget is nothing but an AppWidgetProvider Callback
 */
class DocumentWidget : AppWidgetProvider(),Utils {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        /* update all widgets */
        appWidgetIds.forEach { updateWidgetUI(context,it,appWidgetManager) }
    }
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        /* delete all widgets */
        appWidgetIds.forEach { removeWidgetContent(context,it) }
    }

    companion object{
        fun updateWidgetUI(context: Context, appWidgetId: Int, manager: AppWidgetManager){
            val sp = SafePrefs.instance(context)
            val prefKey = Utils.getSPKeyForWidgetId(appWidgetId)
            val titleValue =  sp.getString("${prefKey}_name")
            val uriString = sp.getString("${prefKey}_uri")
            val file = File(context.filesDir,prefKey)
            log("key:$prefKey | title:$titleValue| uriString:$uriString | file:$file")

            thread {
                val bitmap:Bitmap? = file.toBitMap()
                log("bitmap:${bitmap?.hashCode()} ")

                Handler(Looper.getMainLooper()).post {
                    val views = RemoteViews(context.packageName, R.layout.document_widget)
                    if (titleValue == null) views.setViewVisibility(R.id.appwidget_text,View.GONE)
                    else {
                        views.setViewVisibility(R.id.appwidget_text,View.VISIBLE)
                        views.setTextViewText(R.id.appwidget_text, titleValue)
                    }

                    if (bitmap != null) {
                        views.setImageViewBitmap(R.id.ivThumb, bitmap)
                    }

                    if(uriString!=null){
                        kotlin.runCatching {
                            val intent =context.getReadContentUriPendingIntent(uriString)
                            views.setOnClickPendingIntent(R.id.ivThumb,intent)
                        }
                    }
                    // Instruct the widget manager to update the widget
                    manager.updateAppWidget(appWidgetId, views)
                }
            }


        }

        fun removeWidgetContent(context: Context,id:Int){
            val prefKey = Utils.getSPKeyForWidgetId(id)
            context.deleteFile(prefKey)
            SafePrefs.instance(context).remove(prefKey)
        }

    }

}






