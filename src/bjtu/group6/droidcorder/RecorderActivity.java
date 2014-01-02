package bjtu.group6.droidcorder;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class RecorderActivity extends Activity {
	Button _buttonRecord;
	Button _buttonPlay;
	
	Boolean _recordMode = false;
	Boolean _playMode = false;
	//RecorderTask _recorderTask;
	private MediaRecorder _recorder = null;
	private MediaPlayer _player = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recorder);
		//_recorderTask = new RecorderTask();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recorder, menu);
		return true;
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		_buttonRecord = (Button) findViewById(R.id.ButtonRecordStop);
		_buttonPlay = (Button) findViewById(R.id.ButtonPlayStop);
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
	
	public void onRecordClick(View view)
	{
		File file = new File(this.getFilesDir(), "test.3gp");
		if (!_recordMode)
		{
			_buttonRecord.setText(R.string.recording);
			startRecording(file);
			//_recorderTask.execute(file);
		}
		else
		{
			_buttonRecord.setText(R.string.record);
			stopRecording();
			//_recorderTask.cancel(true);
		}
		_recordMode = !_recordMode;
	}
	
	public void onPlayClick(View view)
	{
		File file = new File(this.getFilesDir(), "test.3gp");
		_player = new MediaPlayer();
		if (!_playMode)
		{
	        try {
	        	_player.setDataSource(file.getAbsolutePath());
	        	_player.prepare();
	        	_player.start();
	        	_buttonPlay.setText(R.string.stop);
	        } catch (IOException e) {
	            //Log.e(LOG_TAG, "prepare() failed");
	        }
		}
		else
		{
			_player.release();
			_player = null;
			_buttonPlay.setText(R.string.play);
		}
		_playMode = !_playMode;
	}
	
    private void startRecording(File file) {
    	_recorder = new MediaRecorder();
    	_recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    	_recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    	_recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    	_recorder.setOutputFile(file.getAbsolutePath());

        try {
        	_recorder.prepare();
        } catch (IOException e) {
            //Log.e(LOG_TAG, "prepare() failed");
        }

        _recorder.start();
    }
    
    private void stopRecording()
    {
    	_recorder.stop();
    	_recorder.release();
    	_recorder = null;
    }
}
