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
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileOutputStream


interface Utils {

    companion object{
        fun getSPKeyForWidgetId(id:Int):String{
            return "widget_$id"
        }

        fun getImageFromCache(fileName: String, cacheDir:File):Bitmap? {
           runCatching {
               val file = File(cacheDir, fileName)
               val fis = FileInputStream(file)
               val bitmap = BitmapFactory.decodeStream(fis)
               fis.close()
               return bitmap
           }.getOrElse {
               it.printStackTrace()
               return null
           }
        }
        fun storeImageToCache(bitmap: Bitmap, fileName: String,dir:File) {
            Log.e("log","storeImageToCache() called with: bitmap = $bitmap, fileName = $fileName, dir = $dir")
            kotlin.runCatching {
                val file = File(dir, fileName)
                val fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
                fos.close()
            }.exceptionOrNull()?.printStackTrace()
        }

        fun getLaunchFilePendingIntent(path:String, ctx: Context):PendingIntent {
            Log.e("TAG", "path:$path" )

            val uri = Uri.parse(path)
            val openIntent = Intent(Intent.ACTION_VIEW)

            openIntent.setDataAndType(uri, "application/pdf")
            openIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            openIntent.addCategory(Intent.CATEGORY_OPENABLE)

            val sdk31PlusFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
            val pe = PendingIntent.getActivity(ctx,123,openIntent,PendingIntent.FLAG_UPDATE_CURRENT or sdk31PlusFlag)

            return pe
        }

    }

    fun log(s: String) {
        Log.e("TAG", s )
    }

    fun ImageView.newBitMap(config:Bitmap.Config = Bitmap.Config.ARGB_8888):Bitmap?{
        setImageBitmap(null)
        return drawToBitmap(config)
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
    fun ContentResolver.updateBitMapWithPDFPage(bitmap: Bitmap, pageNum:Int, pdfUri: Uri){
        val pfd = openFileDescriptor(pdfUri,"r")?:return
        val renderer = PdfRenderer(pfd)
        val page = renderer.openPage(pageNum)?:return
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
    }



}