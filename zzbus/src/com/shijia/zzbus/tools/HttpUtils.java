package com.shijia.zzbus.tools;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.shijia.zzbus.api.InterAPI;

import android.content.Entity;
import android.util.Log;


/**
 * 网络连接工具类
 * 
 * @author ngh
 * 
 */
public class HttpUtils {
	private static String url_path;
	private static String str = "";

	public HttpUtils() {
	};

	public HttpUtils(String url) {
		super();
		this.url_path = InterAPI.URL_PATH + url;
	}

	/**
	 * 根据线路查询数据
	 * 
	 * @param value
	 *            需要传递的参数
	 * @return 接受到的参数
	 * @throws UnsupportedEncodingException  转码错误
	 */
	public static String clientHttpGet(String value) throws UnsupportedEncodingException {
		if((value.substring(0, 1)).matches("[0-9]")){
			System.out.println("这是数字");
		}
		String newUrl = "http://wap.zhengzhoubus.com/gps.asp?xl="+value+"&Submit=%E6%9F%A5%E8%AF%A2";

		// 网络连接
		HttpGet get = new HttpGet(newUrl);
		try {
			HttpResponse res = new DefaultHttpClient().execute(get);
			// 请求结果码 --------200 成功 ------404 找不到页面----- 500 内部页面编译错误
			Log.i("tag", "请求结果码：" + res.getStatusLine().getStatusCode());
			if (res.getStatusLine().getStatusCode() == 200) {
//				str = EntityUtils.toString(res.getEntity());
				str = EntityUtils.toString(res.getEntity(),"UTF-8");
				Log.i("tag", "请求的网页内容" +str);
			}
		} catch (Exception e) {
			Log.i("tag", "IOException 错误:" + e.getMessage());
		}
		return str;
	}

	/**
	 * 根据线路查询数据
	 * 
	 * @param value
	 *            需要传递的参数
	 * @return 接受到的参数
	 * @throws UnsupportedEncodingException  转码错误
	 */
	public static String clientGet(String value) {
		
		String newUrl = "http://wap.zhengzhoubus.com/"+value;

		// 网络连接
		HttpGet get = new HttpGet(newUrl);
		try {
			HttpResponse res = new DefaultHttpClient().execute(get);
			// 请求结果码 --------200 成功 ------404 找不到页面----- 500 内部页面编译错误
			Log.i("tag", "请求结果码：" + res.getStatusLine().getStatusCode());
			if (res.getStatusLine().getStatusCode() == 200) {
//				str = EntityUtils.toString(res.getEntity());
				str = EntityUtils.toString(res.getEntity(),"UTF-8");
				Log.i("tag", "请求的网页内容" +str);
			}
		} catch (Exception e) {
			Log.i("tag", "IOException 错误:" + e.getMessage());
		}
		return str;
	}

	
	
	
	
	
	
	
	
	/**
	 * HttpGet 方式连接 带参数的访问方式
	 * 
	 * @param value
	 *            需要传递的参数
	 * @return 接受到的参数
	 */
	public static String clientGet(Map<String, String> value) {
		
		String newUrl = url_path + "?"; // 构建url头 http://192.168.2.103:8080/web/login?
		// 构建url 尾部参数内容 http://192.168.2.103:8080/web/login?name=admin&pass=admin
		for (String key : value.keySet()) {
			newUrl += key + "=" + value.get(key) + "&";
			// newUrl= newUrl+key+"="+value.get(key)+"&";
		}
		
		// 网络连接
		HttpGet get = new HttpGet(newUrl);
		try {
			HttpResponse res = new DefaultHttpClient().execute(get);
			// 请求结果码 --------200 成功 ------404 找不到页面----- 500 内部页面编译错误
			Log.i("tag", "请求结果码：" + res.getStatusLine().getStatusCode());
			if (res.getStatusLine().getStatusCode() == 200) {
				str = EntityUtils.toString(res.getEntity());
			}
		} catch (ClientProtocolException e) {
			Log.i("tag", "ClientProtocolException 错误:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("tag", "IOException 错误:" + e.getMessage());
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * HttpGet 方式连接 不带参数的方式访问
	 * 
	 * @param url
	 *            需要访问的action
	 * 
	 * @return 接受到的参数
	 */
	public static String clientGet1(String url) {

		// 网络连接
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse res = new DefaultHttpClient().execute(get);
			// 请求结果码 --------200 成功 ------404 找不到页面----- 500 内部页面编译错误
			Log.i("tag", "请求结果码：" + res.getStatusLine().getStatusCode());
			if (res.getStatusLine().getStatusCode() == 200) {
				str = EntityUtils.toString(res.getEntity());

			}
		} catch (ClientProtocolException e) {
			Log.i("tag", "ClientProtocolException 错误:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("tag", "IOException 错误:" + e.getMessage());
			e.printStackTrace();
		} 
		return str;
	}

	/**
	 * HttpPost 方式网络连接 带参数的访问方式
	 * 
	 * @param value
	 *            参数
	 * @param url
	 *            网络地址
	 * @return 请求结果 json
	 */

	public static String clientHttpPost(Map<String, String> value, String url) {
		Log.i("tag", "post url 地址:" + url);
		HttpPost post = new HttpPost(url);

		// 构建post 格式的url地址
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		for (String key : value.keySet()) {
			list.add(new BasicNameValuePair(key, value.get(key)));
			Log.i("tag", "post 数据:" + key + "-------" + value.get(key));
		}
		try {
			post.setEntity(new UrlEncodedFormEntity(list, "utf-8")); // 设置传递数据的字符集格式
			HttpResponse res = new DefaultHttpClient().execute(post);
			Log.i("tag", "请求结果码：" + res.getStatusLine().getStatusCode());
			if (res.getStatusLine().getStatusCode() == 200) {
				str = EntityUtils.toString(res.getEntity());
			}
		} catch (UnsupportedEncodingException e) {
			Log.i("tag", "UnsupportedEncodingException 错误:" + e.getMessage());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Log.i("tag", "ClientProtocolException 错误:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("tag", "IOException 错误:" + e.getMessage());
			e.printStackTrace();
		}
		return str;
	}

}
