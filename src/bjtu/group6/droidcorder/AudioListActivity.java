package bjtu.group6.droidcorder;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import bjtu.group6.droidcorder.model.AudioFileInfo;
import bjtu.group6.droidcorder.service.AudioListAdapter;
import bjtu.group6.droidcorder.service.FileOperation;

public class AudioListActivity extends Activity {
	private ListView audioList;
	private SeekBar playSeekBar;
	private TextView currentTime;
	private TextView totalTime;
	private MediaPlayer audioPlayer = null;

	private final FileOperation fileOperation = new FileOperation();
	private ArrayList<AudioFileInfo> audioFiles = new ArrayList<AudioFileInfo>();

	private int lastIndex = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_list);
		findViews();

		audioFiles = fileOperation.getAudioFileList();

		audioList.setAdapter(new AudioListAdapter(audioFiles,
				AudioListActivity.this));

		setListeners();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		audioStop();
	}

	private void audioStop() {
		if (lastIndex != -1)
		{
			audioList.getChildAt(lastIndex).setBackgroundColor(Color.TRANSPARENT);
			lastIndex = -1;
		}

		if (audioPlayer != null) {
			audioPlayer.stop();
			audioPlayer.release();
			audioPlayer = null;
			handler.removeCallbacks(updateThread);
		}
	}

	Handler handler = new Handler();
	Runnable updateThread = new Runnable() {
		public void run() {
			playSeekBar.setProgress(audioPlayer.getCurrentPosition());
			handler.postDelayed(updateThread, 100);
			currentTime.setText(String.valueOf(fileOperation
					.formatTime(audioPlayer.getCurrentPosition())));
		}
	};

	private void setListeners() {
		audioList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				AudioFileInfo audioInfo = (AudioFileInfo) audioList
						.getItemAtPosition(arg2);
				if (arg2 == lastIndex && audioPlayer != null
						&& audioPlayer.isPlaying()) {
					audioPlayer.pause();
				} else if (arg2 == lastIndex && audioPlayer != null) {
					audioPlayer.start();
				} else {
					if (lastIndex != -1 && arg2 != lastIndex) {
						audioStop();
						// audioPlayer.stop();
						//audioList.getChildAt(lastIndex).setBackgroundColor(
						//		Color.TRANSPARENT);
						// handler.removeCallbacks(updateThread);
					}
					audioPlayer = new MediaPlayer();
					audioPlayer.setOnCompletionListener(onCompletion);
					try {
						audioPlayer.setDataSource(audioInfo.getFilePath());
						audioPlayer.prepare();
						audioPlayer.start();
						playSeekBar.setMax(audioPlayer.getDuration());
						totalTime.setText(String.valueOf(fileOperation
								.formatTime(audioPlayer.getDuration())));
						handler.post(updateThread);
						audioList.getChildAt(arg2).setBackgroundColor(
								Color.LTGRAY);
					} catch (IOException e) {
						Log.e("Group6", "start player error");
					}
				}
				lastIndex = arg2;
			}
		});
		// the listview menu Long time click item listener, is combined with the
		// onContextItemSelected function
		audioList
		.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.setHeaderTitle("Operations:");// БъЬт
				menu.add(0, 0, 0, "Details");
				menu.add(0, 1, 0, "Rename");
				menu.add(0, 2, 0, "Share");
				menu.add(0, 3, 0, "Delete");
				menu.add(0, 4, 0, "Set as ringtone");

			}
		});

		playSeekBar
		.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar,
					int progress, boolean fromUser) {
				if (fromUser == true) {
					if (audioPlayer != null) {
						audioPlayer.seekTo(progress);
						currentTime.setText(String.valueOf(fileOperation
								.formatTime(progress)));
					}
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
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
		playSeekBar = (SeekBar) findViewById(R.id.audioList_play_seekBar);
		currentTime = (TextView) this.findViewById(R.id.audioList_currentTime);
		totalTime = (TextView) this.findViewById(R.id.audioList_totalTime);
		playSeekBar.setProgress(0);
	}

	/**
	 * Long time click item response function and get the detail info of the
	 * chosen item
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		ContextMenuInfo info = item.getMenuInfo();
		AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
		// get the item position
		int position = contextMenuInfo.position;
		final AudioFileInfo audioFileInfo = audioFiles.get(position);
		Log.i("AudioListActivity",
				"Get the audioFile info:" + audioFileInfo.getFileName());

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
			// Delete
			delete(audioFileInfo);
			break;
		case 4:
			// set as ringtone
			setAsRingtone(audioFileInfo);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * Show detail info
	 * 
	 * @author FengXiangmin
	 * @param audioFileInfo
	 */
	private void showDetails(AudioFileInfo audioFileInfo) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String[] message = { "Filename:" + audioFileInfo.getFileName(),
				"FilePath:" + audioFileInfo.getFilePath(),
				"CreateTime:" + audioFileInfo.getCreateTime(),
				"FileSize:" + audioFileInfo.getFileSize(),
				"Duration:" + audioFileInfo.getDuration() };

		builder.setTitle("Details info");
		builder.setItems(message, null);
		builder.setPositiveButton("OK",
				new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * rename
	 * 
	 * @author FengXiangmin
	 * @param audioFileInfo
	 */
	private void rename(final AudioFileInfo audioFileInfo) {
		final EditText editText = new EditText(this);
		editText.setText(audioFileInfo.getFileName());
		final String oldFileName = audioFileInfo.getFileName();
		new AlertDialog.Builder(this).setTitle("Input new name")
		.setIcon(android.R.drawable.ic_dialog_info).setView(editText)
		.setPositiveButton("Yes", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// rename the filename
				String newFilename = editText.getText().toString();
				boolean renameFlag = fileOperation.updateFile(
						audioFileInfo.getFilePath(), newFilename);
				if (renameFlag) {
					Log.i("AudioListActivity", "rename file "
							+ oldFileName + " to name " + newFilename
							+ " success!");
					Toast.makeText(AudioListActivity.this,
							"Rename file " + oldFileName + " success!",
							Toast.LENGTH_SHORT).show();
				} else {
					Log.e("AudioListActivity", "rename file "
							+ oldFileName + " to name " + newFilename
							+ " failed!");
					Toast.makeText(AudioListActivity.this,
							"Rename file " + oldFileName + " failed!",
							Toast.LENGTH_SHORT).show();
				}
				// refresh the listview
				audioFiles.clear();
				audioFiles = fileOperation.getAudioFileList();

				audioList.setAdapter(new AudioListAdapter(audioFiles,
						AudioListActivity.this));
				BaseAdapter sAdapter = (BaseAdapter) audioList
						.getAdapter();
				sAdapter.notifyDataSetChanged();
			}
		}).setNegativeButton("Cancel", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Cancel to rename
				Log.i("AudioListActivity", "rename file " + oldFileName
						+ " Canceled!");
			}
		}).show();

	}

	/**
	 * delete
	 * 
	 * @author FengXiangmin
	 * @param audioFileInfo
	 */
	private void delete(final AudioFileInfo audioFileInfo) {
		final String oldFileName = audioFileInfo.getFileName();
		new AlertDialog.Builder(this).setTitle("Delete Confirm")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setMessage("Delete file " + oldFileName + "?")
		.setPositiveButton("Yes", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// delete the file
				boolean deleteFlag = fileOperation
						.deleteFile(audioFileInfo.getFilePath());
				if (deleteFlag) {
					Log.i("AudioListActivity", "delete file "
							+ audioFileInfo.getFilePath() + " success!");
					Toast.makeText(AudioListActivity.this,
							"Delete file " + oldFileName + " success!",
							Toast.LENGTH_SHORT).show();
				} else {
					Log.e("AudioListActivity", "delete file "
							+ audioFileInfo.getFilePath() + " failed!");
					Toast.makeText(AudioListActivity.this,
							"Delete file " + oldFileName + " failed!",
							Toast.LENGTH_SHORT).show();
				}
				// refresh the listview
				audioFiles.clear();
				audioFiles = fileOperation.getAudioFileList();

				audioList.setAdapter(new AudioListAdapter(audioFiles,
						AudioListActivity.this));
				BaseAdapter sAdapter = (BaseAdapter) audioList
						.getAdapter();
				sAdapter.notifyDataSetChanged();
			}
		}).setNegativeButton("Cancel", null).show();
	}

	/**
	 * setAsRingtone
	 * 
	 * @author FengXiangmin
	 * @param audioFileInfo
	 */
	private void setAsRingtone(final AudioFileInfo audioFileInfo) {
		String[] choices = { "Phone Ringtone", "Alarm Ringtone",
		"Notification Ringtone" };
		AlertDialog dialog = new AlertDialog.Builder(this)
		.setTitle("Set As ...")
		.setItems(choices, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String message;
				switch (which) {
				case 0:
					fileOperation.setAsCallRingtone(
							AudioListActivity.this, audioFileInfo);
					message = "Set file " + audioFileInfo.getFileName()
							+ " as Phone Ringtone success!";
					Log.i("AudioListActivity", message);
					Toast.makeText(AudioListActivity.this, message,
							Toast.LENGTH_SHORT).show();
					break;
				case 1:
					fileOperation.setAsAlarmRingtone(
							AudioListActivity.this, audioFileInfo);
					message = "Set file " + audioFileInfo.getFileName()
							+ " as Alarm Ringtone success!";
					Log.i("AudioListActivity", message);
					Toast.makeText(AudioListActivity.this, message,
							Toast.LENGTH_SHORT).show();
					break;
				case 2:
					fileOperation.setAsNotificationRingtone(
							AudioListActivity.this, audioFileInfo);
					message = "Set file " + audioFileInfo.getFileName()
							+ " as Notification Ringtone success!";
					Log.i("AudioListActivity", message);
					Toast.makeText(AudioListActivity.this, message,
							Toast.LENGTH_SHORT).show();
					break;

				}
			}
		}).create();
		dialog.show();

	}

	private void share(final AudioFileInfo audioFileInfo) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("audio/*");
		sharingIntent.putExtra(Intent.EXTRA_STREAM,
				Uri.parse(audioFileInfo.getFilePath()));
		startActivity(Intent.createChooser(sharingIntent, "Share Sound File"));
	}

	public void onBackClick(View view) {
		this.finish();
	}

	private MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			audioStop();
		}
	};
}
