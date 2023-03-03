package work.curioustools.pdfwidget.utils

import android.graphics.Bitmap
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread

interface Lane {
    fun inParallel(action:Runnable)
    fun inMainThread(action:Runnable)
}
class Activity:AppCompatActivity(),Lane{
    override fun inParallel(action: Runnable) {
        Thread(action).start()
    }

    override fun inMainThread(action: Runnable) {
        runOnUiThread(action)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        val i: ImageView? = null
        inParallel{
            val bitmap:Bitmap? = null
            inMainThread{i?.setImageBitmap(bitmap)}
        }
    }

}