package work.curioustools.demo_storageapis

import android.content.Context
import android.widget.Toast
import java.text.CharacterIterator
import java.text.StringCharacterIterator
import java.util.Locale
import kotlin.math.abs

inline fun runLogging(block: () -> Unit) {
    return try {
        block()
    } catch (e: Throwable) {
        //e.printStackTrace()
    }
}

fun Long.toMemoryString(): String {
    val bytes = this
    val absB = if (bytes == Long.MIN_VALUE) Long.MAX_VALUE else abs(bytes)
    if (absB < 1024) {
        return "$bytes B"
    }
    var value = absB
    val ci: CharacterIterator = StringCharacterIterator("KMGTPE")
    var i = 40
    while (i >= 0 && absB > 0xfffccccccccccccL shr i) {
        value = value shr 10
        ci.next()
        i -= 10
    }
    value *= java.lang.Long.signum(bytes).toLong()
    return String.format(Locale.getDefault(),"%.1f %ciB", value / 1024.0, ci.current())
}



fun Context.toast(msg:String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}