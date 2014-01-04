package bjtu.group6.droidcorder.service;

import java.io.File;
import java.io.IOException;

import android.util.Log;

/**
 * The basic operation of the file
 * @author Feng Xiangmin
 *
 */
public class FileOperation {
	 /**
     * delete a file
     * @param   sPath    filepath
     * @return delete sucess will return true, else false
     */
    public boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // The file exists then delete
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
    
    /**
     * update the filename
     * @param oldFilepath
     * @param newFilename: without the extension name
     * @return
     */
    public boolean updateFile(String oldFilepath, String newFilename)
    {
    	boolean flag = false;
    	File oldFile = new File(oldFilepath);
    	if(oldFile.exists())
    	{
    		try {
				oldFile.createNewFile();
				String rootPath = oldFile.getParent();
				String oldFilename = oldFile.getName();
				String extension = oldFilename.substring(oldFilename.lastIndexOf("."));
				File newFile = new File(rootPath + File.separator + newFilename + extension);
				if(oldFile.renameTo(newFile)){
					flag = true;
				}
			} catch (IOException e) {
				String errorString = "Rename file:" + oldFilepath + ", to " + newFilename + " failed!";
				Log.e("FileOperation", errorString);
			}
    	 }
    	
    	return flag;
    }
}
