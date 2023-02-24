package work.curioustools.demo_storageapis

import org.json.JSONObject
import java.io.File


fun File.traverseRecursively(list: MutableList<File>) {
    // Get the list of files in the directory
    val files = this.listFiles()?: emptyArray()

    // Add the files to the list
    for (file in files) {
        list.add(file)

        // Recursively traverse subdirectories
        if (file.isDirectory) {
            file.traverseRecursively(list)
        }
    }

}
fun File.toJson(): JSONObject {
    val res = JSONObject()
    runLogging { res.put("absoluteFile", this.absoluteFile) }
    runLogging { res.put("absolutePath", this.absolutePath) }
    runLogging { res.put("canonicalFile", this.canonicalFile) }
    runLogging { res.put("canonicalPath", this.canonicalPath) }
    runLogging { res.put("canExecute", this.canExecute()) }
    runLogging { res.put("canRead", this.canRead()) }
    runLogging { res.put("canWrite", this.canWrite()) }
    runLogging { res.put("exists", this.exists()) }
    runLogging { res.put("extension", this.extension) }
    runLogging { res.put("freeSpace", this.freeSpace.toMemoryString()) }
    runLogging { res.put("isAbsolute", this.isAbsolute) }
    runLogging { res.put("isDirectory", this.isDirectory) }
    runLogging { res.put("isFile", this.isFile) }
    runLogging { res.put("isHidden", this.isHidden) }
    runLogging { res.put("isRooted", this.isRooted) }
    runLogging { res.put("invariantSeparatorsPath", this.invariantSeparatorsPath) }
    runLogging { res.put("lastModified", this.lastModified()) }
    runLogging { res.put("length", this.length().toMemoryString()) }
    runLogging { res.put("list", this.list()?.toList()) }
    runLogging { res.put("listFiles", this.listFiles()?.toList()) }
    runLogging { res.put("name", this.name) }
    runLogging { res.put("nameWithoutExtension", this.nameWithoutExtension) }
    runLogging { res.put("path", this.path) }
    runLogging { res.put("parentFile", this.parentFile?.toString()) }


    runLogging { res.put("parent", this.parent) }
    runLogging { res.put("totalSpace", this.totalSpace.toMemoryString()) }
    runLogging { res.put("usableSpace", this.usableSpace.toMemoryString()) }
    //todo checkout StorageManager()



    return res
}
