Demo Storage Apis

A demo showing how various storage apis would work in android, what we permissions would be needed and what would be the returned data.

Following are the approaches

## BRUTE_FORCE
read Environment.getExternalStorageDirectory() and recursively check all files

```json
{
  "BRUTE_FORCE": {
    "about": "read Environment.getExternalStorageDirectory() and recursively check all files",
    "results": [
      {
        "version": "android lollipop/5.1/22",
        "device": "emulator pixel 1 no google services",
        "result": ["SUCCESS"],
        "permissionsRequired": null
      },
      {
        "version": "android m/6.0/23",
        "device": "galaxy nexus no google services",
        "result": ["SUCCESS"],
        "permissionsRequired": ["storage(read/write)"]
      },
      {
        "version": "android p/9.0/28",
        "device": "pixel4",
        "result": ["SUCCESS"],
        "permissionsRequired": ["storage(read/write)"]
      },
      {
        "version": "android q/10.0/29",
        "device": "pixel4",
        "result": ["FAIL", "PARTIAL"],
        "permissionsRequired": ["storage(read/write)"],
        "misc": "<b>result changes to success if we apply android:requestLegacyExternalStorage='true'</b>"
      },
      {
        "version": "android s/12.0/31",
        "device": "pixel4",
        "result": ["SUCCESS", "PARTIAL"],
        "permissionsRequired": ["special_manage_storage"],
        "misc": "simply request special manage storage for getting access to all files otherwise super weird behavior. (1) the usual permission reads as :'Files and Media' instead of storage. 2: let m,r,w = manage,read and write permission respectively. if r,w permission is mentioned in the manifest, the usual permission screen will have the radio buttons saying 'deny' and 'allow access to media'. if user grants the permission, then  bruteforce and mf_all approach will still not provide access to non-media files. However if app mentions m+r+w permissions, then the usual permission page will show 3 radio buttons : 'deny', 'allow media files' , 'allow all files' . giving all files permission is same as accepting m from special permission request page. bruteforce and mf_all starts to work after this"
      },
      {
        "version": "android t+/13.0/33",
        "device": "pixel4",
        "result": ["SUCCESS", "PARTIAL"],
        "permissionsRequired": ["special_manage_storage"],
        "misc": "simply request special manage storage for getting access to all files otherwise it will return files based on 'usual' permissions we have: if we have audio+music, it will return folders and audio. if we have photos+videos, it will return folders,images,and audios"
      }
    ]
  }
}
```

## MEDIA_URI_FILE_ALL
read MediaStore.Files.getContentUri('external') using cursor without any mimetype filters and try to access all files and folders

```json
{
  "MEDIA_URI_FILE_ALL": {
    "about": "read MediaStore.Files.getContentUri('external') using cursor without any mimetype filters and try to access all files and folders",
    "results": [
      {
        "version": "android lollipop/5.1/22",
        "device": "emulator pixel 1 no google services",
        "result": ["UNSURE", "files and folders does show from external storage, but not the ones created by device manager"],
        "permissionsRequired": null
      },
      {
        "version": "android m/6.0/23",
        "device": "galaxy nexus no google services",
        "result": ["SUCCESS"],
        "permissionsRequired": ["storage(read/write)"],
        "misc": "it would take a mobile restart to show up all files"
      },
      {
        "version": "android p/9.0/28",
        "device": "pixel4",
        "result": ["SUCCESS"],
        "permissionsRequired": ["storage(read/write)"],
        "misc": "it would take a mobile restart to show up all files"
      },
      {
        "version": "android q/10.0/29",
        "device": "pixel4",
        "result": ["SUCCESS", "PARTIAL"],
        "permissionsRequired": ["storage(read/write)"],
        "misc": "<b>result changes to full success if we apply android:requestLegacyExternalStorage='true'</b> | only shows audio/video/image files | it would take a mobile restart to show up all files"
      },
      {
        "version": "android s/12.0/31",
        "device": "pixel4",
        "result": ["FAIL", "PARTIAL"],
        "permissionsRequired": ["special_manage_storage"],
        "misc": "simply request special manage storage for getting access to all files otherwise super weird behavior. (1) the usual permission reads as :'Files and Media' instead of storage. 2: let m,r,w = manage,read and write permission respectively. if r,w permission is mentioned in the manifest, the usual permission screen will have the radio buttons saying 'deny' and 'allow access to media'. if user grants the permission, then  bruteforce and mf_all approach will still not provide access to non-media files. However if app mentions m+r+w permissions, then the usual permission page will show 3 radio buttons : 'deny', 'allow media files' , 'allow all files' . giving all files permission is same as accepting m from special permission request page. bruteforce and mf_all starts to work after this"
      },
      {
        "version": "android t+/13.0/33",
        "device": "pixel4",
        "result": ["FAIL", "PARTIAL"],
        "permissionsRequired": ["special_manage_storage"],
        "misc": "simply request special manage storage for getting access to all files otherwise it will return files based on 'usual' permissions we have: if we have audio+music, it will return folders and audio. if we have photos+videos, it will return folders,images,and audios"
      }
    ]
  }
  
}
```


## MEDIA_URI_FILE_FILTER
read MediaStore.Files.getContentUri('external') using cursor with mimetype filters and try to access all associated files

```json
{
  "MEDIA_URI_FILE_FILTER": {
    "about": "read MediaStore.Files.getContentUri('external') using cursor with mimetype filters and try to access all associated files",
    "results": [
      {
        "version": "android lollipop/5.1/22",
        "device": "emulator pixel 1 no google services",
        "result": ["FAIL"],
        "permissionsRequired": null
      },
      {
        "version": "android m/6.0/23",
        "device": "galaxy nexus no google services",
        "result": ["SUCCESS"],
        "permissionsRequired": ["storage(read/write)"],
        "misc": "it would take a mobile restart to show up all files"
      },
      {
        "version": "android p/9.0/28",
        "device": "pixel4",
        "result": ["SUCCESS"],
        "permissionsRequired": ["storage(read/write)"],
        "misc": "it would take a mobile restart to show up all files"
      },
      {
        "version": "android q/10.0/29",
        "device": "pixel4",
        "result": ["SUCCESS"],
        "permissionsRequired": ["storage(read/write)"]
      },
      {
        "version": "android s/12.0/31",
        "device": "pixel4",
        "result": ["SUCCESS"],
        "permissionsRequired": ["storage(read/write)"]
      },
      {
        "version": "android t+/13.0/33",
        "device": "pixel4",
        "result": ["SUCCESS"],
        "permissionsRequired": ["image+videos", "audio+music"]
      }
    ]
  }
  }
```

## MEDIA_URI_SPECIFIC
read specific mediastore queries like MediaStore.Images.Media.EXTERNAL_CONTENT_URI using cursor with mimetype filters and try to access associated files

```json
{
  "MEDIA_URI_SPECIFIC": {
    "about": "read specific mediastore queries like MediaStore.Images.Media.EXTERNAL_CONTENT_URI using cursor with mimetype filters and try to access associated files",
    "results": [
      {
        "version": "android lollipop/5.1/22",
        "device": "emulator pixel 1 no google services",
        "result": ["UNSURE", "files and folders does show from external storage, but not the ones created by device manager"],
        "permissionsRequired": null
      },
      {
        "version": "android m/6.0/23",
        "device": "galaxy nexus no google services",
        "result": ["SUCCESS"],
        "permissionsRequired": ["storage(read/write)"],
        "misc": "it would take a mobile restart to show up all files"
      },
      {
        "version": "android p/9.0/28",
        "device": "pixel4",
        "result": ["SUCCESS"],
        "permissionsRequired": ["storage(read/write)"],
        "misc": "it would take a mobile restart to show up all files"
      },
      {
        "version": "android q/10.0/29",
        "device": "pixel4",
        "result": ["SUCCESS"],
        "permissionsRequired": ["storage(read/write)"],
        "misc": "it would take a mobile restart to show up all files"
      },
      {
        "version": "android s/12.0/31",
        "device": "pixel4",
        "result": ["SUCCESS"],
        "permissionsRequired": ["storage(read/write)"]
      },
      {
        "version": "android t+/13.0/33",
        "device": "pixel4",
        "result": ["SUCCESS"],
        "permissionsRequired": ["image+videos", "audio+music"]
      }
    ]
  }
}
```



## SAF_FOLDER_PICKER
use SAF apis to ask user to choose a folder. once user selects it, we can iterate through all the files and folders in that folder

```json
{
  "SAF_FOLDER_PICKER": {
    "about": "use saf apis to ask user to choose a folder. once user selects it, we can iterate through all the files and folders in that folder",
    "results": [
      {
        "version": "android l/5.1/22",
        "device": "galaxy nexus no google services",
        "result": ["SUCCESS"],
        "permissionsRequired": null,
        "misc": "terrible ui for SAF"
      },
      {
        "version": "android m/6.0/23",
        "device": "galaxy nexus no google services",
        "result": ["SUCCESS"],
        "permissionsRequired": null,
        "misc": "terrible ui for SAF"
      },
      {
        "version": "android p/9.0/28",
        "device": "pixel4",
        "result": ["SUCCESS"],
        "permissionsRequired": null
      },
      {
        "version": "android q/10.0/29",
        "device": "pixel4",
        "result": ["SUCCESS"],
        "permissionsRequired": null
      },
      {
        "version": "android s/12.0/31",
        "device": "pixel4",
        "result": ["SUCCESS", "PARTIAL"],
        "permissionsRequired": null,
        "misc": "in android 11/+ it will not allow access to downloads,root and few other directories"
      },
      {
        "version": "android t+/13.0/33",
        "device": "pixel4",
        "result": ["SUCCESS", "PARTIAL"],
        "permissionsRequired": null,
        "misc": "in android 11/+ it will not allow access to downloads,root and few other directories"
      }
    ]
  }
}
```