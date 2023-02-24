package work.curioustools.demo_storageapis.ui

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

abstract class ModernPermissionActivity: AppCompatActivity(){

    private lateinit var arl: ActivityResultLauncher<Array<String>>
    private val requestType = ActivityResultContracts.RequestMultiplePermissions()
    private val TAG = "PMA>>"
    override fun onStart() {
        super.onStart()
        arl = registerForActivityResult(requestType, ::onPermissionsResponse)
        Log.e(TAG, "onStart: activity result launcher initialised.")

    }

    /**
     *  note: make sure to  call after [AppCompatActivity.onStart].
     *  prefer to not call in automatic flow (i.e first line in lifecycle functions)
     *  prefer to not call in onResume/onStart as these functions get called multiple times
     *  ideal place to call this function is onCreate with a delayed handler
     * */
    fun requestPermission(vararg  permissions:String) {
        Log.e(TAG, "requestForPermission() called with: permissions = $permissions")
        arl.launch(permissions.toList().toTypedArray())
        // val permissionsArray = arrayOf()
    }


    open fun onPermissionsResponse(permissionMap: Map<String,  Boolean>) {}

}