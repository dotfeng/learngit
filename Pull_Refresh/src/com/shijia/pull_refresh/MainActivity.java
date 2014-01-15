package com.shijia.pull_refresh;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.shijia.pull_refresh.R;
import com.shijia.pull_refresh.adapter.MyListAdapter;
import com.shijia.pull_refresh.model.AppInfo;
import com.shijia.pull_refresh.view.SingleLayoutListView;
import com.shijia.pull_refresh.view.SingleLayoutListView.OnLoadMoreListener;
import com.shijia.pull_refresh.view.SingleLayoutListView.OnRefreshListener;

public class MainActivity extends Activity {
	private static final String TAG = "SingleLayoutActivity";

	private static final int LOAD_DATA_FINISH = 10;
	private static final int REFRESH_DATA_FINISH = 11;

	private List<AppInfo> mList = new ArrayList<AppInfo>();
	private MyListAdapter mAdapter;
	private SingleLayoutListView mListView;
	private int mCount = 10;

	private Handler mHandler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_DATA_FINISH:
				if (mAdapter != null) {
					mAdapter.mList = (ArrayList<AppInfo>) msg.obj;
					mAdapter.notifyDataSetChanged();
				}
				mListView.onRefreshComplete(); // 下拉刷新完成
				break;
			case LOAD_DATA_FINISH:
				if (mAdapter != null) {
					mAdapter.mList.addAll((ArrayList<AppInfo>) msg.obj);
					mAdapter.notifyDataSetChanged();
				}
				mListView.onLoadMoreComplete(); // 加载更多完成
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);

		setTitle(getTitle());
		setContentView(R.layout.single_layout_activity);

		buildAppData();
		initView();
	}

	private void initView() {
 		mAdapter = new MyListAdapter(this, mList);
		mListView = (SingleLayoutListView) findViewById(R.id.mListView);
		mListView.setAdapter(mAdapter);

		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO 下拉刷新
				Log.e(TAG, "onRefresh-----------------------");
				loadData(0);
			}
		});

		mListView.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				// TODO 加载更多
				Log.e(TAG, "onLoad=========");
				loadData(1);
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 此处传回来的position和mAdapter.getItemId()获取的一致;
				Log.e(TAG, "click position:" + position);
				// Log.e(TAG,
				// "__ mAdapter.getItemId() = "+mAdapter.getItemId(position));
			}
		});

		// mCanPullRefBtn = (Button) findViewById(R.id.canPullRefBtn);
		// mCanLoadMoreBtn = (Button) findViewById(R.id.canLoadMoreFlagBtn);
		// mCanAutoLoadMoreBtn = (Button) findViewById(R.id.autoLoadMoreFlagBtn);
		// mIsMoveToFirstItemBtn = (Button) findViewById(R.id.isMoveToFirstItemBtn);
		// mIsDoRefreshOnUIChanged = (Button) findViewById(R.id.mIsDoRefreshOnWindowFocused);
		//
		// mCanPullRefBtn.setOnClickListener(this);
		// mCanLoadMoreBtn.setOnClickListener(this);
		// mCanAutoLoadMoreBtn.setOnClickListener(this);
		// mIsMoveToFirstItemBtn.setOnClickListener(this);
		// mIsDoRefreshOnUIChanged.setOnClickListener(this);

		mListView.setCanLoadMore(true);
		mListView.setCanRefresh(true);
		mListView.setAutoLoadMore(true);
		mListView.setMoveToFirstItemAfterRefresh(true);
		mListView.setDoRefreshOnUIChanged(true);

	}

	/**
	 * 加载数据啦~
	 * 
	 * @param type  0  下拉刷新 ，1是上拉加载更多
	 * @date 2013-12-13 上午10:14:08
	 * @author JohnWatson
	 * @version 1.0
	 */
	public void loadData(final int type) {
		new Thread() {
			@Override
			public void run() {
				List<AppInfo> _List = null;
				switch (type) {
				case 0:
					mCount = 10;

					_List = new ArrayList<AppInfo>();
					for (int i = 1; i <= mCount; i++) {
						AppInfo ai = new AppInfo();

						ai.setAppIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
						ai.setAppName("应用Demo_" + i);
						ai.setAppVer("版本: " + (i % 10 + 1) + "." + (i % 8 + 2) + "." + (i % 6 + 3));
						ai.setAppSize("大小: " + i * 10 + "MB");

						_List.add(ai);
					}
					break;

				case 1:
					_List = new ArrayList<AppInfo>();
					int _Index = mCount + 10;

					for (int i = mCount + 1; i <= _Index; i++) {
						AppInfo ai = new AppInfo();

						ai.setAppIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
						ai.setAppName("应用Demo_" + i);
						ai.setAppVer("版本: " + (i % 10 + 1) + "." + (i % 8 + 2) + "." + (i % 6 + 3));
						ai.setAppSize("大小: " + i * 10 + "MB");

						_List.add(ai);
					}
					mCount = _Index;
					break;
				}

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (type == 0) { // 下拉刷新
					// Collections.reverse(mList); //逆序
					Message _Msg = mHandler.obtainMessage(REFRESH_DATA_FINISH, _List);
					mHandler.sendMessage(_Msg);
				} else if (type == 1) {
					Message _Msg = mHandler.obtainMessage(LOAD_DATA_FINISH, _List);
					mHandler.sendMessage(_Msg);
				}
			}
		}.start();
	}

	private void buildAppData() {
		for (int i = 1; i <= 10; i++) {
			AppInfo ai = new AppInfo();

			ai.setAppIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
			ai.setAppName("应用Demo_" + i);
			ai.setAppVer("版本: " + (i % 10 + 1) + "." + (i % 8 + 2) + "." + (i % 6 + 3));
			ai.setAppSize("大小: " + i * 10 + "MB");

			mList.add(ai);
		}
	}

}
