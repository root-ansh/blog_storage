package work.curioustools.pdfwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import work.curioustools.pdfwidget.DocumentWidget.Companion.PREFS_NAME
import work.curioustools.pdfwidget.DocumentWidget.Companion.PREF_PREFIX_KEY
import work.curioustools.pdfwidget.databinding.DocumentWidgetConfigureBinding

class DocumentWidgetConfigureActivity : ExternalActivitiesHandlerActivity() {
    private  val binding by lazy { DocumentWidgetConfigureBinding.inflate(layoutInflater) }
    private val appWidgetId by lazy { intent?.extras?.getInt(EXTRA_APPWIDGET_ID)?:INVALID_APPWIDGET_ID  } // we receive a new widget id from system everytime our activity is called

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finishIfUnintendedLaunchOrSetCancelled()
        setContentView(binding.root)

        setPDFUnAvailableUi()


    }

    private fun setPDFUnAvailableUi() {
        currentPage = 0
        maxPages = 1
        fileName  = ""
        authorName = ""
        bookTitle  = ""
        currentUri = null

        with(binding){
            cvInstruction.visibility = View.VISIBLE
            clCreateWidget.visibility = View.GONE
            btChooseFile.setOnClickListener { requestFile(onResult = ::handleDocumentUri) }
            pdfView.visibility = View.INVISIBLE
        }


    }

    private var currentPage = 0
    private var maxPages = 1
    private var fileName: String  = ""
    private var authorName:String = ""
    private var bookTitle : String = ""
    private var currentUri: Uri? = null

    private fun handleDocumentUri(uri: Uri) {
        currentUri = uri
        loadImage {
            maxPages = binding.pdfView.pageCount
            bookTitle = binding.pdfView.documentMeta?.title ?: ""
            authorName = binding.pdfView.documentMeta?.author ?: ""
            setPDFAvailableUI()
        }

    }

    private fun loadImage(loadComplete: OnLoadCompleteListener? = null) {
        val configurator: PDFView.Configurator = binding
            .pdfView
            .fromUri(currentUri)
            .enableSwipe(false)
            .swipeHorizontal(false)
            .enableDoubletap(false)
            .defaultPage(currentPage)
        if(loadComplete!=null)configurator.onLoad (loadComplete)
        configurator.load()
    }

    private fun setPDFAvailableUI() {
        fun loadPageNumber(){
            binding.tvPdfPage.text = "Page: ${currentPage+1}/$maxPages"
        }

        with(binding){
            cvInstruction.visibility = View.GONE
            clCreateWidget.visibility = View.VISIBLE
            pdfView.visibility = View.VISIBLE


            tvTitle.text = bookTitle
            tvAuthor.text = authorName
            tvPDFName.text = fileName
            loadImage()
            loadPageNumber()

            ibtLeft.setOnClickListener {
                if(currentPage<=0)currentPage = 0
                else currentPage--
                loadImage()
                loadPageNumber()
            }
            ibtRight.setOnClickListener {
                if(currentPage<maxPages-1) currentPage++
                else currentPage = maxPages-1
                loadImage()
                loadPageNumber()
            }
            btCreateWidget.setOnClickListener { makeWidget() }
            btUseAnotherPDF.setOnClickListener { setPDFUnAvailableUi() }




        }
    }

    private fun finishIfUnintendedLaunchOrSetCancelled() {
        if (appWidgetId == INVALID_APPWIDGET_ID) {
            // If this activity was started with an intent without an app widget ID, finish with an error.
            finish()
            return
        }
        else{
            setResult(RESULT_CANCELED) // Set the result to CANCELED.  This will cause the widget host to cancel out of the widget placement if the user presses the back button.
        }

    }

    private fun makeWidget(widgetText:String="") {
        val ctx = this?:return

        /* Write the prefix to the SharedPreferences object for this widget */
        val prefs = ctx.getSharedPreferences(PREFS_NAME, 0).edit()
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, widgetText)
        prefs.apply()


        /* It is the responsibility of the configuration activity to update the app widget */
        AppWidgetManager.getInstance(ctx).updateWidget(ctx,appWidgetId)

        val resultValue = Intent().also { it.putExtra(EXTRA_APPWIDGET_ID,appWidgetId) }
        setResult(RESULT_OK, resultValue)
        finish()
    }


}
