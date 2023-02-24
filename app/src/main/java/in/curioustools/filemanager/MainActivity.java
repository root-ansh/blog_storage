package in.curioustools.filemanager;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

/*
         Their are many ways to access the so called "user files" . basically, android implements
         a YAFFS File system in whiich every file is secured via directory structure : files present
         in one directory will not be visible to other. So, let's discuss the general hiarchy in
         this system.


         1. '/' or the root is , well, the root of the directory. All files/folders are present in
            this directory. Some of the folders present here are : /boot /ext /mnt /data /cache
            /system ,/sdcard,/storage...etc .Basically they all have  a special purpose and if you are
            an app, then you don't have access to almost all of them but you can access
            /storage/emulated/0 and /storage/emuated/1 whichyou can access,if you have read/write
            permissions. these are the 2 directories, where  user has every of its file, and where
            the different apps store their publically accessable data.
         2. to  access user iles, their can be many ways:
           2.1 : Filing

                (a) basics of filing and manual filing

                 On keeping itself true to its name, Android has its own beautiful way to allow acces to files and folders
                 that can often confuse anyone.(But its beautiful.)
                 It provides access to contents of a folder or a simple file as a File Object, which is a data structure
                 that does not actually hold those files, but kind of holds the "pointers" and "meta data" of those files.
                 A file object can be simply created using a filepath. for eg File f = new Fiele("/storage/emulated/0/downloads")
                 and it can be used to accesss the whole contents of folder 'download'. furthermore every file
                 object has several functions which help in getting more info about that File itself.
                 Its funnny how Android treat folders and traditional "files" as same
                 Some functions of File are :
                 - fileObj.list() :
                   Returns a String[] havimg names of all the files/folders in the current folder.
                   you can simply use current folder's path+ filename to get a URI for  any
                   file/folder.
                   This array's length will indicate the total no. of contents in the current
                   folder( i.e if the FileObj is made for the folder)
                   This function can also take an optional parameter of FileNameFilter interface
                   which is useful if you want to get a curated list of specific filetypes.
                   So , if you want to display only the mp3 file in the current directory, you can
                   get a list of them by placing a filter to check the ending for 'mp3' or 'MP3'

                 - fileObj.isFile()/fileObj.isFolder() : as the name suggest, returns true/false

                 - fileObj.isreadable()/fileObj.iswriteable()/fileObj.isexecutable():as the name
                   suggest, returns true/false
                 - fileObj.toString()/fileObj.toPath()/fileObj.toURI()
                 - fileObj.delete() : to delete current file/folder
                 - fileObj.mkdir():
                 - ... and many more

                 By manual filing, i simply mean the way by which we create a file object. Everytime our app opens
                 , it creates a new FileObject using the hardcoded path. this could often lead to provlems since
                 the storage directories are often manufacturer dependent. this means that if samsung mobiles has
                 storage memory mounted at "/storage/emulated/0 , there are fare chances that some other
                 manufacturer has memory mounted at "/storage/emulated/sdcard0. this would cause your app to not show
                 any data if those URI's are different.
                 So Android has come up with a solution which i term as recommended filing.

                 (b) Recommended filing:
                 To prevent the problem given above, android has a specific classes and objects which gives
                 you FileObject mapped to the correct paths of different directories.
                 I know 2 such access points . First is our very own super Activity Context Object.
                 you can use the following functions to get File object of different directories
                  context.getCacheDir();
                  context.getCodeCacheDir();
                  context.getDatabasePath(String path);
                   context.getDataDir();//require min API24
                   context.getDir(String Path , int mode);
                   context.getExternalCacheDirs();
                   context.getExternalCacheDir();
                   context.getExternalFilesDir(String path);
                   context.getExternalFilesDirs(String path);
                   context.getExternalMediaDirs();
                   context.getFilesDir();
                   context.getFileStreamPath(String path);
                   context.getObbDir();
                   context.getObbDirs();
                   context.getNoBackupFilesDir();

                  Second is the Environment class. it holds several static functions, which can be
                  used to interact with different directories without the need of context or even a
                  running foreground app( although i wonder what would be the need of directory tree
                  in background)
                  you can use the following functions to get File object of different directories:
                      Environment.getDataDirectory();
                      Environment.getDownloadCacheDirectory();
                      Environment.getExternalStoragePublicDirectory(String publicDirNmae);
                      Environment.getExternalStorageState();
                      Environment.getExternalStorageState(File path);

                      Environment.isExternalStorageEmulated();
                      Environment.isExternalStorageEmulated(File Path);
                      Environment.isExternalStorageRemovable();
                      Environment.isExternalStorageRemovable(File Path);


           2.2 : MediaStore
*/
public class MainActivity extends AppCompatActivity {
    TextView tvCurrentPath;
    RecyclerView rv;
    RvAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        
    }



//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
//
//        initUI();
//        File initialDirFile = Environment.getExternalStorageDirectory();
//
//        updateUIData(initialDirFile);
//    }
//
    //TODO: 08-03-2019  if recyclerview length = 0 then show nothing here screen
    //todo : sorting
    // TODO: 08-03-2019 add options to delete
    // TODO: throw BroadCasts to make Activiy aware of UserClicks
    //todo : replace current path textview with tabs
    //todo : copy/paste file( although that takes a lot of time and therefore needs services
    //todo ftp share
    //todo : gridview/multi select
    // TODO: 09-03-2019 permissions
    //todo : throws error when trying to load a big directory with large number of big files, like DCIM: need to learn to handle cache
    //todo : show sd card location too

    //https://stackoverflow.com/questions/2975197/convert-file-uri-to-file-in-android funny question,
    // every answer works for everyone

    private void initUI() {
        tvCurrentPath = findViewById(R.id.text_currentpath);
        rv = findViewById(R.id.rv_filemanager);
        rv.setLayoutManager(new LinearLayoutManager(this));

        File f = Environment.getExternalStorageDirectory();
        Utilities.logFileDetails(f);
        adp = new RvAdapter(f);

        rv.setAdapter(adp);
    }

    private void updateUIData(File currentDirFile) {
        tvCurrentPath.setText(currentDirFile.toString());
        adp.updateCurrentDirAndChildren(currentDirFile,currentDirFile.list());

    }

    @Override
    public void onBackPressed() {
       try {
           File currentDir = adp.getCurrentDir();
           if(currentDir.equals(Environment.getExternalStorageDirectory())){
               super.onBackPressed();
           }
           else {
               File previousDir = currentDir.getParentFile();
               updateUIData(previousDir);
           }
       }
       catch (Throwable t){
           t.printStackTrace();
           super.onBackPressed();
       }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(menu==null)return false;
        menu.add(Menu.NONE,0,Menu.NONE,"Request Permission for newer devices").setOnMenuItemClickListener(
                item -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        Intent intent =new  Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this,("permission not available. your device has os that is less than android R(${Build.VERSION_CODES.R})"),Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
        );
        return true;
    }
}

