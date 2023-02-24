package work.curioustools.demo_storageapis.ui

import work.curioustools.demo_storageapis.R

enum class TypeUI(val emoji:String,val icon:Int){
    FOLDER("\uD83D\uDCC1", R.drawable.ic_folder),
    ZIP(" \uD83D\uDCBE", R.drawable.ic_zip),
    IMAGE("\uD83C\uDF04", R.drawable.ic_image),
    VIDEO("\uD83C\uDFA5", R.drawable.ic_video),
    DOC("\uD83D\uDCD7", R.drawable.ic_book),
    AUDIO("\uD83C\uDFB5", R.drawable.ic_audio),

    UNKNOWN_FILE(" \uD83D\uDCCE", R.drawable.ic_file);

    companion object{
        private fun imageExtensions():List<String>{
            return listOf("png","jpg","jpeg","svg","webp","eps","heic","image")
        }
        private fun docExtensions():List<String>{
            return listOf("pdf","txt","md","doc","docs","docx","word","xlsx","cbr","text")
        }
        private fun videoExtensions():List<String>{
            return listOf("flv","3gpp","mp4","mkv","mov","video")
        }
        private fun audioExtensions():List<String>{
            return listOf("mp3","wav","amr","m4a","aac","mpeg","audio")
        }

        private fun compressedExtensions():List<String>{
            return listOf("zip","tar","7z","apk","application")
        }


        /* works with name.png , x/y/z/.png but not just the png */
        fun getTypeFromFileName(name:String,isMimeType:Boolean=false): TypeUI {
            val lastDelimiter =  if(isMimeType) "/" else "."
            val check = if(isMimeType)name.split(lastDelimiter).first().lowercase() else name.split(lastDelimiter).last().lowercase()
            return when(check){
                in imageExtensions() -> IMAGE
                in videoExtensions() -> VIDEO
                in audioExtensions() -> AUDIO
                in docExtensions() -> DOC
                in compressedExtensions() -> ZIP
                else -> UNKNOWN_FILE
            }
        }


        fun getEmojiPath(parts:List<String>,lastIcon:String?=null):String{
            val finalLastIcon = lastIcon?: getTypeFromFileName(parts.last())

            var pathStringWithEmojis = ""
            parts.forEachIndexed { index, s ->
                pathStringWithEmojis =
                    (if (index == parts.lastIndex) pathStringWithEmojis + finalLastIcon + s
                    else pathStringWithEmojis + FOLDER.emoji + s + " > ")
            }
            return pathStringWithEmojis
        }
    }
}