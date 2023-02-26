package work.curioustools.pdfwidget

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

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

        val task = {
            Thread.sleep(100)
            println(".")
        }
        AppExecutor.sharedQueueTask().execute(task)
        AppExecutor.ioTask().execute(task)
        AppExecutor.sharedQueueTask().execute(task)
        AppExecutor.ioTask().execute(task)
        AppExecutor.sharedQueueTask().execute(task)
        AppExecutor.ioTask().execute(task)
        AppExecutor.sharedQueueTask().execute(task)
        AppExecutor.ioTask().execute(task)
        AppExecutor.sharedQueueTask().execute(task)
        AppExecutor.ioTask().execute(task)
        AppExecutor.sharedQueueTask().execute(task)
        AppExecutor.ioTask().execute(task)
        AppExecutor.ioTask().execute(task)
        AppExecutor.ioTask().execute(task)
        AppExecutor.ioTask().execute(task)
        AppExecutor.ioTask().execute(task)

        AppExecutor.sharedQueueTask().execute(task)
        AppExecutor.sharedQueueTask().execute(task)
        AppExecutor.sharedQueueTask().execute(task)
        AppExecutor.sharedQueueTask().execute(task)

        println("main finished, waiting for other threads to finish")
        Thread.sleep(3000)



    }

}