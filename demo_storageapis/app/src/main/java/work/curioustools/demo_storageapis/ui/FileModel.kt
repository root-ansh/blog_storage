package work.curioustools.demo_storageapis.ui


import org.json.JSONObject

data class FileModel(
    val title: String,
    val path: String,
    val fileSize: String,
    val internalItemsSize: String,
    val res: Int,
    val mimeType:String,
){
    fun toJson():JSONObject{
        return JSONObject().also {
            it.put("title",title)
            it.put("path",path)
            it.put("fileSize",fileSize)
            it.put("internalItemsSize",internalItemsSize)
            it.put("res",res)
            it.put("mimeType",mimeType)
        }
    }
}

