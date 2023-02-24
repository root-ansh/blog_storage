# Files and Uri


## What does this demo have?
This demo shows a list of common `file://` based uris and there associated details.

**Permission Details** : This app does not require any Permissions in manifest as well as any additional storage flags

**API Behavior**
This app supports Android version 19-33.

| SDK VERSION                   | DEVICE | BEHAVIOR |
|-------------------------------|--------|----------|
| 19    (Kitkat/4.4 - 4.4.4)    | x      |          |
| 20    (Kitkat/4.4 - 4.4.4)    | x      |          |
| 21,22 (Lollipop/5.0 - 5.1.1)  | x      |          |
| 23    (Marshmallow/6.0 - 6.1) | x      |          |
| 24,25 (Nougat/7.0-7.1)        | x      |          |
| 26    (Oreo)                  | x      |          |
| 27    (Oreo)                  | x      |          |
| 28    (Pie)                   | x      |          |
| 29    (Q/Android 10/10.0 )    | x      |          |
| 30    (R/Android 11/11.0)     | x      |          |
| 31    (S)                     | x      |          |
| 32    (S)                     | x      |          |
| 33    (T)                     | x      |          |


| SDK Version | device    | Behavior   |                                                                                                                                     | Sample Methods and Descriptions                                                                                                                                                                                                                                                                                                                         |
|-----------------|---------------|
| Helper Class    | Description   |                                                                                                                                     | Sample Methods and Descriptions                                                                                                                                                                                                                                                                                                                         |




## About

- URI (Uniform Resource Identifier) is a unique sequence of characters that represents a logical or physical resource somewhere.
- It is common in web technologies as URL(Uniform Resource Locator) and URN(Uniform Resource Names). URI is basically a Superset of URL and URN.

```
// A typical uri

          subdomai  host      port
          ┌┴┐  ┌─────┴──────┐ ┌┴┐
  https://api.www.example.com:8888/forum/questions/?tag=networking&order=newest#top
  └─┬─┘   └───────────┬──────────┘    └───────┬───────┘ └────────────┬────────────┘ └┬┘
  scheme          authority                  path                  query           fragment
```

```
a typical urn

  urn:oasis:names:specification:docbook:dtd:xml:4.1.2
  └┬┘ └──────────────────────┬──────────────────────┘
  scheme                    path
```

- In Android, a URI string can be somewhat used to represent the location of a folder or file in filesystem. We have a system class `Uri` that provides functions to not only get access to a string that represents a location, but also additional functions that can be used to get info about other properties of the location: like whether the location represents a file resource or a folder resource, its size, its properties etc. for eg, following is the json representation of  `Environment.getExternalStorageDirectory()` uri

```json
{"authority":"","host":"","lastPathSegment":"0","path":"/storage/emulated/0","pathSegments":"[storage, emulated, 0]","queryParameterNames":"[]","scheme":"file","encoding":{"encodedAuthority":"","encodedPath":"/storage/emulated/0","encodedSchemeSpecificPart":"///storage/emulated/0"},"checks":{"isAbsolute":true,"isHierarchical":true,"isOpaque":false,"isRelative":false},"extension":""}
```

- Note that Android uses the `android.net.Uri` class for local storage and not the ~`java.net.URI`~ class. there are a few more confusing classes mentioned [here](https://stackoverflow.com/a/40725390) basically we have to always work with `java.io.File` and `android.net.Uri` class, the former of which is a better wrapper over uri with even more info. for eg, when we convert `Environment.getExternalStorageDirectory()`s uri to file, we get following more info:

```json
{"authority":"","host":"","lastPathSegment":"0","path":"/storage/emulated/0","pathSegments":"[storage, emulated, 0]","queryParameterNames":"[]","scheme":"file","encoding":{"encodedAuthority":"","encodedPath":"/storage/emulated/0","encodedSchemeSpecificPart":"///storage/emulated/0"},"checks":{"isAbsolute":true,"isHierarchical":true,"isOpaque":false,"isRelative":false},"toFile":{"absoluteFile":"/storage/emulated/0","absolutePath":"/storage/emulated/0","canonicalFile":"/storage/emulated/0","canonicalPath":"/storage/emulated/0","canExecute":true,"canRead":true,"canWrite":true,"exists":true,"extension":"","freeSpace":"5.1 GiB","isAbsolute":true,"isDirectory":true,"isFile":false,"isHidden":false,"isRooted":true,"invariantSeparatorsPath":"/storage/emulated/0","lastModified":1676797789000,"length":"4.0 KiB","list":"[Android, Music, Podcasts, Ringtones, Alarms, Notifications, Pictures, Movies, Download, DCIM]","listFiles":"[/storage/emulated/0/Android, /storage/emulated/0/Music, /storage/emulated/0/Podcasts, /storage/emulated/0/Ringtones, /storage/emulated/0/Alarms, /storage/emulated/0/Notifications, /storage/emulated/0/Pictures, /storage/emulated/0/Movies, /storage/emulated/0/Download, /storage/emulated/0/DCIM]","name":"0","nameWithoutExtension":"0","path":"/storage/emulated/0","parentFile":"/storage/emulated","parent":"/storage/emulated","totalSpace":"5.8 GiB","usableSpace":"4.9 GiB"},"sdk26Attrs":{"fileKey":"(dev=16,ino=106499)","isDirectory":true,"isOther":false,"isRegularFile":false,"isSymbolicLink":false,"creationTime":"2023-02-19T09:09:49Z","lastAccessTime":"2023-02-19T09:09:47Z","lastModifiedTime":"2023-02-19T09:09:49Z","size":4096},"extension":""}
```

## Common  Helper classes for files

| Helper Class    | Description                                                                                                                                        | Sample Methods and Descriptions                                                                                                                                                                                                                                                                                                                         |
|-----------------|----------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Files           | Provides utility methods for working with files, including methods for checking file existence, getting file attributes, and copying/moving files. | `exists(Path path)`: Returns `true` if the specified file exists. <br> `isDirectory(Path path)`: Returns `true` if the specified file is a directory. <br> `move(Path source, Path target, CopyOption... options)`: Moves the file at `source` to `target`.                                                                                             |
| Paths           | Provides utility methods for working with file paths.                                                                                              | `get(String first, String... more)`: Creates a new `Path` object by joining together the specified path elements. <br> `getFileSystem()`: Returns the `FileSystem` that created this `Path`. <br> `isAbsolute()`: Returns `true` if this `Path` is absolute.                                                                                            |
| FileUtils       | Provides utility methods for working with files, including methods for reading and writing files, creating temporary files, and deleting files.    | `copyFile(File srcFile, File destFile)`: Copies the contents of the `srcFile` to the `destFile`. <br> `writeStringToFile(File file, String data, Charset encoding)`: Writes the specified string to the `file` using the specified character encoding. <br> `deleteQuietly(File file)`: Deletes the specified file, ignoring any errors that may occur. |
| URLConnection   | Represents a connection to a resource on the internet, such as a file or web page.                                                                 | `getContentLength()`: Returns the length of the content associated with this connection. <br> `getContentType()`: Returns the content type of the resource associated with this connection. <br> `getLastModified()`: Returns the date and time that the resource associated with this connection was last modified.                                    |
| FileInputStream | Represents a stream of bytes read from a file.                                                                                                     | `read(byte[] b)`: Reads up to `b.length` bytes of data from this input stream into an array of bytes. <br> `available()`: Returns the number of bytes that can be read from this input stream without blocking. <br> `close()`: Closes this input stream and releases any system resources associated with it.                                          |
| MimeTypeMap     | Provides a map of file extensions to MIME types, which can be used to determine the type of a file based on its extension.                         | `getMimeTypeFromExtension(String extension)`: Returns the MIME type for the specified file extension. <br> `getExtensionFromMimeType(String mimeType)`: Returns the file extension for the specified MIME type.                                                                                                                                         |
| PathMatcher     | Provides a way to match file paths against a specified pattern.                                                                                    | `matches(Path path)`: Returns `true` if the specified path matches the pattern. <br> `toString()`: Returns a string representation of the pattern.                                                                                                                                                                                                      |
| FileVisitor     | Provides a way to traverse a file tree and perform an action on each file or directory.                                                            | `preVisitDirectory(Path dir, BasicFileAttributes attrs)`: Called before visiting a directory. <br> `visitFile(Path file, BasicFileAttributes attrs)`: Called when visiting a file. <br> `postVisitDirectory(Path dir, IOException exc)`: Called after visiting a directory.                                                                             |
| Charset         | Represents a character set, which can be used to encode and decode text.                                                                           | `forName(String charsetName)`: Returns a `Charset` object for the specified charset name. <br> `encode(CharBuffer cb)`: Encodes the specified character buffer into a byte buffer using this charset's default encoder. <br> `decode(ByteBuffer bb)`: Decodes the specified byte buffer into a character buffer using this charset's default decoder.   |
| ZipFile         | Provides a way to read the contents of a ZIP file.                                                                                                 | `entries()`: Returns an enumeration of the ZIP file entries. <br> `getInputStream(ZipEntry entry)`: Returns an input stream for reading the contents of the specified ZIP file entry. <br> `close()`: Closes this ZIP file and releases any system resources associated with it.                                                                        |
| FileSystem      | Provides a way to access the file system, including methods for creating and deleting files and directories, and getting file attributes.          | `getPath(String first, String... more)`: Returns a `Path` object representing the specified path. <br> `getFileStore(Path path)`: Returns the `FileStore` object for the file system containing the specified path. <br> `getUserPrincipalLookupService()`: Returns the `UserPrincipalLookupService` for the file system.                               |
| StorageManager  | -                                                                                                                                                  | -                                                                                                                                                                                                                                                                                                                                                       |
