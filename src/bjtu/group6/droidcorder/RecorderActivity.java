package bjtu.group6.droidcorder;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import bjtu.group6.droidcorder.R.drawable;
import bjtu.group6.droidcorder.service.FileOperation;
import bjtu.group6.droidcorder.service.RecorderTask;

public class RecorderActivity extends Activity {
	private Button _buttonRecord;
	private Chronometer _chronometer;

	private File _currentFile = null;

	private Boolean _recordMode = false;
	private RecorderTask _recorderTask;
	private MediaRecorder _recorder = null;
	private FileOperation fileOperation = new FileOperation();

	private SharedPreferences settingPreference;
	private Handler handler;

	private Runnable recordLimit = new Runnable() {
		@Override
		public void run() {
			stopRecord();
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recorder);
		if (!isExternalStorageWritable())
			this.finish();
		_buttonRecord = (Button) findViewById(R.id.ButtonRecordStop);
		_chronometer = (Chronometer) findViewById(R.id.Chronometer);
		settingPreference = this.getSharedPreferences("setting_preference",
				Context.MODE_PRIVATE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recorder, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
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

	public void onRecordClick(View view) {
		if (!_recordMode) {
			startRecord();
		} else {
			stopRecord();
		}
		_recordMode = !_recordMode;
	}
	
	private void startRecord() {
		_recorderTask = new RecorderTask();
		_currentFile = fileOperation.getStorageDir(this);
		_buttonRecord.setBackgroundResource(drawable.stop_record);
		_recorderTask.execute(_currentFile);
		_chronometer.setBase(SystemClock.elapsedRealtime());
		_chronometer.start();

		handler = new Handler();
		handler.postDelayed(recordLimit, settingPreference.getInt("DURATION_KEY", 30) * 1000);
	}

	private void stopRecord() {
		handler.removeCallbacks(recordLimit);
		_buttonRecord.setBackgroundResource(drawable.record);
		_recorderTask.cancel(true);
		_chronometer.stop();
		_recorderTask = null;
	}

	public void onListMediaClick(View view) {
		Intent i = new Intent(this, AudioListActivity.class);
		startActivity(i);
	}

	public void onSettingClick(View view) {
		Intent i = new Intent(this, ConfigActivity.class);
		startActivity(i);
	}

}
