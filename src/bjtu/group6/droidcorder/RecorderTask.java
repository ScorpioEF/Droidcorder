package bjtu.group6.droidcorder;

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
    }
    
    protected void onCancelled() {

	}
}
