package bjtu.group6.droidcorder.service;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.AsyncTask;

public class RecorderTask extends AsyncTask<File, Void, Void>{
	private MediaRecorder _recorder = null;

	@Override
	protected Void doInBackground(File... params) {
		startRecording(params[0]);
		return null;
	}
	
    protected void onCancelled() {
    	stopRecording();
	}
    
    protected void onPostExecute() {
    	stopRecording();
    }
    
    private void startRecording(File file) {
    	_recorder = new MediaRecorder();
    	_recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    	_recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    	_recorder.setOutputFile(file.getAbsolutePath());
    	_recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    	
        try {
        	_recorder.prepare();
        } catch (IOException e) {
            //Log.e(LOG_TAG, "prepare() failed");
        }
        _recorder.start();
        
        try {
        	while (true)
        	{
        		Thread.sleep(100, 0);
        		if (isCancelled())
        			break;
        	}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	private void stopRecording() {
		_recorder.stop();
		_recorder.release();
		_recorder = null;
	}
}
