package work.curioustools.pdfwidget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID
import android.content.Intent
import android.os.Bundle
import work.curioustools.pdfwidget.DocumentWidget.Companion.PREFS_NAME
import work.curioustools.pdfwidget.DocumentWidget.Companion.PREF_PREFIX_KEY
import work.curioustools.pdfwidget.DocumentWidget.Companion.updateWidget
import work.curioustools.pdfwidget.databinding.DocumentWidgetConfigureBinding


class DocumentWidgetConfigureActivity : Activity() {

    private var appWidgetId: Int = INVALID_APPWIDGET_ID

    private  val binding by lazy { DocumentWidgetConfigureBinding.inflate(layoutInflater) }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setResult(RESULT_CANCELED) // Set the result to CANCELED.  This will cause the widget host to cancel out of the widget placement if the user presses the back button.

        setContentView(binding.root)

        appWidgetId =intent?.extras?.getInt(EXTRA_APPWIDGET_ID)?:INVALID_APPWIDGET_ID // we receive a new widget id from system everytime our activity is called

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        with(binding){
            addButton.setOnClickListener {
                val ctx = root.context?:return@setOnClickListener

                val widgetText = appwidgetText.text.toString()


                /* Write the prefix to the SharedPreferences object for this widget */
                val prefs = ctx.getSharedPreferences(PREFS_NAME, 0).edit()
                prefs.putString(PREF_PREFIX_KEY + appWidgetId, widgetText)
                prefs.apply()


                /* It is the responsibility of the configuration activity to update the app widget */
                val appWidgetManager = AppWidgetManager.getInstance(ctx)
                appWidgetManager.updateWidget(ctx,appWidgetId)

                val resultValue = Intent().also { it.putExtra(EXTRA_APPWIDGET_ID,appWidgetId) }
                setResult(RESULT_OK, resultValue)
                finish()
            }
        }


    }



}
