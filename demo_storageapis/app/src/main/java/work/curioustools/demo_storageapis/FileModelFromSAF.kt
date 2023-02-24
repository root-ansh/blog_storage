package work.curioustools.demo_storageapis

import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import work.curioustools.demo_storageapis.ui.FileModel
import work.curioustools.demo_storageapis.ui.TypeUI
import java.util.*
import kotlin.concurrent.thread


interface FileModelFromSAF{
    fun getRcSAF() = 111


    fun AppCompatActivity.requestFolderFromSAF(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val initialUri = Environment.getExternalStorageDirectory()
                it.putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialUri)
                it.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION or FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        }
        startActivityForResult(intent, getRcSAF())
    }

    fun AppCompatActivity.handleSAFResults(requestCode: Int, resultCode: Int, resultData: Intent?) {
        println("handleSAFResults() called with: requestCode = $requestCode, resultCode = $resultCode, resultData = $resultData")

        val ctx = this
        val isOK = resultCode == AppCompatActivity.RESULT_OK
        val isSAF = requestCode == getRcSAF()
        val resultUri: Uri? = resultData?.data
        println("isOK: $isOK | isSAF:$isSAF | resultUri:${resultUri?.toJson()}")
        if(!isSAF || !isOK || resultUri==null) {
            return
        }

        ctx.contentResolver.takePersistableUriPermission(resultUri, FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION)

        val id: String = DocumentsContract.getTreeDocumentId(resultUri)
        println("id=$id")

        val treeUri: Uri = DocumentsContract.buildTreeDocumentUri(resultUri.authority, id)
        println("treeUri= $treeUri")

        val treeFile: DocumentFile? = DocumentFile.fromTreeUri(ctx, treeUri)
        println("treeFile= $treeFile")

        if(treeFile!=null) {
            thread {
                val result = mutableListOf<FileModel>()
                convertToFileModels(treeFile, result)
                ctx.runOnUiThread { onSAFFilesReceived(result) }
            }
        }
    }

    private fun convertToFileModels(file: DocumentFile, fileList: MutableList<FileModel>) {
        val str = "${file.name} (${file.type}) - Last Modified: ${Date(file.lastModified())}"
        println(str)
        val name = file.name?:""
        val mimeType = file.type?:"?/?"
        val ui = TypeUI.getTypeFromFileName(mimeType.split("/").last())
        val path =  file.uri.path

        if (file.isDirectory) {
            val model = FileModel(name, "${TypeUI.FOLDER.emoji}$path",  "", internalItemsSize = file.listFiles().size.toString(), res = TypeUI.FOLDER.icon, mimeType)

            fileList.add(model)
            for (childFile in file.listFiles()) {
                convertToFileModels(childFile, fileList)
            }
        } else {
            val model = FileModel(name, "${ui.emoji}$path",  file.length().toMemoryString(), internalItemsSize = "", res =ui.icon, mimeType)
            fileList.add(model)
        }
    }

    fun onSAFFilesReceived(entries:List<FileModel>){}


}


