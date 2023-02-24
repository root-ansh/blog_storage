package work.curioustools.pdfwidget

import android.net.Uri
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
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