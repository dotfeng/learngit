package com.shijia.zzbus.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.shijia.zzbus.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 弹出对话框的适配器
 * 
 * @author ngh
 */

public class ListViewAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<HashMap<String, String>> line = new ArrayList<HashMap<String, String>>();

	public ListViewAdapter(Context context, ArrayList<HashMap<String, String>> line) {
		super();
		this.context = context;
		this.line = line;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return line.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v==null){
			v = LayoutInflater.from(context).inflate(R.layout.item, null);
		}
		HashMap<String,String> m = line.get(position);
		TextView tv = (TextView) v.findViewById(R.id.line_name);
		tv.setText(m.get("name"));
		
		return v;
	}

}
