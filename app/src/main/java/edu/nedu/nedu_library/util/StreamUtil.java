package edu.nedu.nedu_library.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamUtil {

	public static String streamToString(InputStream  in){
		String result = null;
		try{
			//创建一个字节数组写入流
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
				out.flush();
			}
			result = new String(out.toByteArray(),"utf-8");
			//result = new String(out.toByteArray());
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String streamToString2(InputStream  in){
		String result = null;
		try{
			//创建一个字节数组写入流
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = 0;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
				out.flush();
			}
			//result = new String(out.toByteArray(),"utf-8");
			result = new String(out.toByteArray());
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
