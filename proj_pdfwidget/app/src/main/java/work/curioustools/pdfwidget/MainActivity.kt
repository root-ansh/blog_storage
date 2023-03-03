package work.curioustools.pdfwidget

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import kotlin.concurrent.thread


class MainActivity : ExternalActivitiesHandlerActivity() {
    private val tvInstruction by lazy { findViewById<MaterialTextView>(R.id.tvInstructions) }
    private val tvPermissionStatus by lazy { findViewById<MaterialTextView>(R.id.tvPermissionStatus) }
    private val btAction by lazy { findViewById<MaterialButton>(R.id.btAction) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //getPermissions()
    }

    fun getPermissions(){
        val onPermissionsResponse =  {permissionMap: Map<String, Boolean> ->
            val hasAllPermissions = permissionMap.values.reduce { acc, b -> acc && b }
            updateUi(hasAllPermissions)
        }
        thread {
            Thread.sleep(1000)
            runOnUiThread { requestPermission(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, onResult =  onPermissionsResponse) }
        }
    }

    private fun updateUi(isPermissionAvailable: Boolean) {
        if(isPermissionAvailable){
            tvPermissionStatus.text = getString(R.string.msg_permission)
            btAction.text= getString(R.string.action_permission)
            btAction.setOnClickListener {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=wpiRZJyQphI"))
                startActivity(webIntent)

            }
        }
        else{
            tvPermissionStatus.setText(R.string.msg_no_permission)
            btAction.setText(R.string.action_no_permission)
            btAction.setOnClickListener { getPermissions() }
        }

    }

    private fun temp(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}