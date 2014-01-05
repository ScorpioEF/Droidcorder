package bjtu.group6.droidcorder;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import bjtu.group6.droidcorder.model.AudioFileInfo;
import bjtu.group6.droidcorder.service.AudioListAdapter;
import bjtu.group6.droidcorder.service.FileOperation;

public class AudioListActivity extends Activity {
	private ListView audioList;
	private MediaPlayer _player = null;

	private final FileOperation fileOperation = new FileOperation();
	private ArrayList<AudioFileInfo> audioFiles = new ArrayList<AudioFileInfo>();

	private Boolean _playMode = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_list);
		findViews();

		// For test
		AudioFileInfo audioinfo = new AudioFileInfo();
		audioinfo.setAudioId(111111);
		audioinfo.setFileName("test1");
		audioinfo.setDuration("10.50");
		audioinfo.setCreateTime("2014.1.1");
		audioinfo.setFilePath("/mnt/sdcard/test1.mp3");

		AudioFileInfo audioinfo2 = new AudioFileInfo();
		audioinfo2.setAudioId(111222);
		audioinfo2.setFileName("Audio2");
		audioinfo2.setDuration("21.50");
		audioinfo2.setCreateTime("2014.1.2");
		audioFiles.add(audioinfo);
		audioFiles.add(audioinfo2);

		audioList.setAdapter(new AudioListAdapter(audioFiles,
				AudioListActivity.this));

		setListeners();

	}

	@Override
	public void onPause() {
		super.onPause();
		if (_player != null) {
			_player.release();
			_player = null;
		}
	}
	private void setListeners() {
		audioList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				AudioFileInfo audioInfo = (AudioFileInfo) audioList.getItemAtPosition(arg2);

				if (_player != null && _player.isPlaying()) {
					_player.stop();
					
				}
				else {
					_player = new MediaPlayer();
					try {
						//TODO
						//_player.setDataSource(audioInfo.getFilePath());
						//test
						_player.setDataSource("/storage/sdcard0/Droidcorder/2014-01-05 13:56:02.829.3gp");
						_player.prepare();
						_player.start();
						_playMode = true;
					} catch (IOException e) {
						Log.e("youpi", "prepare() failed");
					}
				}
			}

		});
		//the listview menu Long time click item listener, is combined with the onContextItemSelected function
		audioList.setOnCreateContextMenuListener(new OnCreateContextMenuListener()
		{
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
			{
				menu.setHeaderTitle("Operations:");//标题
				menu.add(0, 0, 0, "Details");
				menu.add(0, 1, 0, "Rename");
				menu.add(0, 2, 0, "Share");
				menu.add(0, 3, 0, "Delete");
				menu.add(0, 4, 0, "Set as ringtone");
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.audio_list, menu);
		return true;
	}

	private void findViews() {
		audioList = (ListView) this.findViewById(R.id.audioList);
	}

	//the listview menu listener, is combined with the onContextItemSelected function
	OnCreateContextMenuListener listviewOnCreateContextMenuListener = new OnCreateContextMenuListener()
	{

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
		{
			menu.setHeaderTitle("Operations:");//标题
			menu.add(0, 0, 0, "Details");
			menu.add(0, 1, 0, "Rename");
			menu.add(0, 2, 0, "Share");
			menu.add(0, 3, 0, "Delete");
			menu.add(0, 4, 0, "Set as ringtone");
		}
	};


	/**
	 * Long time click item response function and get the detail info of the chosen item
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{

		ContextMenuInfo info = item.getMenuInfo();
		AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
		// get the item position
		int position = contextMenuInfo.position;
		final AudioFileInfo audioFileInfo = audioFiles.get(position);
		Log.i("AudioListActivity", "Get the audioFile info:" + audioFileInfo.getFileName());

		switch (item.getItemId()) {
		case 0:
			// Details
			showDetails(audioFileInfo);
			break;
		case 1:
			// Rename
			rename(audioFileInfo);		
			break;
		case 2:
			// Share
			share(audioFileInfo);
			break;
		case 3:
			//Delete
			delete(audioFileInfo);
			break;
		case 4:
			//set as ringtone
			setAsRingtone(audioFileInfo);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * Show detail info
	 * @author FengXiangmin
	 * @param audioFileInfo
	 */
	private void showDetails(AudioFileInfo audioFileInfo){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);  
		String[] message = {"Filename:" + audioFileInfo.getFileName(),
				"FilePath:" + audioFileInfo.getFilePath(),
				"CreateTime:" + audioFileInfo.getCreateTime(),
				"FileSize:" + audioFileInfo.getFileSize(),
				"Duration:" + audioFileInfo.getDuration()};

		builder.setTitle("Details info");  
		builder.setItems(message,null);  
		builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int which)  
			{  
				dialog.dismiss();  
			}  
		});  
		builder.show(); 
	}


	/**
	 * rename
	 * @author FengXiangmin
	 * @param audioFileInfo
	 */
	private void rename(final AudioFileInfo audioFileInfo){
		final EditText editText = new EditText(this);
		final String oldFileName = audioFileInfo.getFileName();
		new AlertDialog.Builder(this)
		.setTitle("Input new name")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setView(editText)
		.setPositiveButton("Yes", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// rename the filename
				String newFilename = editText.getText().toString();
				boolean renameFlag = fileOperation.updateFile(audioFileInfo.getFilePath(), newFilename);
				if(renameFlag){
					Log.i("AudioListActivity", "rename file " + oldFileName + " to name " + newFilename + " success!");
					Toast.makeText(AudioListActivity.this, "Rename file " + oldFileName + " success!", Toast.LENGTH_SHORT).show();
				}else{
					Log.e("AudioListActivity", "rename file " + oldFileName + " to name " + newFilename + " failed!");
					Toast.makeText(AudioListActivity.this, "Rename file " + oldFileName + " failed!", Toast.LENGTH_SHORT).show();
				}
			}
		})
		.setNegativeButton("Cancle", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Cancle to rename
				Log.i("AudioListActivity", "rename file " +oldFileName + " cancled!");
			}
		})
		.show();
	}

	/**
	 * delete
	 * @author FengXiangmin
	 * @param audioFileInfo
	 */
	private void delete(AudioFileInfo audioFileInfo)
	{
		final String oldFileName = audioFileInfo.getFileName();
		boolean deleteFlag = fileOperation.deleteFile(audioFileInfo.getFilePath());
		if(deleteFlag){
			Log.i("AudioListActivity", "delete file " + audioFileInfo.getFilePath() + " success!");
			Toast.makeText(AudioListActivity.this, "Delete file " + oldFileName + " success!", Toast.LENGTH_SHORT).show();
		}else{
			Log.e("AudioListActivity", "delete file " + audioFileInfo.getFilePath() + " failed!");
			Toast.makeText(AudioListActivity.this, "Delete file " + oldFileName + " failed!", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * setAsRingtone
	 * @author FengXiangmin
	 * @param audioFileInfo
	 */
	private void setAsRingtone(final AudioFileInfo audioFileInfo){
		String[] choices={"Phone Ringtone","Alarm Ringtone","Notification Ringtone"};
		AlertDialog dialog = new AlertDialog.Builder(this)  
		.setTitle("Set As ...")  
		.setItems(choices, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String message;
				switch (which) {  
		         case 0:  
		        	fileOperation.setAsCallRingtone(AudioListActivity.this, audioFileInfo);
		        	message = "Set file " + audioFileInfo.getFileName() + " as Phone Ringtone success!";
		     		Log.i("AudioListActivity", message);
		     		Toast.makeText(AudioListActivity.this, message, Toast.LENGTH_SHORT).show();  
		            break;  
		         case 1:  
		        	 fileOperation.setAsAlarmRingtone(AudioListActivity.this, audioFileInfo);
		        	 message = "Set file " + audioFileInfo.getFileName() + " as Alarm Ringtone success!";
			     	 Log.i("AudioListActivity", message);
			     	 Toast.makeText(AudioListActivity.this, message, Toast.LENGTH_SHORT).show(); 
			         break;  
		         case 2:  
		        	 fileOperation.setAsNotificationRingtone(AudioListActivity.this, audioFileInfo);
		        	 message = "Set file " + audioFileInfo.getFileName() + " as Notification Ringtone success!";
			     	 Log.i("AudioListActivity", message);
			     	 Toast.makeText(AudioListActivity.this, message, Toast.LENGTH_SHORT).show();  
			         break;    
				
			}
		}}).create();  
		 dialog.show();  
		
	 }	 
	 
	 private void share(final AudioFileInfo audioFileInfo)
	 {
			Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
			sharingIntent.setType("audio/*");
			sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(audioFileInfo.getFilePath()));
			startActivity(Intent.createChooser(sharingIntent, "Share Sound File"));
	 }
}
