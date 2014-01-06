package bjtu.group6.droidcorder;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.ListView;

public class FormatDetailActivity extends ListActivity {

	private String[] formatInfo = new String[] { "AAC_ADTS", "AMR_NB", "MPEG_4", "THREE_GPP"}; 
	private String[] formatInfoValue = new String[] { ".acc", ".amr", ".mp4", ".3gp"};
	private int defaultIndex = 0;
	private int selectedFormatIndex;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		updateSetting();
		setContentView(R.layout.activity_setting_detail);	
		ListView listView = this.getListView();
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		selectedFormatIndex = position;
		saveSetting();
		super.onListItemClick(l, v, position, id);
	}

	public void onReturnClick(View view){
		Intent intent = new Intent();
		intent.setClass(FormatDetailActivity.this, ConfigActivity.class);
		startActivity(intent);
		FormatDetailActivity.this.finish();
	}

	public void onConfirmClick(View view){
		saveSetting();
		Intent intent = new Intent();
		intent.setClass(FormatDetailActivity.this, ConfigActivity.class);
		startActivity(intent);
		FormatDetailActivity.this.finish();
	}

	private void updateSetting() {
		Context ctx = FormatDetailActivity.this; 
		SharedPreferences setting_preference = ctx.getSharedPreferences("setting_preference", MODE_PRIVATE);
		selectedFormatIndex = setting_preference.getInt("INDEX_KEY", defaultIndex);
	}

	private void saveSetting() {
    	Context ctx = FormatDetailActivity.this; 
		SharedPreferences setting_preference = ctx.getSharedPreferences("setting_preference", MODE_PRIVATE);
		
		Editor editor = setting_preference.edit();
		editor.putInt("INDEX_KEY", selectedFormatIndex);
		editor.putString("FORMAT_KEY", formatInfo[selectedFormatIndex]);
		editor.putString("POSTFIX_KEY", formatInfoValue[selectedFormatIndex]);
		editor.commit();		
	}
}
