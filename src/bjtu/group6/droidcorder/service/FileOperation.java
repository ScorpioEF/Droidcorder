package bjtu.group6.droidcorder.service;

import java.io.File;
import java.io.IOException;

import bjtu.group6.droidcorder.model.AudioFileInfo;

import android.content.ContentValues;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
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
        if (sPath == null || sPath.isEmpty()) {
			return flag;
		}
        
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
    
    /**
     * setAsCallRingtone
     * @param context
     * @param filepath
     * @return
     */
    public boolean setAsCallRingtone(Context context, AudioFileInfo audioFileInfo) {
    	boolean flag = true;
    	File ringtoneFile = new File(audioFileInfo.getFilePath());
    	
    	ContentValues content = new ContentValues();
    	content.put(MediaStore.MediaColumns.DATA,ringtoneFile.getAbsolutePath());
    	content.put(MediaStore.MediaColumns.TITLE, audioFileInfo.getFileName());
    	content.put(MediaStore.MediaColumns.SIZE, audioFileInfo.getFileSize());
    	content.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
    	content.put(MediaStore.Audio.Media.DURATION, audioFileInfo.getDuration());
    	content.put(MediaStore.Audio.Media.IS_RINGTONE, true);
    	content.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
    	content.put(MediaStore.Audio.Media.IS_ALARM, false);
    	content.put(MediaStore.Audio.Media.IS_MUSIC, false);
    	
    	 //Insert it into the database
        //Log.i("FileOperation", "the absolute path of the file is :"+ringtoneFile.getAbsolutePath());
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringtoneFile.getAbsolutePath());
        Uri ringtoneUri = context.getContentResolver().insert(uri, content);
        RingtoneManager.setActualDefaultRingtoneUri(context,RingtoneManager.TYPE_RINGTONE,ringtoneUri);
        Log.i("FileOperation","the ringtone uri is :"+ ringtoneUri);
        
    	return flag;
	}
    
    /**
     * set as alarm ringtone
     * @param context
     * @param audioFileInfo
     * @return
     */
    public boolean setAsAlarmRingtone(Context context, AudioFileInfo audioFileInfo) {
    	boolean flag = true;
    	File ringtoneFile = new File(audioFileInfo.getFilePath());
    	
    	ContentValues content = new ContentValues();
    	content.put(MediaStore.MediaColumns.DATA,ringtoneFile.getAbsolutePath());
    	content.put(MediaStore.MediaColumns.TITLE, audioFileInfo.getFileName());
    	content.put(MediaStore.MediaColumns.SIZE, audioFileInfo.getFileSize());
    	content.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
    	content.put(MediaStore.Audio.Media.DURATION, audioFileInfo.getDuration());
    	content.put(MediaStore.Audio.Media.IS_RINGTONE, false);
    	content.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
    	content.put(MediaStore.Audio.Media.IS_ALARM, true);
    	content.put(MediaStore.Audio.Media.IS_MUSIC, false);
    	
    	 //Insert it into the database
        Log.i("FileOperation", "the absolute path of the file is :"+ringtoneFile.getAbsolutePath());
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringtoneFile.getAbsolutePath());
        Uri ringtoneUri = context.getContentResolver().insert(uri, content);
        Log.i("FileOperation","the ringtone uri is :"+ ringtoneUri);
        RingtoneManager.setActualDefaultRingtoneUri(context,RingtoneManager.TYPE_ALARM,ringtoneUri);

    	return flag;
	}
    
    /**
     * set as notification ringtones
     * @param context
     * @param audioFileInfo
     * @return
     */
    public boolean setAsNotificationRingtone(Context context, AudioFileInfo audioFileInfo) {
    	boolean flag = true;
    	File ringtoneFile = new File(audioFileInfo.getFilePath());
    	
    	ContentValues content = new ContentValues();
    	content.put(MediaStore.MediaColumns.DATA,ringtoneFile.getAbsolutePath());
    	content.put(MediaStore.MediaColumns.TITLE, audioFileInfo.getFileName());
    	content.put(MediaStore.MediaColumns.SIZE, audioFileInfo.getFileSize());
    	content.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
    	content.put(MediaStore.Audio.Media.DURATION, audioFileInfo.getDuration());
    	content.put(MediaStore.Audio.Media.IS_RINGTONE, false);
    	content.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
    	content.put(MediaStore.Audio.Media.IS_ALARM, false);
    	content.put(MediaStore.Audio.Media.IS_MUSIC, false);
    	
    	 //Insert it into the database
        Log.i("FileOperation", "the absolute path of the file is :"+ringtoneFile.getAbsolutePath());
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringtoneFile.getAbsolutePath());
        Uri ringtoneUri = context.getContentResolver().insert(uri, content);
        Log.i("FileOperation","the ringtone uri is :"+ ringtoneUri);
        RingtoneManager.setActualDefaultRingtoneUri(context,RingtoneManager.TYPE_NOTIFICATION,ringtoneUri);

    	return flag;
	}
    
}
