package work.curioustools.demo_storageapis.ui

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


open class LegacyPermissionActivity:AppCompatActivity(), ActivityUtils {
    private val rcPermissions = 100

    private var onRecieveSuccess: (() -> Unit)? = null


    fun requestPermission(permission:String,onSuccess:()->Unit){
        onRecieveSuccess = onSuccess
        val hasPermission = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), rcPermissions)
        }
        else{
            toast("permission already granted: $permission")
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==rcPermissions && grantResults.getOrNull(0)==PackageManager.PERMISSION_GRANTED){
            toast("permission available: ${permissions.getOrNull(0)}")
            onRecieveSuccess?.invoke()
        }
        else{
            toast("permission rejected by user. requesting via system call")
        }

    }
}


