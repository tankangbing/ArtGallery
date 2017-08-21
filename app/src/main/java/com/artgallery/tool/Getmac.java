package com.artgallery.tool;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Getmac {
	
	public Activity activity;
	
	
   
	public Getmac(Activity activity) {
		super();
		this.activity = activity;
	}



	public String getLocalMacAddress() {
	    WifiManager wifi = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);  
	    WifiInfo info = wifi.getConnectionInfo();  
	    String mac = info.getMacAddress();
	    mac= mac.replaceAll(":", "");
	    String mac2 = "aca213c81600";
	    return mac;  
	}


	public static String getLocalMacAddressFromBusybox(){
		String result = "";
		String Mac = "";
		result = callCmd("busybox ifconfig","HWaddr");

		//如果返回的result == null，则说明网络不可取
		if(result==null){
			return "网络出错，请检查网络";
		}

		//对该行数据进行解析
		//例如：eth0      Link encap:Ethernet  HWaddr 00:16:E8:3E:DF:67
		if(result.length()>0 && result.contains("HWaddr")==true){
			Mac = result.substring(result.indexOf("HWaddr")+6, result.length()-1);

             /*if(Mac.length()>1){
                 Mac = Mac.replaceAll(" ", "");
                 result = "";
                 String[] tmp = Mac.split(":");
                 for(int i = 0;i<tmp.length;++i){
                     result +=tmp[i];
                 }
             }*/
			result = Mac;
		}
		result= result.replaceAll(":", "");
		return result;
	}

	private static String callCmd(String cmd,String filter) {
		String result = "";
		String line = "";
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
			InputStreamReader is = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader (is);

			//执行命令cmd，只取结果中含有filter的这一行
			while ((line = br.readLine ()) != null && line.contains(filter)== false) {
				//result += line;
			}

			result = line;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}


}


