package work.curioustools.pdfwidget.utils

import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import android.widget.ImageView
import androidx.core.view.drawToBitmap
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


interface Utils {

    companion object{
        fun getSPKeyForWidgetId(id:Int):String{
            return "widget_$id"
        }
    }
}

fun Context.getReadContentUriPendingIntent(path: String,code:Int = 123): PendingIntent {
   log("getReadContentUriPendingIntent() called with: path = $path")

    val uri = Uri.parse(path)
    val openIntent = Intent(Intent.ACTION_VIEW)

    openIntent.setDataAndType(uri, "application/pdf")
    openIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
    val chooser = Intent.createChooser(openIntent, "Open with")
    val sdk31PlusFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
    return PendingIntent.getActivity(this, code, chooser, PendingIntent.FLAG_UPDATE_CURRENT or sdk31PlusFlag)

}


fun ContentResolver.getFileNameFromUri(uri: Uri):String{
    return when (uri.scheme) {
        ContentResolver.SCHEME_CONTENT -> {
            val cursor = query(uri, null, null, null, null, null) ?: return ""
            cursor.moveToFirst()
            val idxOfColName = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val result = cursor.getString(idxOfColName)
            cursor.close()
            result
        }
        else -> uri.lastPathSegment?:""
    }

}

fun ContentResolver.getPDFPageCount(pdfUri: Uri):Int{
    val pfd = openFileDescriptor(pdfUri,"r")?:return 1
    val renderer = PdfRenderer(pfd)
    return renderer.pageCount
}
fun ContentResolver.updateBitMapWithPDFPage(bitmap: Bitmap?, pageNum:Int, pdfUri: Uri?){
    runCatching {
        bitmap?: error("bitmap is null")
        pdfUri?: error("pdf uri is null")
        val pfd = openFileDescriptor(pdfUri,"r")?: error("cannot create ParcelFileDescriptor")
        val renderer = PdfRenderer(pfd)
        val page = renderer.openPage(pageNum)?: error("page is null")
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        renderer.close()
        page.close()

    }.exceptionOrNull()?.printStackTrace()
}


fun ImageView.newBitMap(config:Bitmap.Config = Bitmap.Config.ARGB_8888):Bitmap{
    setImageBitmap(null)
    return drawToBitmap(config)
}

fun File.toBitMap():Bitmap? {
    runCatching {
        val fis = FileInputStream(this)
        val bitmap = BitmapFactory.decodeStream(fis)
        fis.close()
        return bitmap
    }.getOrElse {
        it.printStackTrace()
        return null
    }
}


fun Bitmap?.saveToFile(file:File) {
    log("saveToFile() called with: bitmap = ${hashCode()}, file:$file")
    kotlin.runCatching {
        this?: error("bitmap is null,can't create file")
        val fos = FileOutputStream(file)
        compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        fos.close()
    }.exceptionOrNull()?.printStackTrace()
}

fun <T> Any.unsafeCast(): T {
    return this as T
}

fun log(s: String) {
    Log.e("PDF_WIDGET", s )
}