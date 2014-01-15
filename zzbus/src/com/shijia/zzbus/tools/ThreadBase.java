package com.shijia.zzbus.tools;

import java.io.UnsupportedEncodingException;

import com.shijia.zzbus.MainActivity;

import android.os.Handler;
import android.os.Message;

public class ThreadBase extends Thread{

	private Handler handler;
	private int state ;
	private String url ;
	public ThreadBase(int state, String url,Handler handler) {
		super();
		this.state = state;
		this.url = url;
		this.handler = handler;
	}
	@Override
	public void run() {
		if(state==1){
			String res = HttpUtils.clientGet(url);
			
			if (res != null) {
				Message msg = handler.obtainMessage(2, res);
				handler.sendMessage(msg);
			} 
		}else{
			try {
				String res = HttpUtils.clientHttpGet(url);
				if (res != null) {
					Message msg = handler.obtainMessage(1, res);
					handler.sendMessage(msg);
				} 
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	

}
