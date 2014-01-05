package bjtu.group6.droidcorder;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import bjtu.group6.droidcorder.R;
import bjtu.group6.droidcorder.service.RecorderTask;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;

public class RecorderActivity extends Activity {
	Button _buttonRecord;
	Button buttonAudioList;

	Chronometer _chronometer;

	String _path = "/Droidcorder";

	File _currentFile = null;

	Boolean _recordMode = false;
	RecorderTask _recorderTask;
	private MediaRecorder _recorder = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recorder);
		buttonAudioList = (Button)this.findViewById(R.id.btnAudioList);
		setListeners();

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


	private void setListeners() {
		buttonAudioList.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(RecorderActivity.this, AudioListActivity.class);
				startActivity(intent);
				RecorderActivity.this.finish();
			}

		});

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
		_chronometer = (Chronometer) findViewById(R.id.Chronometer);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (_recorder != null) {
			_recorder.release();
			_recorder = null;
		}
	}

	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state))
			return true;
		return false;
	}

	public File getStorageDir(String path) {
		if (!Environment.getExternalStoragePublicDirectory(path).isDirectory()) {
			Environment.getExternalStoragePublicDirectory(path).mkdir();
			//Log.e(LOG_TAG, "Directory not created");
		}
		File file = new File(Environment.getExternalStoragePublicDirectory(path), generateFileName() + ".3gp");
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

}
