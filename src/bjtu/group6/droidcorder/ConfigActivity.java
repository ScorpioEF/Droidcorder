package bjtu.group6.droidcorder;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class ConfigActivity extends Activity {	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
        setContentView(R.layout.activity_setting);
	}
	
    public void onFormatSettingClick(View view){
    	Intent intent = new Intent();
    	intent.setClass(ConfigActivity.this, FormatDetailActivity.class);
    	startActivity(intent);
    	ConfigActivity.this.finish();
    }
    
    public void onReturnRecorderClick(View view){
    	Intent intent = new Intent();
    	intent.setClass(ConfigActivity.this, RecorderActivity.class);
    	startActivity(intent);
    	ConfigActivity.this.finish();
    }
      
}
