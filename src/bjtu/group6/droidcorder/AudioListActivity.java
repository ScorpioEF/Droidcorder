package bjtu.group6.droidcorder;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import bjtu.group6.droidcorder.model.AudioFileInfo;
import bjtu.group6.droidcorder.service.AudioListAdapter;

public class AudioListActivity extends Activity {
	private ArrayList<AudioFileInfo> audioFiles = new ArrayList<AudioFileInfo>();
	private ListView audioList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_list);
		findViews();
		
		//For test
		AudioFileInfo audioinfo = new AudioFileInfo();
		audioinfo.setAudioId(111111);
		audioinfo.setFileName("Audio1");
		audioinfo.setDuration("10.50");
		audioinfo.setCreateTime("2014.1.1");
		AudioFileInfo audioinfo2 = new AudioFileInfo();
		audioinfo2.setAudioId(111222);
		audioinfo2.setFileName("Audio2");
		audioinfo2.setDuration("21.50");
		audioinfo2.setCreateTime("2014.1.2");
		audioFiles.add(audioinfo);
		audioFiles.add(audioinfo2);
		
		audioList.setAdapter(new AudioListAdapter(audioFiles, AudioListActivity.this));
		
		setListeners();
	}

	private void setListeners() {
		audioList.setOnItemClickListener(new OnItemClickListener(){ 
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) { 
	            AudioFileInfo audioInfo = (AudioFileInfo) audioList.getItemAtPosition(arg2); 
	            System.out.println(audioInfo.getFileName());
//	        	Intent intent = new Intent();
//    			intent.setClass(AudioListActivity.this, AudioInfoInfor.class);
//    			
//    			startActivity(intent);
	        } 
	         
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.audio_list, menu);
		return true;
	}

	private void findViews(){
		audioList=(ListView)this.findViewById(R.id.audioList);
    }
}
