package work.curioustools.demo_storageapis

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.net.toUri
import work.curioustools.demo_storageapis.ui.FileModel


class FileModelFromMediaUris(private val contentResolver: ContentResolver){


    // note : this is NOT generic. it only requests for string value from the cursor, whereas the value can be integer or other too, and that will result in an error
    private fun getPathsFromCursor(selectFromLocation: Uri, selectionColumns: Array<String>, whereConditionLHS:String? = null, whereConditionRHS:Array<String>? = null, sortBy: String?):List<List<String>>{
       println( "getFilePathList() called with: selectFromLocation = $selectFromLocation, selectionColumns = ${selectionColumns.toList()}, whereConditionLHS = ${whereConditionLHS}, whereConditionRHS = ${whereConditionRHS?.toList()}, sortBy = $sortBy")

        val result = mutableListOf<List<String>>()
        val cursor: Cursor? = contentResolver.query(selectFromLocation, selectionColumns, whereConditionLHS, whereConditionRHS, sortBy)

        println( "getFilePathList:  cursor recieved= $cursor | has files = ${cursor?.count}" )


        while (cursor?.moveToNext()==true) {
            val singleEntry = mutableListOf<String>()
            selectionColumns.forEach {
                val i = cursor.indexOfOrDefault(it)
                val data: String? = cursor.getString(i)
                singleEntry.add(data?:"")
            }

            result.add(singleEntry)
        }
        cursor?.close()
        return result
    }

    fun getAllMediaFiles():List<FileModel>{
        // this is supposed to return all kinds of files and folders, but
        // only returns media files (audio/video/image)on android 11+,12,13"
        // bruteforce recursion is the only function that returns non media files,
        // and that too on android 12 and below, and that too using android manifest flag


        return getPathsFromCursor(
            selectFromLocation = MediaStore.Files.getContentUri("external"),
            selectionColumns = arrayOf(MediaStore.Files.FileColumns.DATA,MediaStore.Files.FileColumns.MIME_TYPE),
            whereConditionLHS = null,
            whereConditionRHS = null,
            sortBy = "${MediaStore.Files.FileColumns.MIME_TYPE} DESC",
        ).map {
            val data = it[0]
            val mimeType = it.getOrNull(1)
            "file://$data".toUri().toFileModel(mimeType)
        }
    }
    fun getMultipleMedia(mimeTypes :Array<String> =  arrayOf("image/%","audio/%","video/%")):List<FileModel>{
        //can be used to query for specific type of media file too, like  image/jpeg
        val questionMarks = mimeTypes.map { MediaStore.Files.FileColumns.MIME_TYPE+" LIKE ?" }.joinToString (" or ")
        return getPathsFromCursor(
            selectFromLocation = MediaStore.Files.getContentUri("external"),
            selectionColumns = arrayOf(MediaStore.Files.FileColumns.DATA,MediaStore.Files.FileColumns.MIME_TYPE),
            whereConditionLHS = questionMarks,
            whereConditionRHS =mimeTypes,
            sortBy = "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC",
        ).map {
            val data = it[0]
            val mimeType = it.getOrNull(1)
            "file://$data".toUri().toFileModel(mimeType)
        }
    }
    fun getSingleMedia():List<FileModel>{
        //notice , has different mediastore classes
        val mimeTypes = arrayOf("image/%") // or "video/%  or "audio/%". remember to use appropriate image queries accordingly
        val questionMarks = mimeTypes.map { "?" }.joinToString ()
        return getPathsFromCursor(
            selectFromLocation = MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            selectionColumns = arrayOf(MediaStore.Images.Media.DATA,MediaStore.Images.Media.MIME_TYPE),
            whereConditionLHS = MediaStore.Images.Media.MIME_TYPE + " LIKE ($questionMarks)",
            whereConditionRHS =mimeTypes,
            sortBy = "${MediaStore.Images.Media.DATE_MODIFIED} DESC",
        ).map {
            val data = it[0]
            val mimeType = it.getOrNull(1)
            "file://$data".toUri().toFileModel(mimeType)
        }
    }


    @Deprecated("does not work")
    fun getAllFolders():List<FileModel>{
        //does not work

        val mimeTypes = arrayOf(DocumentsContract.Document.MIME_TYPE_DIR)
        val questionMarks: String = mimeTypes.map { "?" }.joinToString ()
        return getPathsFromCursor(
            selectFromLocation = MediaStore.Files.getContentUri("external"),
            selectionColumns = arrayOf(DocumentsContract.Document.COLUMN_DISPLAY_NAME),
            whereConditionLHS = "${DocumentsContract.Document.COLUMN_MIME_TYPE} in ($questionMarks)",
            whereConditionRHS = mimeTypes,
            sortBy = null,
        ).map { it[0].toUri().toFileModel() }
    }

    @Deprecated("does not work")
    fun getAllDocuments():List<FileModel>{
        //does not work on android 12 //todo

        //val mimeTypes =  arrayOf(
        //    "text/plain",/* (txt, log, ini)*/"text/html", /*(html, htm)*/"application/pdf", /*(pdf)*/"application/msword", /*(doc)*/
        //    "application/vnd.openxmlformats-officedocument.wordprocessingml.document", /*(docx)*/"application/vnd.ms-excel", /*(xls)*/
        //    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", /*(xlsx)*/"application/vnd.ms-powerpoint", /*(ppt)*/
        //    "application/vnd.openxmlformats-officedocument.presentationml.presentation", /*(pptx)*/"application/rtf", /*(rtf)*/"application/json", /*(json)*/
        //    "application/xml", /*(xml)*/"application/zip", /*(zip)*/"application/gzip", /*(gz)*/"application/x-rar-compressed", /*(rar)*/
        //)

        val mimeTypes = arrayOf("application/*","text/*")
        val questionMarks = mimeTypes.map { "?" }.joinToString()
        return getPathsFromCursor(
            selectFromLocation = MediaStore.Files.getContentUri("external"),
            selectionColumns = arrayOf(MediaStore.Files.FileColumns.DATA),
            whereConditionLHS = MediaStore.Files.FileColumns.MIME_TYPE + " IN ($questionMarks) ",
            whereConditionRHS =mimeTypes,
            sortBy = "${MediaStore.Files.FileColumns.DISPLAY_NAME} ASC",
        ).map { it[0].toUri().toFileModel() }
    }

    fun Cursor.indexOfOrDefault(name:String):Int{
        val index = getColumnIndex(name)
        return if(index<0) 0 else index
    }
}




