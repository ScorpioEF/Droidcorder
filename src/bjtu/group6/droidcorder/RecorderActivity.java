package bjtu.group6.droidcorder;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import bjtu.group6.droidcorder.R.drawable;
import bjtu.group6.droidcorder.service.FileOperation;
import bjtu.group6.droidcorder.service.RecorderTask;

public class RecorderActivity extends Activity {
	Button _buttonRecord;
	Chronometer _chronometer;

	File _currentFile = null;

	Boolean _recordMode = false;
	RecorderTask _recorderTask;
	private MediaRecorder _recorder = null;
	private FileOperation fileOperation =new FileOperation();

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


	public void onRecordClick(View view) {
		if (!_recordMode) {
			_recorderTask = new RecorderTask();
			_currentFile = fileOperation.getStorageDir();
			_buttonRecord.setBackgroundResource(drawable.stop_record);
			_recorderTask.execute(_currentFile);
			_chronometer.setBase(SystemClock.elapsedRealtime());
			_chronometer.start();
		} else {
			_buttonRecord.setBackgroundResource(drawable.record);
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
	
	public void onSettingClick(View view){
		Intent intent = new Intent();
    	intent.setClass(RecorderActivity.this, ConfigActivity.class);
    	startActivity(intent);
    	RecorderActivity.this.finish();
	}
	
}
