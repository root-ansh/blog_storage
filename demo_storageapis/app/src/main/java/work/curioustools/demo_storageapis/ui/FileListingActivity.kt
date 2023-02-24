package work.curioustools.demo_storageapis.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import work.curioustools.demo_storageapis.*
import kotlin.concurrent.thread


class FileListingActivity : LegacyPermissionActivity(), FileModelFromSAF, FileModelFromLegacyUris {
    private val adp = FileContentAdapter()
    private val cru by lazy { FileModelFromMediaUris(contentResolver) }



    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_listing)

        findViewById<RecyclerView>(R.id.rvFiles).run {
            layoutManager = LinearLayoutManager(this@FileListingActivity)
            adapter = adp
        }
    }

    private fun updateAdp(items:List<FileModel>, setVertical:Boolean=false){
        runOnUiThread {
            adp.files.clear()
            adp.files.addAll(items)
            adp.verticalFolder = setVertical
            adp.notifyDataSetChanged()
        }

    }


    @SuppressLint("MissingPermission")
    val entries = listOf(
        "toggle management permission(spl)" to {
            runOnUiThread {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    startActivity(intent)
                } else toast("permission not available. your device has os that is less than android R(${Build.VERSION_CODES.R})")



            }
        },
        "toggle usual permission" to {
            runOnUiThread {
                val uri =  Uri.fromParts("package", packageName, null)
                val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,uri)
                startActivity(i)
            }
        },
        "pdf and cbr files using SAF" to {requestFolderFromSAF()},
        "all files using brute force recursion" to {updateAdp(getAllFilesAndFoldersUsingBruteForceRecursion())},
        "media files using cru (all) " to {updateAdp(cru.getAllMediaFiles())},
        "media files using cru (only image) " to {updateAdp(cru.getSingleMedia())},
        "media files using cru (multiple) " to {updateAdp(cru.getMultipleMedia(arrayOf("video/%","audio/%")))},
        "All external paths" to {updateAdp( getAllRawPathAccesses(this),true) },
        )

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?:return false
        entries.forEachIndexed {i,entry ->
            menu.add(Menu.NONE, i, Menu.NONE,entry.first ).setOnMenuItemClickListener {
                thread { entry.second() }
                true
            }
        }



        return true
    }


    override fun onSAFFilesReceived(entries: List<FileModel>) {
        updateAdp(entries)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handleSAFResults(requestCode,resultCode,data)
    }



}


