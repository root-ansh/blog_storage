package work.curioustools.pdfwidget

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SPTest {
    private lateinit var prefs: SafePrefs

    @Before
    fun before() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        prefs = SafePrefs.instance(appContext)
    }

    @Test
    fun testConcurrentWritesToSamePreferenceKey() {

        val t1 =Thread{prefs.putInt("key",1)}
        val t2 =Thread{prefs.putInt("key",2)}


        t1.start()
        t2.start()

        Thread.sleep(1000)

        assertEquals(2, prefs.getInt("key"))
    }

    @Test
    fun testConcurrentReadsToSamePreferenceKey() {
        

        // Save a preference value
        prefs.putInt("my_key", 1)

        // Start two threads that read the preference value
        val thread1 = Thread {
            val value = prefs.getInt("my_key")
            assertEquals(1, value)
        }

        val thread2 = Thread {
            val value = prefs.getInt("my_key")
            assertEquals(1, value)
        }

        thread1.start()
        thread2.start()

        // Wait for both threads to finish
        thread1.join()
        thread2.join()
    }

    @Test
    fun testConcurrentReadsAndWritesToDifferentPreferenceKeys() {
        

        // Start two threads that read and write to different preference keys
        val thread1 = Thread {
            prefs.putInt("key1", 1)
            val value = prefs.getInt("key2")
            assertEquals(0, value)
        }

        val thread2 = Thread {
            prefs.putInt("key2", 2)
            val value = prefs.getInt("key1")
            assertEquals(0, value)
        }

        thread1.start()
        thread2.start()

        // Wait for both threads to finish
        thread1.join()
        thread2.join()

        // Verify that the preference values are correct
        assertEquals(1, prefs.getInt("key1"))
        assertEquals(2, prefs.getInt("key2"))
    }


    @Test
    fun testRemovePreferenceKey() {
        

        // Save a preference value
        prefs.putInt("my_key", 1)

        // Start two threads that remove the preference key
        val thread1 = Thread {
            prefs.remove("my_key")
        }

        val thread2 = Thread {
            prefs.remove("my_key")
        }

        thread1.start()
        thread2.start()

        // Wait for both threads to finish
        thread1.join()
        thread2.join()

        // Verify that the preference key is removed
        assertFalse(prefs.contains("my_key"))
    }

    @Test
    fun testClearAllPreferences() {
        

        // Save some preference values
        prefs.putInt("key1", 1)
        prefs.putBoolean("key2", true)

        // Start two threads that clear all preferences
        val thread1 = Thread {
            prefs.clearAll()
        }

        val thread2 = Thread {
            prefs.clearAll()
        }

        thread1.start()
        thread2.start()

        // Wait for both threads to finish
        thread1.join()
        thread2.join()

        // Verify that all preferences are cleared
        assertFalse(prefs.contains("key1"))
        assertFalse(prefs.contains("key2"))
    }




}