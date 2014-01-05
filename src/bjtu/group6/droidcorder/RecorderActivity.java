package bjtu.group6.droidcorder;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import bjtu.group6.droidcorder.R;
import bjtu.group6.droidcorder.service.RecorderTask;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class RecorderActivity extends Activity {
	Button _buttonRecord;
	Button _buttonPlay;

	Chronometer _chronometer;

	String _path = "/";

	File _currentFile = null;

	Boolean _recordMode = false;
	Boolean _playMode = false;
	RecorderTask _recorderTask;
	private MediaRecorder _recorder = null;
	private MediaPlayer _player = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recorder);
		
//	    final AlertDialog.Builder alert = new AlertDialog.Builder(this);
//	    alert.setTitle(R.string.filename);
//	    final EditText input = new EditText(this);
//	    alert.setView(input);
//	    alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//	        public void onClick(DialogInterface dialog, int whichButton) {
//	            String value = input.getText().toString().trim();
//	        }
//	    });
//	    alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//	        public void onClick(DialogInterface dialog, int whichButton) {
//	            dialog.cancel();
//	        }
//	    });
//	    alert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recorder, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isExternalStorageWritable())
			this.finish();
		_buttonRecord = (Button) findViewById(R.id.ButtonRecordStop);
		//_buttonPlay = (Button) findViewById(R.id.ButtonPlayStop);
		_chronometer = (Chronometer) findViewById(R.id.Chronometer);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (_recorder != null) {
			_recorder.release();
			_recorder = null;
		}

		if (_player != null) {
			_player.release();
			_player = null;
		}
	}

	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state))
			return true;
		return false;
	}

	public File getStorageDir(String path) {
		File file = new File(Environment.getExternalStoragePublicDirectory(path), generateFileName() + ".3gp");
		//if (!file.mkdirs()) {
			// Log.e(LOG_TAG, "Directory not created");
		//}
		return file;
	}

	private String generateFileName() {
		 return new Timestamp(new Date().getTime()).toString();
	}

	public void onRecordClick(View view) {
		if (!_recordMode) {
			_recorderTask = new RecorderTask();
			_currentFile = getStorageDir(_path);
			_buttonRecord.setText(R.string.recording);
			_recorderTask.execute(_currentFile);
			_chronometer.setBase(SystemClock.elapsedRealtime());
			_chronometer.start();
		} else {
			_buttonRecord.setText(R.string.record);
			_recorderTask.cancel(true);
			_chronometer.stop();
			_recorderTask = null;
		}
		_recordMode = !_recordMode;
	}

	public void onListMediaClick(View view)
	{
		Intent i = new Intent(this, AudioListActivity.class);
		startActivity(i);
	}
	
	public void onPlayClick(View view) {
		_player = new MediaPlayer();
		if (_currentFile == null)
			return;
		if (!_playMode) {
			try {
				_player.setDataSource(_currentFile.getAbsolutePath());
				_player.prepare();
				_player.start();
				_buttonPlay.setText(R.string.stop);
			} catch (IOException e) {
				Log.e("youpi", "prepare() failed");
			}
		} else {
			_player.release();
			_player = null;
			_buttonPlay.setText(R.string.play);
		}
		_playMode = !_playMode;
	}
}
