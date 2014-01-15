package com.shijia.zzbus.other;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.shijia.zzbus.MainActivity;
import com.shijia.zzbus.R;
import com.shijia.zzbus.R.layout;
import com.shijia.zzbus.tools.HttpUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * 候车站点信息
 * 
 * @author ngh
 */
public class Car_info extends Activity implements View.OnClickListener {
	private TextView tv2;
	private TextView tv4;
	private TextView tv6;
	private TextView tv7;
	private TextView tv8;
	private TextView tv9;
	private Button back_home;
	private Button btn_ref;

	private String line_hczd;
	private ArrayList<String> line_list;
	
	private ProgressDialog pd;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			pd.dismiss();
			line_list = new ArrayList<String>();
			Document result = Jsoup.parse(msg.obj.toString());
			Elements br = result.select("br");
			line_list.add(br.get(1).nextSibling().toString());
			line_list.add(br.get(2).nextSibling().toString());
			line_list.add(br.get(4).nextSibling().toString());
			line_list.add(br.get(5).nextSibling().toString());
			line_list.add(br.get(6).nextSibling().toString());
			
			setTextView();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_item_info);
		initUI();
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		line_list = new ArrayList<String>();
		line_list = (ArrayList<String>) bundle.get("line_info");
		line_hczd = (String) bundle.get("line_hczd");
		setTextView();
		back_home.setOnClickListener(this);
		btn_ref.setOnClickListener(this);
	}

	/**
	 * 设置候车站点还有几站
	 */
	private void setTextView() {
		tv2.setText(line_list.get(0).substring(line_list.get(0).indexOf("：") + 1, line_list.get(0).length()));
		tv4.setText(line_list.get(1).substring(line_list.get(1).indexOf("：") + 1, line_list.get(1).length()));
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datatime = sf.format(new Date());
		tv9.setText("查询时间：" + datatime);

		if (line_list.get(2) != null && !line_list.get(2).equals("<br />")) {
			tv6.setText(line_list.get(2));
			if (line_list.get(3) != null && !line_list.get(3).equals("<br />")) {
				tv7.setVisibility(View.VISIBLE);
				tv7.setText(line_list.get(3));
				if (line_list.get(4) != null && !line_list.get(4).equals("<br />")) {
					tv8.setVisibility(View.VISIBLE);
					tv8.setText(line_list.get(4));

				}
			}

		} else {
			tv6.setText("为查询到车辆信息！");
		}
	}

	/**
	 * 获取候车站点
	 */
	private void setLine_list() {
	}

	/**
	 * 初始化UI组件
	 */
	private void initUI() {
		tv2 = (TextView) findViewById(R.id.tv2);
		tv4 = (TextView) findViewById(R.id.tv4);
		tv6 = (TextView) findViewById(R.id.tv6);
		tv7 = (TextView) findViewById(R.id.tv7);
		tv8 = (TextView) findViewById(R.id.tv8);
		tv9 = (TextView) findViewById(R.id.tv9);
		back_home = (Button) findViewById(R.id.back_home);
		btn_ref = (Button) findViewById(R.id.ref);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_home:
			finish();
			break;
		case R.id.ref:
			pd = ProgressDialog.show(Car_info.this, "加载数据", "正在加载 loading....");
			// 访问网络获取线路信息
			new Thread(new Runnable() {
				@Override
				public void run() {
					String res = HttpUtils.clientGet(line_hczd);
					System.out.println(res + "------****这是候车界面的信息");
					Message msg = handler.obtainMessage(1, res);
					handler.sendMessage(msg);

				}
			}).start();
			break;

		default:
			break;
		}

	}

}
