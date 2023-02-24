package work.curioustools.pdfwidget

import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.json.JSONArray
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import work.curioustools.pdfwidget.testing_storage.toFileModel
import work.curioustools.pdfwidget.testing_storage.toJson
import work.curioustools.pdfwidget.testing_storage.traverseRecursively
import java.io.File
import kotlin.system.measureTimeMillis

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

//    // Recursive method to traverse the file system
//    private void traverseFileSystem(File directory, List<File> fileList) {
//        // Get the list of files in the directory
//        File[] files = directory.listFiles();
//
//        // Add the files to the list
//        for (File file : files) {
//            fileList.add(file);
//
//            // Recursively traverse subdirectories
//            if (file.isDirectory()) {
//                traverseFileSystem(file, fileList);
//            }
//        }
//    }



    @Test
    fun testParsingTime(){
        val startFile = Uri.parse("file:///storage/emulated/0").toFile()
        val fileList = mutableListOf<File>()
        println("starting recursion")
        var timeTaken = measureTimeMillis {
            startFile.traverseRecursively(fileList)
        }
        println("finished recursion. | timeTaken=$timeTaken")

        val jsonArray = JSONArray()
        println("starting parsing")
        timeTaken = measureTimeMillis {
            fileList.forEach {
                val json = it.toUri().toFileModel().toJson()
                jsonArray.put(json)
            }
        }
        println("finished parsing. | timeTaken=$timeTaken")
        logLargeString(jsonArray.toString())



    }

    @Test
    fun addition_isCorrect() {

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val uris = listOf(
            "urn:isbn:0–486–27557–4",
            "https://api.example.com:8080/user/ansh1234?data=profile#basic",  //[  (Protocol:https://)    (subdomain:api)    (domain:example.com)    (port: :8080)    (path: /user)    (pathparam: /ansh1234)    (queryparam: ?data=profile) (fragment : #basic)
            "mailto:user@example.com",
            "ftp://ftp.example.com/file.txt",
            "data:image/png;base64,iVBORw0KGg...",
        )
        val jsonArray = JSONArray()
        val storageUris = listOf(
            /* file   framework based uri for a file: */ "file:///storage/emulated/0/Pictures/example.jpeg",
            /* file   framework based uri for a file2: */ "file:///storage/emulated/0/Download/example.pdf",
            /* file   framework based uri for a folder: */ "file:///storage/emulated/0/Pictures",
            /* content framework based uri for a file: */ "content://com.android.providers.downloads.documents/document/123",
            /* content framework based uri for a file2: */ "content://com.android.providers.media.documents/document/image:123",
            /* content framework based uri for a folder: */ "content://com.android.externalstorage.documents/tree/primary%3AMusic",
        ).map {
            val json = Uri.parse(it).toJson(appContext, true)
            jsonArray.put(json)
        }

        println("=======")
        logLargeString(jsonArray.toString())


    }
    fun logLargeString(str: String) {
        if (str.length > 2000) {
            println( str.substring(0, 2000))
            logLargeString(str.substring(2000))
        } else {
            println( str) // continuation
        }
    }


    @Test
    fun checkschema() {
        assertEquals(4, 2 + 2)
        val uris = listOf(
            "urn:isbn:0–486–27557–4",
            "https://api.example.com:8080/user/ansh1234?data=profile#basic",  //[  (Protocol:https://)    (subdomain:api)    (domain:example.com)    (port: :8080)    (path: /user)    (pathparam: /ansh1234)    (queryparam: ?data=profile) (fragment : #basic)
            "mailto:user@example.com",
            "ftp://ftp.example.com/file.txt",
            "data:image/png;base64,iVBORw0KGg...",
        )
        val storageUris = listOf(
            /* file   framework based uri for a file: */ "file:///storage/emulated/0/Pictures/example.jpg",
            /* file   framework based uri for a file2: */ "file:///storage/emulated/0/Download/example.pdf",
            /* file   framework based uri for a folder: */ "file:///storage/emulated/0/Download",
            /* content framework based uri for a file: */ "content://com.android.providers.downloads.documents/document/123",
            /* content framework based uri for a file2: */ "content://com.android.providers.media.documents/document/image:123",
            /* content framework based uri for a folder: */ "content://com.android.externalstorage.documents/tree/primary%3AMusic",
        )
        listOf(uris,storageUris).flatten().forEach {
            println(Uri.parse(it).scheme)
        }

    }
}