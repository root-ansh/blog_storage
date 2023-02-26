package work.curioustools.pdfwidget

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

@SuppressLint("commitSharedPref")
class SafePrefs private constructor(
    private val systemSP:SharedPreferences,
    private val inMemorySP:MutableMap<String,Any?> = hashMapOf()
){
    init {
        systemSP.all.map { inMemorySP[it.key]=it.value }
    }


    fun getAll():Map<String,Any?> = inMemorySP
    fun getString(key: String):String? = inMemorySP[key]?.unsafeCast()
    fun getStringSet(key: String):Set<String>? = inMemorySP[key]?.unsafeCast()
    fun getInt(key: String):Int? = inMemorySP[key]?.unsafeCast()
    fun getLong(key: String):Long? = inMemorySP[key]?.unsafeCast()
    fun getFloat(key: String):Float? = inMemorySP[key]?.unsafeCast()
    fun getBoolean(key: String):Boolean? = inMemorySP[key]?.unsafeCast()


    fun contains(key: String):Boolean = inMemorySP.contains(key)
    fun clearAll(){
        inMemorySP.clear()
        runInParallel { systemSP.edit().clear().commit() }
    }

    
    fun putString(key: String, value: String?){
        inMemorySP[key]=value
        runInParallel { systemSP.edit().putString(key, value).commit() }
    }

    fun putStringSet(key: String, value: MutableSet<String>?){
        inMemorySP[key]=value
        runInParallel { systemSP.edit().putStringSet(key, value).commit() }
    }

    fun putInt(key: String, value: Int){
        inMemorySP[key]=value
        runInParallel { systemSP.edit().putInt(key, value).commit() }
    }

    fun putLong(key: String, value: Long){
        inMemorySP[key]=value
        runInParallel { systemSP.edit().putLong(key, value).commit() }
    }

    fun putFloat(key: String, value: Float){
        inMemorySP[key]=value
        runInParallel { systemSP.edit().putFloat(key, value).commit() }
    }

    fun putBoolean(key: String, value: Boolean){
        inMemorySP[key]=value
        runInParallel { systemSP.edit().putBoolean(key, value).commit() }
    }

    fun remove(key: String){
        inMemorySP.remove(key)
        runInParallel { systemSP.edit().remove(key).commit() }
    }
    
    
    private fun runInParallel(task:()->Unit){

        AppExecutor.sharedQueueTask().execute(task)

        
    }

    

    companion object{
        private var singleton: SafePrefs? = null
        fun instance(appCtx: Context): SafePrefs {
            if (singleton != null) return singleton!!
            synchronized(SafePrefs::class.java) {
                if (singleton != null) return singleton!!
                else {
                    val sp = appCtx.applicationContext.getSharedPreferences("DEFAULT_SP", Context.MODE_PRIVATE)
                    singleton = SafePrefs(sp)
                    singleton
                    return singleton!!
                }
            }
        }
    }


    fun <T> Any.unsafeCast():T{
        return this as T
    }
    

}
