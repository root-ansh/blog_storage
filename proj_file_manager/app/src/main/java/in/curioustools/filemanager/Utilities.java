package in.curioustools.filemanager;


import android.content.Context;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

public class Utilities {

    //    String someFilepath = "image.fromyesterday.test.jpg";
//    String extension = someFilepath.substring(someFilepath.lastIndexOf("."));
//    System.out.println(extension);// .jpg
    public static int getIconRes(String ending) {
        switch (ending) {

            case ".mp3":
                return R.drawable.ic_audio;
            case ".ogg":
                return R.drawable.ic_audio;
            case ".aac":
                return R.drawable.ic_audio;


            case ".mp4":
                return R.drawable.ic_video;
            case ".mkv":
                return R.drawable.ic_video;


            case ".jpeg":
                return R.drawable.ic_image;
            case ".jpg":
                return R.drawable.ic_image;
            case ".png":
                return R.drawable.ic_image;
            case ".gif":
                return R.drawable.ic_image;

            case ".pdf":
                return R.drawable.ic_pdf;
            case ".txt":
                return R.drawable.ic_file;
            case ".docx":
                return R.drawable.ic_docx;
            case ".doc":
                return R.drawable.ic_docx;

            case ".zip":
                return R.drawable.ic_zip;
            case ".rar":
                return R.drawable.ic_zip;


            case "folder":
                return R.drawable.ic_folder;
            default:
                return R.drawable.ic_file;
        }
    }

    // TODO: 08-03-2019
    public static String getTimeString(long timeInMillis) {
        return DateFormat.format("MMM dd ,yyyy", new Date(timeInMillis)).toString();
    }

    public static String getMemoryString(long size) {

        if ((double) size < 1000) {
            return String.format("%3.0f bytes", (double) size);
        } else if ((double) size / 1000 < 1000) {
            return String.format("%.2f KB", ((double) size / 1000));
        } else if ((double) size / (1000000) < 1000) {
            return String.format("%.2f MB", ((double) size / 1000000));
        } else {
            return String.format("%.2f GB", ((double) size / 1000000000));
        }
    }


    public static void logFileDetails(File dirFile) {
        String TAG = "FILE_DETAILS";

        String storage_state = Environment.getExternalStorageState();
        Log.e(TAG, ": state of external storage:" + storage_state);
        // gives external storage  state of god knows which file!

        //dirFile = Environment.getExternalStorageDirectory();// path :  /storage/emulated/0 //gives error if permissions not handled correctly
        //dirFile = Environment.getRootDirectory();  // path :  /system
        //dirFile = Environment.getDataDirectory();  //path: /data : not accessible until the phn is rooted: will directly close app w/o warning
        //dirFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM); // /storage/emulated/0/DCIM
        //dirFile = new File("/storage/emulated/0/Download/tmp 2/");

        Log.e(TAG, ": dirfile    ::    " + dirFile);

        //c
        Log.e(TAG, ": dirFile.canExecute()    ::    " + dirFile.canExecute());
        Log.e(TAG, ": dirFile.canRead()    ::    " + dirFile.canRead());
        Log.e(TAG, ": dirfile.canWrite()    ::    " + dirFile.canWrite());
        //Log.e(TAG, ": dirfile.compareTo(filepath)    ::    "  +   dirFile.compareTo(filepath));// filepath req
        //Log.e(TAG, ": dirfile.createNewFile()    ::    "  +   dirFile.createNewFile());//Throws I/0. warns for T/C

        //d
        //Log.e(TAG, ": dirfiledelete()    ::    "  +   dirFile.delete()); Scary
        //Log.e(TAG, ": dirfile.deleteOnExit()    ::    "  +  dirFile.deleteOnExit());Scary, returns void

        //e
        //Log.e(TAG, ": dirfile.exists()    ::    "  +   dirFile.getCanonicalPath());//Throws I/0. warns for T/C

        //g
        Log.e(TAG, ": dirfile.getAbsolutePath()    ::    " + dirFile.getAbsolutePath());
        Log.e(TAG, ": dirfile.getName()    ::    " + dirFile.getName());
        Log.e(TAG, ": dirfile.getParent()    ::    " + dirFile.getParent());
        Log.e(TAG, ": dirfile.getPath()    ::    " + dirFile.getPath());
        Log.e(TAG, ": dirfile.getAbsoluteFile()    ::    " + dirFile.getAbsoluteFile());
        //Log.e(TAG, ": dirfile.getCanonicalFile()    ::    " + dirFile.getCanonicalFile());//Throws I/0. warns for T/C
        Log.e(TAG, ": dirfile.getFreeSpace()    ::    " + dirFile.getFreeSpace());
        Log.e(TAG, ": dirfile.getParentFile()    ::    " + dirFile.getParentFile());
        Log.e(TAG, ": dirfile.getTotalSpace()    ::    " + dirFile.getTotalSpace());
        Log.e(TAG, ": dirfile.getUsableSpace()    ::    " + dirFile.getUsableSpace());

        //i
        Log.e(TAG, ": dirfile.isAbsolute()    ::    " + dirFile.isAbsolute());
        Log.e(TAG, ": dirfile.isDirectory()    ::    " + dirFile.isDirectory());
        Log.e(TAG, ": dirfile.isFile()    ::    " + dirFile.isFile());
        Log.e(TAG, ": dirfile.isHidden()    ::    " + dirFile.isHidden());

        //L
        Log.e(TAG, ": dirfile.list()    ::    " + Arrays.toString(dirFile.list()));
        //Log.e(TAG, ": dirfile.list(filter)    ::    "  +   dirFile.list(filter) );
        Log.e(TAG, ": dirfile.listFiles()    ::    " + Arrays.toString(dirFile.listFiles()));
        //Log.e(TAG, ": dirfile.listFiles(filter)    ::    "  +   dirFile.listFiles(filter) );

        //m
        //Log.e(TAG, ": dirfile.mkdir()    ::    " + dirFile.mkdir());//scary
        //Log.e(TAG, ": dirfile.mkdirs()    ::    " + dirFile.mkdirs());//scary

        //r
        //Log.e(TAG, ": dirfile.renameTo()    ::    "  +   dirFile.renameTo(path) );//scary

        //s
        //Log.e(TAG, ": dirfile.setExecutable()    ::    "  +   dirFile.setExecutable(bool,bool) );
        //Log.e(TAG, ": dirfile.setLastModified()    ::    "  +   dirFile.setLastModified(long) );
        //Log.e(TAG, ": dirfile.setReadable()    ::    "  +   dirFile.setReadable(bool,bool) );
        //Log.e(TAG, ": dirfile.setReadOnly()    ::    "  +   dirFile.setReadOnly() ); //scary
        //Log.e(TAG, ": dirfile.setWritable()    ::    "  +   dirFile.setWritable(bool,bool));

        //t
        Log.e(TAG, ": dirfile.toString()    ::    " + dirFile.toString());
        //Log.e(TAG, ": dirfile.toPath()    ::    "  +   dirFile.toPath() );//-> req minapi 26
        Log.e(TAG, ": dirfile.toURI()    ::    " + dirFile.toURI());


        if (dirFile.isDirectory() && dirFile.canRead()) {
            Log.e(TAG, ": directory is:" + dirFile);
            Log.e(TAG, ": -----------");

            StringBuilder res = new StringBuilder();
            if (dirFile.list().length == 0) {
                Log.e(TAG, "myTesting: {NO FILES/FOLDERS}");
            }
            for (String s : dirFile.list()) {
                res.append(s).append('\n');
            }

            Log.e(TAG, ": \n" + res);
            Log.e(TAG, ": -----------");

        } else {
            Log.e(TAG, ": dir file is null");
        }
    }


    public static void makeToast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    
}
