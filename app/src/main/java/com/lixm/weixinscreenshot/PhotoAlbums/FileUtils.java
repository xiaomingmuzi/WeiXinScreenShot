package com.lixm.weixinscreenshot.PhotoAlbums;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import com.lixm.weixinscreenshot.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

	public static String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath()
			+ "/com.cnfol.financialplanner/";
	private static int counter;

	public static String saveBitmap(Context context,Bitmap bm, String picName) {
		try {
			String path = SDPATH + "/temp";
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			if(bm==null){
				bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.loadfailed);
			}
			File file = new File(path, picName + ".jpg");
			FileOutputStream out = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			bm.recycle();
			return file.getAbsolutePath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * 
	 * @param dir
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	/**
	 * 查找特定字符串出现的次数
	 * 
	 * @param original
	 *            原始字符串
	 * @param key
	 *            要查找的字符串
	 * @return
	 */
	public static int stringNumbers(String original, String key) {
		counter = 0;
		if (TextUtils.isEmpty(original) || TextUtils.isEmpty(key)) {
			return 0;
		}
		return getStringNumbers(original, key);

	}

	private static int getStringNumbers(String original, String key) {
		if (original.indexOf(key) == -1) {
			return 0;
		} else {
			counter++;
			getStringNumbers(original.substring(original.indexOf(key) + key.length()), key);
			return counter;
		}
	}

	/**
	 * Save Bitmap to a file.保存图片到SD卡。
	 * @param bitmap
	 * @param _file
	 * @throws IOException
	 */
	public static void saveBitmapToFile(Bitmap bitmap, String _file) throws IOException {
		BufferedOutputStream os = null;
		try {
			File file = new File(_file);
			// String _filePath_file.replace(File.separatorChar +
			// file.getName(), "");
			int end = _file.lastIndexOf(File.separator);
			String _filePath = _file.substring(0, end);
			File filePath = new File(_filePath);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			file.createNewFile();
			os = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// Log.e("", e.getMessage(), e);
				}
			}
		}
	}

	public static File createSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}

	public static void delFile(String fileName) {
		File file = new File(SDPATH + fileName);
		if (file.isFile()) {
			file.delete();
		}
		// file.exists();
	}

	public static void deleteDir() {
		File dir = new File(SDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete();
			else if (file.isDirectory())
				deleteDir();
		}
		dir.delete();
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

}
