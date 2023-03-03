package work.curioustools.pdfwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View

import work.curioustools.pdfwidget.databinding.DocumentWidgetConfigureBinding
import work.curioustools.pdfwidget.utils.*
import java.io.File

import kotlin.concurrent.thread

class DocumentWidgetConfigureActivity : ExternalActivitiesHandlerActivity(), Utils {
    private  val binding by lazy { DocumentWidgetConfigureBinding.inflate(layoutInflater) }
    private val appWidgetId by lazy { intent?.extras?.getInt(EXTRA_APPWIDGET_ID)?:INVALID_APPWIDGET_ID  } // we receive a new widget id from system everytime our activity is called
    private var currentPage = 0
    private var maxPages = 1
    private var fileName: String  = ""
    private var bitmap: Bitmap? = null
    private var currentPDFUri: Uri? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finishIfUnintendedLaunchOrSetCancelled()
        setContentView(binding.root)
        supportActionBar?.title = "Configure Your Widget"
        setPDFUnAvailableUi()
    }

    private fun setPDFUnAvailableUi() {
        currentPage = 0
        maxPages = 1
        fileName  = ""
        bitmap = null
        currentPDFUri = null

        with(binding){
            cvInstruction.visibility = View.VISIBLE
            clCreateWidget.visibility = View.GONE
            btChooseFile.setOnClickListener { requestFile(onResult = ::handleDocumentUri) }
            ivPdfView.visibility = View.INVISIBLE
        }
    }

    private fun handleDocumentUri(uri: Uri){
        contentResolver.takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        currentPDFUri = uri
        fun setPageDetailsAndImage(pageNum:Int){
            if (pageNum !in 0.until(maxPages)) return
            currentPage = pageNum
            bitmap = binding.ivPdfView.newBitMap()

            thread {
                maxPages = contentResolver.getPDFPageCount(uri)
                runOnUiThread { binding.tvPdfPage.text = "Page: ${currentPage+1}/$maxPages" }
            }

            thread {
                contentResolver.updateBitMapWithPDFPage(bitmap!!,currentPage,uri)
                runOnUiThread { binding.ivPdfView.setImageBitmap(bitmap) }
            }
        }
        fun setFileName(){
            thread {
                fileName = contentResolver.getFileNameFromUri(uri)
                runOnUiThread { binding.tvTitle.text = fileName }
            }
        }

        with(binding){
            cvInstruction.visibility = View.GONE
            clCreateWidget.visibility = View.VISIBLE
            ivPdfView.visibility = View.VISIBLE

            setPageDetailsAndImage(0)
            setFileName()

            ibtLeft.setOnClickListener { setPageDetailsAndImage(currentPage-1) }
            ibtRight.setOnClickListener { setPageDetailsAndImage(currentPage+1) }
            btCreateWidget.setOnClickListener { makeWidget() }
            btUseAnotherPDF.setOnClickListener { setPDFUnAvailableUi() }
        }
    }
    private fun finishIfUnintendedLaunchOrSetCancelled() {
        if (appWidgetId == INVALID_APPWIDGET_ID) {
            finish()
            return
        }
        else{
            // If this activity was started with an intent without an app widget ID, finish with an error.
            // Set the result to CANCELED.  This will cause the widget host to cancel out of the widget placement if the user presses the back button.
            setResult(RESULT_CANCELED)
        }

    }

    private fun makeWidget() {
        val ctx = this
        val spKey = Utils.getSPKeyForWidgetId(appWidgetId)
        val isChecked = binding.cbShowName.isChecked
        val sp = SafePrefs.instance(ctx)
        val file = File(ctx.filesDir,spKey)
        log("spkey:$spKey | isChecked:$isChecked | bitmap: ${bitmap?.hashCode()} | File:$file")

        thread {
            sp.putString("${spKey}_uri",currentPDFUri.toString(),immediate = true)
            if(isChecked) { sp.putString("${spKey}_name", fileName, immediate = true) }
            bitmap.saveToFile(file)

            runOnUiThread {
                val mgr = AppWidgetManager.getInstance(ctx)
                DocumentWidget.updateWidgetUI(ctx,appWidgetId,mgr)

                val resultValue = Intent().also { it.putExtra(EXTRA_APPWIDGET_ID,appWidgetId) }
                setResult(RESULT_OK, resultValue)
                finish()
            }
        }
    }


}
