package com.artgallery.tool;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTool {
	public static byte[] read(InputStream inStream) throws Exception{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();//设置内存
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len = inStream.read(buffer)) != -1){
			outStream.write(buffer, 0, len);//写进内存
		}
		inStream.close();
		return outStream.toByteArray();
	}
}
