package bjtu.group6.droidcorder.service;

import java.util.ArrayList;

import bjtu.group6.droidcorder.R;
import bjtu.group6.droidcorder.model.AudioFileInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AudioListAdapter extends BaseAdapter {
	private ArrayList<AudioFileInfo> audioFiles;
	private LayoutInflater mInflater;
	
	public AudioListAdapter(ArrayList<AudioFileInfo> audioFiles, Context context) {
		super();
		this.audioFiles = audioFiles;
		this.mInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return audioFiles.size();
	}
	@Override
	public Object getItem(int position) {
		return audioFiles.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			
			holder=new ViewHolder();  
			
			convertView = mInflater.inflate(R.layout.list_audio, null);
			holder.fileName = (TextView)convertView.findViewById(R.id.fileName);
			holder.duration = (TextView)convertView.findViewById(R.id.audioDuration);
			holder.createTiem = (TextView)convertView.findViewById(R.id.createTime);
			holder.position=position;
			convertView.setTag(holder);
			
		}else {
			
			holder = (ViewHolder)convertView.getTag();
		}
		
		String duration = audioFiles.get(position).getDuration();
		holder.fileName.setText(audioFiles.get(position).getFileName());
		holder.duration.setText(duration);
		holder.createTiem.setText(audioFiles.get(position).getCreateTime());
		return convertView;
	}
	public final class ViewHolder{
		public TextView fileName;
		public TextView duration;
		public TextView createTiem;
		public int position;
	}
}
