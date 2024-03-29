package com.lixm.weixinscreenshot.PhotoAlbums;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;

import androidx.collection.LruCache;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: NativeImageLoader
 * @Description: 加载本地图片
 * @author cwq
 * @date 2015年8月27日 下午3:20:08
 *
 */
public class NativeImageLoader {

	private LruCache<String, Bitmap> mMemoryCache;
	private static NativeImageLoader mInstance = new NativeImageLoader();
	private ExecutorService mImageThreadPool = Executors.newFixedThreadPool(1);

	private NativeImageLoader(){
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		final int cacheSize = maxMemory / 4;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
			}
		};
	}

	public static NativeImageLoader getInstance(){
		return mInstance;
	}

	public Bitmap loadNativeImage(final String path, final NativeImageCallBack mCallBack){
		return this.loadNativeImage(path, null, mCallBack);
	}

	public Bitmap loadNativeImage(final String path, final Point mPoint, final NativeImageCallBack mCallBack){
		Bitmap bitmap = getBitmapFromMemCache(path);

		final Handler mHander = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				mCallBack.onImageLoader((Bitmap)msg.obj, path);
			}
		};

		if(bitmap == null){
			mImageThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					Bitmap mBitmap = decodeThumbBitmapForFile(path, mPoint == null ? 0 : mPoint.x, mPoint == null ? 0 : mPoint.y);
					Message msg = mHander.obtainMessage();
					msg.obj = mBitmap;
					mHander.sendMessage(msg);

					addBitmapToMemoryCache(path, mBitmap);
				}
			});
		}
		return bitmap;

	}

	private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null && bitmap != null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	private Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

	private Bitmap decodeThumbBitmapForFile(String path, int viewWidth, int viewHeight){
		Bitmap bitmap = null;
		try{
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeFile(path, options);
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inPurgeable = true;//设置内存不足对象能被回收
			options.inSampleSize = computeScale(options, viewWidth, viewHeight);

			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(path, options);

			return bitmap;
		}catch (Exception e){
			e.printStackTrace();
			try {
				return Bimp.revitionImageSize(new File(path));
			} catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
		}catch (OutOfMemoryError outOfMemoryError){
			outOfMemoryError.printStackTrace();
			try {
				return Bimp.revitionImageSize(new File(path));
			} catch (OutOfMemoryError | Exception error){
				error.printStackTrace();
				return null;
			}
		}
	}

	private int computeScale(BitmapFactory.Options options, int viewWidth, int viewHeight){
		int inSampleSize = 1;
		if(viewWidth == 0){
			return inSampleSize;
		}
		int bitmapWidth = options.outWidth;
		int bitmapHeight = options.outHeight;

		if(bitmapWidth > viewWidth || bitmapHeight > viewWidth){
			int widthScale = Math.round((float) bitmapWidth / (float) viewWidth);
			int heightScale = Math.round((float) bitmapHeight / (float) viewWidth);

			inSampleSize = widthScale < heightScale ? widthScale : heightScale;
		}
		return inSampleSize;
	}

	public interface NativeImageCallBack{
		void onImageLoader(Bitmap bitmap, String path);
	}
}
