package com.shijia.pull_refresh.adapter;

import java.util.ArrayList;
import java.util.List;

import com.shijia.pull_refresh.R;
import com.shijia.pull_refresh.model.AppInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ListView 适配器
 * 
 * @author ngh
 */
public class MyListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	public List<com.shijia.pull_refresh.model.AppInfo> mList;

	public MyListAdapter(Context pContext, List<AppInfo> pList) {
		mInflater = LayoutInflater.from(pContext);
		if (pList != null) {
			mList = pList;
		} else {
			mList = new ArrayList<AppInfo>();
		}
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// System.out.println("getItemId = " + position);
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (getCount() == 0) {
			return null;
		}
		// System.out.println("position = "+position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, null);

			holder = new ViewHolder();
			holder.setmImage((ImageView) convertView.findViewById(R.id.ivIcon));
			holder.setmName((TextView) convertView.findViewById(R.id.tvName));
			holder.setmVer((TextView) convertView.findViewById(R.id.tvVer));
			holder.setmSize((TextView) convertView.findViewById(R.id.tvSize));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		AppInfo ai = mList.get(position);
		holder.getmImage().setImageBitmap(ai.getAppIcon());
		holder.getmName().setText(ai.getAppName());
		holder.getmVer().setText(ai.getAppVer());
		holder.getmSize().setText(ai.getAppSize());

		return convertView;
	}
}
