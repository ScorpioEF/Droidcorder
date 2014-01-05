package bjtu.group6.droidcorder;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;


public class ConfigActivity extends Activity {	
	private int max = 2147483647;
	private String[]arrayDuration = {"30 mins", "1h", "4 hs", "no limit"};
	private int []durationInfo = { 30, 60, 240, max};
	private int selectedDurationIndex;
	private int defaultIndex = 0;
	private String _path = "/Droidcorder";
	private String strAbout = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		updateAllSetting();
        setContentView(R.layout.activity_setting);
	}
	
	public void onMaxDurationClick(View view){   	 
        Dialog durationSettingDialog = new AlertDialog.Builder(this).   
                setTitle("Set Max Duration")   
                .setIcon(R.drawable.ic_launcher)   
                .setSingleChoiceItems(arrayDuration, selectedDurationIndex ,new DialogInterface.OnClickListener() {  
                    @Override   
                    public void onClick(DialogInterface dialog, int which) {     
                        selectedDurationIndex = which;
                        saveDurationSetting();
                    }   
                }). 
                setPositiveButton("OK", new DialogInterface.OnClickListener() {              	   
                    @Override   
                    public void onClick(DialogInterface dialog, int which) {   
                    	saveDurationSetting();
                    }   
                }).                                    
                create();   
        durationSettingDialog.show();   
    } 
	
	public void onDefaultPathClick(View view){ 
		
		String path = Environment.getExternalStoragePublicDirectory(_path).getPath();
		
        Dialog durationSettingDialog = new AlertDialog.Builder(this).   
                setTitle("Default Path")   
                .setIcon(R.drawable.ic_launcher)   
                .setMessage(path)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {	
					}}).                                    
                create();   
        durationSettingDialog.show();   
    } 
	
    public void onFormatSettingClick(View view){
    	Intent intent = new Intent();
    	intent.setClass(ConfigActivity.this, FormatDetailActivity.class);
    	startActivity(intent);
    	ConfigActivity.this.finish();
    }
    public void onAboutUsClick(View view){ 
		Dialog aboutUsDialog = new AlertDialog.Builder(this).   
                setTitle("Default Path")   
                .setIcon(R.drawable.ic_launcher)   
                .setMessage(strAbout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {	
					}}).                                    
                create();   
		aboutUsDialog.show();   
    }
    public void onReturnRecorderClick(View view){
    	Intent intent = new Intent();
    	intent.setClass(ConfigActivity.this, RecorderActivity.class);
    	startActivity(intent);
    	ConfigActivity.this.finish();
    }
    
    private void updateAllSetting() {
    	Context ctx = ConfigActivity.this; 
		SharedPreferences setting_preference = ctx.getSharedPreferences("setting_preference", MODE_PRIVATE);
		selectedDurationIndex = setting_preference.getInt("DURATION_INDEX_KEY", defaultIndex);
		strAbout = setting_preference.getString("ABOUT_KEY", "Droidcorder 1.0.0");
	}
    
    private void saveDurationSetting() {
    	Context ctx = ConfigActivity.this; 
		SharedPreferences setting_preference = ctx.getSharedPreferences("setting_preference", MODE_PRIVATE);
		
		Editor editor = setting_preference.edit();
		editor.putInt("DURATION_INDEX_KEY", selectedDurationIndex);
		editor.putInt("DURATION_KEY", durationInfo[selectedDurationIndex]);
		editor.commit();
	}
      
}
