package com.haifeiWu.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import sun.misc.BASE64Encoder;

/**
 * 这个工具类，用到了吗？？？
 * 
 * @author wuhaifei
 * @2017年4月15日
 */
public class Base64 {// 用到了吗？？？
	// @Test
	// public void test() {
	// String strImg = GetImageStr();
	// System.out.println(strImg);
	// GenerateImage(strImg);
	// }

	@SuppressWarnings("restriction")
	public static String file2base64(File file) {
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(buffer);// 返回Base64编码过的字节数组字符串
	}

	// 图片转化成base64字符串
	// public static String GetImageStr() {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
	// String imgFile = "D://timg.jpg";// 待处理的图片
	// InputStream in = null;
	// byte[] data = null;
	// // 读取图片字节数组
	// try {
	// in = new FileInputStream(imgFile);
	// data = new byte[in.available()];
	// in.read(data);
	// in.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// // 对字节数组Base64编码
	// BASE64Encoder encoder = new BASE64Encoder();
	// return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	// }

	// base64字符串转化成图片
	// public static boolean GenerateImage(String imgStr) { //
	// //对字节数组字符串进行Base64解码并生成图片
	// if (imgStr == null) // 图像数据为空
	// return false;
	// BASE64Decoder decoder = new BASE64Decoder();
	// try {
	// // Base64解码
	// byte[] b = decoder.decodeBuffer(imgStr);
	// for (int i = 0; i < b.length; ++i) {
	// if (b[i] < 0) {// 调整异常数据
	// b[i] += 256;
	// }
	// }
	// // 生成jpeg图片
	// String imgFilePath = "d://222.jpg";// 新生成的图片
	// OutputStream out = new FileOutputStream(imgFilePath);
	// out.write(b);
	// out.flush();
	// out.close();
	// return true;
	// } catch (Exception e) {
	// return false;
	// }
	// }
}
