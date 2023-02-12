package in.curioustools.filemanager;


import androidx.annotation.NonNull;

import java.io.File;


//uses utilities : getFileFolderIcon(),getTimeString(), getMemoryString90
class RvData {
    private String fileFolderName, otherInfo1,dateCreated;
    private int iconRes;

    public RvData(File childFile){
        initVariablesFromFile(childFile);
    }

    private void initVariablesFromFile(File childFile) {
        this.fileFolderName=childFile.getName();
        this.dateCreated = "Last Modified: "+Utilities.getTimeString(childFile.lastModified());

        if(childFile.isDirectory() && childFile.canRead()){
            this.otherInfo1 =""+ childFile.list().length+ " Files."; //lol, this is the total no. of files in dir
            this.iconRes = Utilities.getIconRes("folder");
        }
        else {
            this.otherInfo1= Utilities.getMemoryString(childFile.length()); // while this is the file size, but both are lengths
            String childpath=childFile.toString();
            String extension =childpath.substring(childpath.lastIndexOf("."));

            this.iconRes = Utilities.getIconRes(extension);
        }


    }

    public String getFileFolderName() {
        return fileFolderName;
    }
    public String getOtherInfo1() {
        return otherInfo1;
    }
    public String getDateCreated() {
        return dateCreated;
    }
    public int getIconRes() {
        return iconRes;
    }

    public void setFileFolderName(String fileFolderName) {
        this.fileFolderName = fileFolderName;
    }
    public void setOtherInfo1(String otherInfo1) {
        this.otherInfo1 = otherInfo1;
    }
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    @NonNull
    @Override
    public String toString() {
        return "RvData{" +
                "fileFolderName='" + fileFolderName + '\'' +
                ", otherInfo1='" + otherInfo1 + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", iconRes=" + iconRes +
                '}';
    }


}
