package work.curioustools.pdfwidget

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import work.curioustools.pdfwidget.utils.AppExecutor

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {



    @Test
    fun addition_isCorrect() {

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        fun task(id:String): () -> Unit {
            return {
                Thread.sleep(100)
                println("executed: $id")
            }
        }
        AppExecutor.sharedQueueTask().execute(task("sq1"))
        AppExecutor.ioTask().execute(task("io1"))
        AppExecutor.sharedQueueTask().execute(task("sq2"))
        AppExecutor.ioTask().execute(task("io2"))
        AppExecutor.sharedQueueTask().execute(task("sq3"))
        AppExecutor.ioTask().execute(task("io3"))
        AppExecutor.sharedQueueTask().execute(task("sq4"))
        AppExecutor.ioTask().execute(task("io4"))
        AppExecutor.sharedQueueTask().execute(task("sq5"))
        AppExecutor.ioTask().execute(task("io5"))
        AppExecutor.sharedQueueTask().execute(task("sq6"))
        AppExecutor.ioTask().execute(task("io6"))
        AppExecutor.ioTask().execute(task("io7"))
        AppExecutor.ioTask().execute(task("io8"))
        AppExecutor.ioTask().execute(task("io9"))
        AppExecutor.ioTask().execute(task("io10"))

        AppExecutor.sharedQueueTask().execute(task("sq7"))
        AppExecutor.sharedQueueTask().execute(task("sq8"))
        AppExecutor.sharedQueueTask().execute(task("sq9"))
        AppExecutor.sharedQueueTask().execute(task("sq10"))

        println("main finished, waiting for other threads to finish")
        Thread.sleep(3000)



    }

}