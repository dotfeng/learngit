package com.shijia.zzbus;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.shijia.zzbus.adapter.ListViewAdapter;
import com.shijia.zzbus.other.Car_info;
import com.shijia.zzbus.tools.HttpTool;
import com.shijia.zzbus.tools.HttpUtils;
import com.shijia.zzbus.tools.ThreadBase;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class MainActivity extends Activity implements android.view.View.OnClickListener {
	private EditText xianlu_num; // 输入的线路
	private Spinner s_x; // 上行还是下行
	private Spinner houche_str; // 候车地点
	private Button query_xianlu; // 查询线路的名称
	private String num; // 线路编号

	// 存放查找到线路的数据
	private ArrayList<HashMap<String, String>> resLine;
	private String[] data_line; // 上行 OR 下行
	private String[] data_line_xian; // 连接地址
	private Document result = null;

	private ArrayList<String> line_item;
	private ArrayList<String> line_item_xian;

	private String fx; // 车辆行驶的方向
	private String hczd; // 候车站点

	private String name; // 多条线路的显示名称
	private int max_line_num; // 选择的线路编号；

	private ProgressDialog pd;
	private String[] str = { "请输入线路名称！" };
	private ArrayAdapter<String> adapter;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			result = Jsoup.parse(msg.obj.toString());
			Elements br = result.select("br");
			pd.dismiss();
			if (msg.what == 1) {
				data_line = new String[3];
				data_line_xian = new String[3];

				s_x.setEnabled(true);
				// 解析html 网页内容
				if (br.size() >= 4) {
					Toast.makeText(MainActivity.this,"请选择车辆形式方向！", 0).show();
					String s = br.get(1).nextElementSibling().select("a").toString();
//					String code = br.get(1).nextSibling().toString();
//					Toast.makeText(MainActivity.this, s.length() + "--" + br.get(0).nextSibling().toString() + "--" + "请选择线路".equals(code) + "--" + br.get(1).nextSibling().toString(), 0).show();
					if (s.length() > 5) {
						data_line[0] = "请选择车辆形式方向";
						data_line[1] = br.get(1).nextSibling().toString() + "~" + br.get(1).nextElementSibling().select("a").html().toString();
						data_line[2] = br.get(2).nextSibling().toString() + "~" + br.get(2).nextElementSibling().select("a").html().toString();

						data_line_xian[0] = "";
						data_line_xian[1] = br.get(1).nextElementSibling().select("a").attr("href").toString();
						data_line_xian[2] = br.get(2).nextElementSibling().select("a").attr("href").toString();

						set_xs_line(); // 设置形式方向

					} else {
						sendMsg(br);
					}
				} else {
					Toast.makeText(MainActivity.this, "没有此线路信息,请重新输入！", 1).show();
				}

			} else if (msg.what == 2) {
				s_x.setEnabled(false);

				line_item = new ArrayList<String>();
				line_item_xian = new ArrayList<String>();

				line_item.add("请选择候车地点");
				line_item_xian.add("");

				for (int i = 1; i <= br.size() - 3; i++) {
					line_item.add(br.get(i).nextSibling().toString() + "-" + br.get(i).nextElementSibling().select("a").html().toString());
					line_item_xian.add(br.get(i).nextElementSibling().select("a").attr("href").toString());

				}
				set_line_item();
			} else if (msg.what == 3) { //

				s_x.setEnabled(false);
				data_line[0] = name;
				fx = num;
				set_xs_line(); // 设置形式方向
				new Thread(new Runnable() {

					@Override
					public void run() {
						String res = HttpUtils.clientGet(fx);
						if (res != null) {
							Message msg = handler.obtainMessage(2, res);
							handler.sendMessage(msg);
						} else {

						}

					}
				}).start();

			} else if (msg.what == 4) {
				s_x.setEnabled(true);
				ArrayList<String> list_line = new ArrayList<String>();
				list_line.add(br.get(1).nextSibling().toString());
				list_line.add(br.get(2).nextSibling().toString());
				list_line.add(br.get(4).nextSibling().toString());
				list_line.add(br.get(5).nextSibling().toString());
				list_line.add(br.get(6).nextSibling().toString());

				Intent intent = new Intent(MainActivity.this, Car_info.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("line_info", list_line);
				bundle.putSerializable("line_hczd", hczd);
				intent.putExtras(bundle);
				startActivityForResult(intent, 1);

			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_main); // 主界面
		initUI(); // 初始化UI组件
		if (HttpTool.isNetworkAvailable(MainActivity.this)) {
			Toast.makeText(MainActivity.this, "网络可用", 1).show();
			query_xianlu.setOnClickListener(this);
		} else {
			Toast.makeText(MainActivity.this, "网络不可用", 1).show();
		}
	}

	/**
	 * 设置车辆形式方向
	 */
	private void set_xs_line() {

		ArrayAdapter<String> s_x_ling = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, data_line);
		s_x_ling.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s_x.setAdapter(s_x_ling);
		s_x.setPrompt("请选择车辆行驶方向");
		s_x.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position != 0 && id != 0) {
					fx = data_line_xian[position];
					pd = ProgressDialog.show(MainActivity.this, "加载数据", "正在加载 loading....");
					// 访问网络获取线路信息
					new Thread(new Runnable() {

						@Override
						public void run() {
							String res = HttpUtils.clientGet(fx);
							if (res != null) {
								Message msg = handler.obtainMessage(2, res);
								handler.sendMessage(msg);
							} else {

							}

						}
					}).start();

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	/**
	 * 设置候车地点
	 */
	private void set_line_item() {
		ArrayAdapter<String> s_x_ling = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, line_item);
		s_x_ling.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		houche_str.setAdapter(s_x_ling);
		houche_str.setPrompt("请选择候车站点");
		houche_str.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position != 0 && id != 0) {
					hczd = line_item_xian.get(position);
					pd = ProgressDialog.show(MainActivity.this, "加载数据", "正在加载 loading....");
					// 访问网络获取线路信息
					new Thread(new Runnable() {
						@Override
						public void run() {
							String res = HttpUtils.clientGet(hczd);
							Message msg = handler.obtainMessage(4, res);
							handler.sendMessage(msg);
						}
					}).start();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	/**
	 * 初始化UI组件
	 */
	private void initUI() {
		xianlu_num = (EditText) findViewById(R.id.xianlu_num);
		s_x = (Spinner) findViewById(R.id.s_x);
		houche_str = (Spinner) findViewById(R.id.houche_str);
		query_xianlu = (Button) findViewById(R.id.query_xianlu);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.query_xianlu:
			num = xianlu_num.getText().toString();
			if (num.length() <= 0) {
				// 提示输入线路名称
				Toast.makeText(MainActivity.this, "请输入要乘坐的公交线路！", 1).show();
			} else {
				pd = ProgressDialog.show(MainActivity.this, "加载数据", "正在加载 loading....");

				// 访问网络获取线路信息
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							String res = HttpUtils.clientHttpGet(num);
							if (res != null) {
								Message msg = handler.obtainMessage(1, res);
								handler.sendMessage(msg);
							} else {

							}
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}).start();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 提示选择那条线路
	 * 
	 * @return 线路结果
	 */
	private void sendMsg(Elements br) {
		resLine = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = null;
		for (int i = 2; i <= br.size() - 2; i++) {
			map = new HashMap<String, String>();
			System.out.println(br.get(i).nextElementSibling().select("a").html().toString() + "****----****" + br.get(i).nextElementSibling().select("a").attr("href").toString());
			map.put("name", br.get(i).nextElementSibling().select("a").html().toString());
			map.put("line", br.get(i).nextElementSibling().select("a").attr("href").toString());
			resLine.add(map);
		}
		ListViewAdapter lva = new ListViewAdapter(MainActivity.this, resLine);
		new AlertDialog.Builder(this).setTitle("请选择要乘坐的线路").setAdapter(lva, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				max_line_num = which;
				num = resLine.get(which).get("line");
				name = resLine.get(which).get("name");
				xianlu_num.setText(name);
				System.out.println(max_line_num + "*****-----*****" + num + "----****" + name);
				pd = ProgressDialog.show(MainActivity.this, "加载数据", "正在加载 loading....");
				// 访问网络获取线路信息
				new Thread(new Runnable() {

					@Override
					public void run() {
						String res = HttpUtils.clientGet(num);

						result = Jsoup.parse(res.toString());
						Elements br = result.select("br");
						String code = br.get(1).nextSibling().toString();
						if ("上行. ".equals(code)) {
							Message msg = handler.obtainMessage(1, res);
							handler.sendMessage(msg);
						} else {
							Message msg = handler.obtainMessage(3, res);
							handler.sendMessage(msg);

						}
					}
				}).start();
			}
		}).show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, str);
		xianlu_num.setText("");
		s_x.setAdapter(adapter);
		houche_str.setAdapter(adapter);

	}

}
