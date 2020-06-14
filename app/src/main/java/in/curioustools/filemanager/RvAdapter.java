package in.curioustools.filemanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;

@SuppressWarnings("unused")
public class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvHolder> {
    private File currentDir;
    private String[] currentDirChildren;

    RvAdapter(File currentDirFile) {
        if (currentDirFile != null) {
            this.currentDir = currentDirFile;
            if (currentDirFile.canRead()) {
//            FilenameFilter filter = new FilenameFilter() {
//                @Override
//                 boolean accept(File dir, String name) {
//                    return name.endsWith(".mp3")||name.endsWith(".MP3");
//                }
//            };
//            this.currentDirChildren=currentDirFile.list(filter);
                this.currentDirChildren = currentDirFile.list();

            } else {
                this.currentDirChildren = new String[0];
            }
        } else {
            this.currentDirChildren = new String[0];
        }
        Arrays.sort(this.currentDirChildren,String.CASE_INSENSITIVE_ORDER);
    }

    @NonNull
    @Override
    public RvHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_eachrow_rv, parent, false);
        return new RvHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RvHolder rvHolder, int pos) {
        File currentChildFile = new File(currentDir, currentDirChildren[pos]);
        RvData currentRowData = new RvData(currentChildFile);
        rvHolder.bind(currentRowData);
    }

    @Override
    public int getItemCount() {
        return currentDirChildren.length;
    }

    File getCurrentDir() {
        return currentDir;
    }

    public String[] getCurrentDirChildren() {
        return currentDirChildren;
    }

    void updateCurrentDirAndChildren(File newDirFile, String[] newDirChildren) {
        this.currentDir = newDirFile;
        this.currentDirChildren = newDirChildren;
        Arrays.sort(this.currentDirChildren,String.CASE_INSENSITIVE_ORDER);

        notifyDataSetChanged();

    }

    class RvHolder extends RecyclerView.ViewHolder {
        private TextView tvFoldername, tvOtherInfo1, tvDateCreated;
        private ImageView ivFFIcon;

        RvHolder(@NonNull View itemView) {
            super(itemView);
            tvFoldername = itemView.findViewById(R.id.text_filefolder_name);
            tvDateCreated = itemView.findViewById(R.id.text_filefolder_datecreated);
            tvOtherInfo1 = itemView.findViewById(R.id.text_filefolder_info1);
            ivFFIcon = itemView.findViewById(R.id.iv_filefolder_icon);
            itemView.setOnClickListener(v -> onItemClick());
        }

        private void onItemClick() {
            int i = getAdapterPosition();
            String data = currentDirChildren[i];
            File pressedContentFile = new File(currentDir.toString() + '/' + data);
            if (pressedContentFile.canRead()) {

                if (pressedContentFile.isDirectory()) {
                    updateCurrentDirAndChildren(pressedContentFile, pressedContentFile.list());
                } else {
                    /* TODO: 08-03-2019 open file */
                    Utilities.logFileDetails(pressedContentFile);
                    openFile(pressedContentFile, itemView.getContext());
//                    itemView.getContext().startActivity(Intent.createChooser(intent, "Open With..."));
                }
            } else {
                Utilities.makeToast(itemView.getContext(), "Sorry, cannot read current FileFolder");
            }

        }

        private void openFile(File file, Context ctx) {
            MimeTypeMap map = MimeTypeMap.getSingleton();

            String filepath = file.toString();
            String ext =filepath.substring(filepath.lastIndexOf(".")+ 1);
            String type = map.getMimeTypeFromExtension(ext);
            if (type == null)
                type = "*/*";

            Intent intent = new Intent(Intent.ACTION_VIEW);
//          Uri data = Uri.fromFile(file);
            Uri data = FileProvider.getUriForFile(ctx, "in.curioustools.filemanager", file);

            intent.setDataAndType(data, type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            ctx.startActivity(intent);
        }

        void bind(RvData data) {
            tvFoldername.setText(data.getFileFolderName());
            tvOtherInfo1.setText(data.getOtherInfo1());
            tvDateCreated.setText(data.getDateCreated());
            ivFFIcon.setImageResource(data.getIconRes());

        }

    }


    /*

    If your targetSdkVersion >= 24, then we have to use FileProvider class to give access to the particular file or folder to make them accessible for other apps. We create our own class inheriting FileProvider in order to make sure our FileProvider doesn't conflict with FileProviders declared in imported dependencies as described here.

Steps to replace file:// URI with content:// URI:

Add a class extending FileProvider

public class GenericFileProvider extends FileProvider {}
Add a FileProvider <provider> tag in AndroidManifest.xml under <application> tag. Specify a unique authority for the android:authorities attribute to avoid conflicts, imported dependencies might specify ${applicationId}.provider and other commonly used authorities.

<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    ...
    <application
        ...
        <provider
            android:name=".GenericFileProvider"
            android:authorities="${applicationId}.my.package.name.provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/provider_paths"/>
        </provider>
    </application>
</manifest>
Then create a provider_paths.xml file in res/xml folder. Folder may be needed to created if it doesn't exist. The content of the file is shown below. It describes that we would like to share access to the External Storage at root folder (path=".") with the name external_files.
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-path name="external_files" path="."/>
</paths>
The final step is to change the line of code below in

Uri photoURI = Uri.fromFile(createImageFile());
to

Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".my.package.name.provider", createImageFile());
Edit: If you're using an intent to make the system open your file, you may need to add the following line of code:

intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


    */

}
