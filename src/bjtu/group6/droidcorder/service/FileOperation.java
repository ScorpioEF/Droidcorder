package bjtu.group6.droidcorder.service;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import bjtu.group6.droidcorder.model.AudioFileInfo;

/**
 * The basic operation of the file
 * @author Feng Xiangmin
 *
 */
public class FileOperation {

	private String path = "/Droidcorder";
	private ArrayList<AudioFileInfo> audioFiles = new ArrayList<AudioFileInfo>();

	public ArrayList<AudioFileInfo> getAudioFileList() {
		File dir = Environment.getExternalStoragePublicDirectory(path);
		if (dir.isDirectory()) {

			String[] filelist = dir.list();
			for (int i = 0; i < filelist.length; i++) {
				File readfile = new File(dir + "/" + filelist[i]);
				if (!readfile.isDirectory()) {
					AudioFileInfo audioFileInfo = new AudioFileInfo();
					audioFileInfo.setFileName(readfile.getName().split("\\.")[0]);
					audioFileInfo.setFilePath(readfile.getPath());
					audioFileInfo.setFileSize(formatFileSize(readfile.length()));
					Long time =readfile.lastModified();
					Calendar cd = Calendar.getInstance();
					cd.setTimeInMillis(time);
					audioFileInfo.setCreateTime(new SimpleDateFormat("yyyy-MM-dd").format(cd.getTime()));
					MediaPlayer audioPlayer = new MediaPlayer();
					try {
						audioPlayer.setDataSource(readfile.getPath());
						audioPlayer.prepare();
					} catch (IOException e) {
						Log.e("Get Media", "Error");
					}
					audioFileInfo.setDuration(formatTime(audioPlayer.getDuration()));
					audioPlayer.release();
					Log.e("audio file path=",readfile.getPath());
					Log.e("absolutepath=", readfile.getAbsolutePath());
					Log.e("name=", readfile.getName());
					audioFiles.add(audioFileInfo);
				}
			}
		}
		return audioFiles;
	}

	public String formatTime(int duration) {
		int audioTime = duration/1000;
		int minute = audioTime/60;
		int second = audioTime%60;
		DecimalFormat df = new DecimalFormat("00");
		return String.valueOf(df.format(minute)) + ":" + String.valueOf(df.format(second));
	}

	public File getStorageDir() {
		if (!Environment.getExternalStoragePublicDirectory(path).isDirectory()) {
			Environment.getExternalStoragePublicDirectory(path).mkdir();
		}
		File file = new File(Environment.getExternalStoragePublicDirectory(path), generateFileName() + ".3gp");
		return file;
	}


	private String generateFileName() {
		return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date().getTime());
	}

	public String formatFileSize(long fileLength)
	{
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileLength < 1024)
		{
			fileSizeString = df.format((double) fileLength) + "B";
		}
		else if (fileLength < 1048576)
		{
			fileSizeString = df.format((double) fileLength / 1024) + "K";
		}
		else if (fileLength < 1073741824)
		{
			fileSizeString = df.format((double) fileLength / 1048576) + "M";
		}
		else
		{
			fileSizeString = df.format((double) fileLength / 1073741824) + "G";
		}
		return fileSizeString;
	}
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
