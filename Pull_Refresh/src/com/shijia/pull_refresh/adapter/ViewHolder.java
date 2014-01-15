package com.shijia.pull_refresh.adapter;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * 实体内容，ListView 中的组件
 * @author ngh
 */
public class ViewHolder {
	private ImageView mImage;
	private TextView mName;
	private TextView mVer;
	private TextView mSize;
	public ImageView getmImage() {
		return mImage;
	}
	public void setmImage(ImageView mImage) {
		this.mImage = mImage;
	}
	public TextView getmName() {
		return mName;
	}
	public void setmName(TextView mName) {
		this.mName = mName;
	}
	public TextView getmVer() {
		return mVer;
	}
	public void setmVer(TextView mVer) {
		this.mVer = mVer;
	}
	public TextView getmSize() {
		return mSize;
	}
	public void setmSize(TextView mSize) {
		this.mSize = mSize;
	}
}