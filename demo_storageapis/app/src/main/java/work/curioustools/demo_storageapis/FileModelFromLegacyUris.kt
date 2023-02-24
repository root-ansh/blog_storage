package work.curioustools.demo_storageapis

import android.content.Context
import android.os.Build
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.annotation.WorkerThread
import androidx.core.net.toUri
import work.curioustools.demo_storageapis.ui.FileModel
import work.curioustools.demo_storageapis.ui.TypeUI
import java.io.File

interface FileModelFromLegacyUris{

    @WorkerThread
    fun getAllRawPathAccesses(context: Context):List<FileModel>{
        val f = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.getStorageDirectory()
        } else { null}
        val f2 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { context.dataDir }else { null }
        val paths: List<FileModel> = mapOf<String, File?>(
            "e.getDataDirectory" to Environment.getDataDirectory(),
            "e.getDownloadCacheDirectory" to Environment.getDownloadCacheDirectory(),
            "e.getExternalStorageDirectory" to Environment.getExternalStorageDirectory(),
            "e.getExternalStoragePublicDirectory" to Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM
            ),
            "e.getRootDirectory" to Environment.getRootDirectory(),
            "e.getStorageDirectory" to f,
            "c.cacheDir" to context.cacheDir,
            "c.codeCacheDir" to context.codeCacheDir,
            "c.dataDir" to f2,
            "c.externalCacheDir" to context.externalCacheDir,
            "c.filesDir" to context.filesDir,
            "c.noBackupFilesDir" to context.noBackupFilesDir,
            "c.obbDir" to context.obbDir,
        ).map {
            val file = it.value
            if(file== Environment.getExternalStorageDirectory()){
                runCatching {
                    val title = it.key+"(${file?.absolutePath?:"null"})"
                    println("title=$title")
                    println(file?.toUri()?.toJson(context,true))
                }
            }
            val title = it.key+"(${file?.absolutePath?:"null"})"

            val children = (file?.listFiles()?: emptyArray()).map { child ->
                val name = child.name
                if(child.isDirectory) TypeUI.FOLDER.emoji+name
                else TypeUI.getTypeFromFileName(name).emoji+name
            }.joinToString(",\t\t")
            val childrenSize = (file?.listFiles()?.size?:0).toString()

            val extension =
                MimeTypeMap.getFileExtensionFromUrl(file?.path ?: "") // uriAsFile.extension
            val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)?:"?/?"

            FileModel(title, children, "", childrenSize, TypeUI.FOLDER.icon, mimetype)
        }
        return paths


    }


    @WorkerThread
    fun getAllFilesAndFoldersUsingBruteForceRecursion():List<FileModel>{

        val startFile =
            Environment.getExternalStorageDirectory()//Uri.parse("file:///storage/emulated/0").toFile()
        val fileList = mutableListOf<File>()
        println("starting recursion")
        startFile.traverseRecursively(fileList)
        println("finished recursion. files = ${fileList.size} ")

        val fileModels = mutableListOf<FileModel>()
        println("starting parsing")

        fileList.forEach {
            val model = it.toUri().toFileModel()
            fileModels.add(model)
        }

        println("finished parsing. ")
        return fileModels

    }
}