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
	private String[]arrayDuration = {"30 mins", "1 hour", "4 hours", "no limit"};
	private int []durationInfo = { 30, 60, 240, max};
	private int selectedDurationIndex;
	private int defaultIndex = 0;
	private String _path = "/Droidcorder";
	private String strAbout = "";
	
	private String[] formatInfo = new String[] { "AAC_ADTS", "AMR_NB", "MPEG_4", "THREE_GPP"}; 
	private String[] formatInfoValue = new String[] { ".acc", ".amr", ".mp4", ".3gp"};
	private int selectedFormatIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		updateAllSetting();
		setContentView(R.layout.activity_setting);
	}
	
	public void onFormatDetialClick(View view){   	 
		Dialog formatDetailDialog = new AlertDialog.Builder(this).   
				setTitle("Default Sound Format")   
				.setIcon(R.drawable.ic_launcher)   
				.setSingleChoiceItems(R.array.format_string, selectedFormatIndex ,new DialogInterface.OnClickListener() {  
					@Override   
					public void onClick(DialogInterface dialog, int which) {     
						selectedFormatIndex = which;
						saveFormatSetting();
					}   
				}). 
				setPositiveButton("OK", new DialogInterface.OnClickListener() {              	   
					@Override   
					public void onClick(DialogInterface dialog, int which) {   
						saveFormatSetting();
					}   
				}).                                    
				create();   
		formatDetailDialog.show();   
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
		this.finish();
	}

	private void updateAllSetting() {
		Context ctx = ConfigActivity.this; 
		SharedPreferences setting_preference = ctx.getSharedPreferences("setting_preference", MODE_PRIVATE);
		selectedDurationIndex = setting_preference.getInt("DURATION_INDEX_KEY", defaultIndex);
		selectedFormatIndex = setting_preference.getInt("INDEX_KEY", defaultIndex);
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
	
	private void saveFormatSetting() {
    	Context ctx = ConfigActivity.this; 
		SharedPreferences setting_preference = ctx.getSharedPreferences("setting_preference", MODE_PRIVATE);
		
		Editor editor = setting_preference.edit();
		editor.putInt("INDEX_KEY", selectedFormatIndex);
		editor.putString("FORMAT_KEY", formatInfo[selectedFormatIndex]);
		editor.putString("POSTFIX_KEY", formatInfoValue[selectedFormatIndex]);
		editor.commit();		
	}

}
