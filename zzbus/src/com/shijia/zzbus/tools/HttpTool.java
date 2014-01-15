package com.shijia.zzbus.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * �������ӹ���
 * 
 * @author ngh
 */
public class HttpTool {

	// �ж������Ƿ�����
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info == null) {
				return false;
			} else {
				if (info.isAvailable()) {
					return true;
				}
			}
		}
		return false;
	}

	// �ж�������������
	// ���������Ƿ����
}
