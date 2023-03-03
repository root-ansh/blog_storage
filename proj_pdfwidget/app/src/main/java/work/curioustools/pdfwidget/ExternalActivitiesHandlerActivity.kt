package work.curioustools.pdfwidget

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

open  class  ExternalActivitiesHandlerActivity:AppCompatActivity(){

    private lateinit var arlPickFile: ActivityResultLauncher<Intent>
    private var callbackPickFile : (Uri) -> Unit = {}

    private lateinit var arlPermission : ActivityResultLauncher<Array<String>>
    private var callbackPermission: (Map<String,  Boolean>) -> Unit = {}


    private fun initARLPickFile(){
        val requestType = ActivityResultContracts.StartActivityForResult()
        arlPickFile = registerForActivityResult(requestType){
            val result = it?.resultCode?:RESULT_CANCELED;
            if(result!= RESULT_OK) return@registerForActivityResult
            val data = it?.data?.data?:return@registerForActivityResult
            callbackPickFile.invoke(data)
            callbackPickFile = {}
        }
    }

    private fun initARLPermission(){
        val requestType = ActivityResultContracts.RequestMultiplePermissions()
        arlPermission = registerForActivityResult(requestType){
            callbackPermission.invoke(it)
            callbackPermission = {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initARLPickFile()
        initARLPermission()

    }

    fun requestFile(category: String = Intent.CATEGORY_OPENABLE, filter : String = "application/pdf", onResult:(Uri)->Unit = {}) {
        callbackPickFile = onResult
        val baseIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        baseIntent.addCategory(category)
        baseIntent.type = filter
        arlPickFile.launch(baseIntent)

    }

    fun requestPermission(vararg  permissions:String, onResult: (Map<String,  Boolean>) -> Unit = {}) {
        callbackPermission = onResult
        arlPermission.launch(permissions.toList().toTypedArray())
    }


}

open  class  EAHActivity:AppCompatActivity(){

    private lateinit var arlPickFile: ActivityResultLauncher<Intent>
    private var callbackPickFile : (Uri) -> Unit = {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requestType = ActivityResultContracts.StartActivityForResult()
        arlPickFile = registerForActivityResult(requestType){
            val result = it?.resultCode?:RESULT_CANCELED;
            if(result!= RESULT_OK) return@registerForActivityResult
            val data = it?.data?.data?:return@registerForActivityResult
            callbackPickFile.invoke(data)
            callbackPickFile = {}
        }
    }


    fun requestFile(category: String = Intent.CATEGORY_OPENABLE, filter : String = "application/pdf", onResult:(Uri)->Unit = {}) {
        callbackPickFile = onResult
        val baseIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        baseIntent.addCategory(category)
        baseIntent.type = filter
        arlPickFile.launch(baseIntent)

    }


}