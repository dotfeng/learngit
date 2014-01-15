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
 * �������ӹ�����
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
	 * ������·��ѯ����
	 * 
	 * @param value
	 *            ��Ҫ���ݵĲ���
	 * @return ���ܵ��Ĳ���
	 * @throws UnsupportedEncodingException  ת�����
	 */
	public static String clientHttpGet(String value) throws UnsupportedEncodingException {
		if((value.substring(0, 1)).matches("[0-9]")){
			System.out.println("��������");
		}
		String newUrl = "http://wap.zhengzhoubus.com/gps.asp?xl="+value+"&Submit=%E6%9F%A5%E8%AF%A2";

		// ��������
		HttpGet get = new HttpGet(newUrl);
		try {
			HttpResponse res = new DefaultHttpClient().execute(get);
			// �������� --------200 �ɹ� ------404 �Ҳ���ҳ��----- 500 �ڲ�ҳ��������
			Log.i("tag", "�������룺" + res.getStatusLine().getStatusCode());
			if (res.getStatusLine().getStatusCode() == 200) {
//				str = EntityUtils.toString(res.getEntity());
				str = EntityUtils.toString(res.getEntity(),"UTF-8");
				Log.i("tag", "�������ҳ����" +str);
			}
		} catch (Exception e) {
			Log.i("tag", "IOException ����:" + e.getMessage());
		}
		return str;
	}

	/**
	 * ������·��ѯ����
	 * 
	 * @param value
	 *            ��Ҫ���ݵĲ���
	 * @return ���ܵ��Ĳ���
	 * @throws UnsupportedEncodingException  ת�����
	 */
	public static String clientGet(String value) {
		
		String newUrl = "http://wap.zhengzhoubus.com/"+value;

		// ��������
		HttpGet get = new HttpGet(newUrl);
		try {
			HttpResponse res = new DefaultHttpClient().execute(get);
			// �������� --------200 �ɹ� ------404 �Ҳ���ҳ��----- 500 �ڲ�ҳ��������
			Log.i("tag", "�������룺" + res.getStatusLine().getStatusCode());
			if (res.getStatusLine().getStatusCode() == 200) {
//				str = EntityUtils.toString(res.getEntity());
				str = EntityUtils.toString(res.getEntity(),"UTF-8");
				Log.i("tag", "�������ҳ����" +str);
			}
		} catch (Exception e) {
			Log.i("tag", "IOException ����:" + e.getMessage());
		}
		return str;
	}

	
	
	
	
	
	
	
	
	/**
	 * HttpGet ��ʽ���� �������ķ��ʷ�ʽ
	 * 
	 * @param value
	 *            ��Ҫ���ݵĲ���
	 * @return ���ܵ��Ĳ���
	 */
	public static String clientGet(Map<String, String> value) {
		
		String newUrl = url_path + "?"; // ����urlͷ http://192.168.2.103:8080/web/login?
		// ����url β���������� http://192.168.2.103:8080/web/login?name=admin&pass=admin
		for (String key : value.keySet()) {
			newUrl += key + "=" + value.get(key) + "&";
			// newUrl= newUrl+key+"="+value.get(key)+"&";
		}
		
		// ��������
		HttpGet get = new HttpGet(newUrl);
		try {
			HttpResponse res = new DefaultHttpClient().execute(get);
			// �������� --------200 �ɹ� ------404 �Ҳ���ҳ��----- 500 �ڲ�ҳ��������
			Log.i("tag", "�������룺" + res.getStatusLine().getStatusCode());
			if (res.getStatusLine().getStatusCode() == 200) {
				str = EntityUtils.toString(res.getEntity());
			}
		} catch (ClientProtocolException e) {
			Log.i("tag", "ClientProtocolException ����:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("tag", "IOException ����:" + e.getMessage());
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * HttpGet ��ʽ���� ���������ķ�ʽ����
	 * 
	 * @param url
	 *            ��Ҫ���ʵ�action
	 * 
	 * @return ���ܵ��Ĳ���
	 */
	public static String clientGet1(String url) {

		// ��������
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse res = new DefaultHttpClient().execute(get);
			// �������� --------200 �ɹ� ------404 �Ҳ���ҳ��----- 500 �ڲ�ҳ��������
			Log.i("tag", "�������룺" + res.getStatusLine().getStatusCode());
			if (res.getStatusLine().getStatusCode() == 200) {
				str = EntityUtils.toString(res.getEntity());

			}
		} catch (ClientProtocolException e) {
			Log.i("tag", "ClientProtocolException ����:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("tag", "IOException ����:" + e.getMessage());
			e.printStackTrace();
		} 
		return str;
	}

	/**
	 * HttpPost ��ʽ�������� �������ķ��ʷ�ʽ
	 * 
	 * @param value
	 *            ����
	 * @param url
	 *            �����ַ
	 * @return ������ json
	 */

	public static String clientHttpPost(Map<String, String> value, String url) {
		Log.i("tag", "post url ��ַ:" + url);
		HttpPost post = new HttpPost(url);

		// ����post ��ʽ��url��ַ
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		for (String key : value.keySet()) {
			list.add(new BasicNameValuePair(key, value.get(key)));
			Log.i("tag", "post ����:" + key + "-------" + value.get(key));
		}
		try {
			post.setEntity(new UrlEncodedFormEntity(list, "utf-8")); // ���ô������ݵ��ַ�����ʽ
			HttpResponse res = new DefaultHttpClient().execute(post);
			Log.i("tag", "�������룺" + res.getStatusLine().getStatusCode());
			if (res.getStatusLine().getStatusCode() == 200) {
				str = EntityUtils.toString(res.getEntity());
			}
		} catch (UnsupportedEncodingException e) {
			Log.i("tag", "UnsupportedEncodingException ����:" + e.getMessage());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Log.i("tag", "ClientProtocolException ����:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("tag", "IOException ����:" + e.getMessage());
			e.printStackTrace();
		}
		return str;
	}

}
