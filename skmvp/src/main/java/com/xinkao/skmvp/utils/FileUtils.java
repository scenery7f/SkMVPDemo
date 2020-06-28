package com.xinkao.skmvp.utils;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

	/**
	 * 将输入流写入文件
	 *
	 * @param inputString
	 * @param filePath
	 */
	public static Exception writeFile(InputStream inputString, String filePath) {

		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);

			byte[] b = new byte[1024];

			int len;
			while ((len = inputString.read(b)) != -1) {
				fos.write(b,0,len);
			}
			inputString.close();
			fos.close();

		} catch (FileNotFoundException e) {
			Logger.e("文件写入错误：" + "FileNotFoundException");
			e.printStackTrace();
			return e;
		} catch (IOException e) {
			Logger.e("文件写入错误：" + "IOException");
			e.printStackTrace();
			return e;
		}

		return null;
	}

}