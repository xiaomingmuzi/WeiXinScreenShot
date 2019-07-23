package com.lixm.weixinscreenshot.PhotoAlbums;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.File;


/**
 * <p>
 * Title:ImageItem 
 * </p>
 * <p>
 * Description: 
 * </p>
 * @author Swh
 * @date 2015-10-10 下午5:47:47
 */
public class ImageItem implements Parcelable {
	
	/**
	 * 
	 */
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	private String imageName;
	private boolean isNet;

	public ImageItem(Parcel in) {
		imageId = in.readString();
		thumbnailPath = in.readString();
		imagePath = in.readString();
		imageName = in.readString();
		isNet = in.readByte() != 0;
	}

	public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
		@Override
		public ImageItem createFromParcel(Parcel in) {
			return new ImageItem(in);
		}

		@Override
		public ImageItem[] newArray(int size) {
			return new ImageItem[size];
		}
	};

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getThumbnailPath() {
		return thumbnailPath;
	}
	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public void getThumbPath(final Context context){
		new Thread(){
			public void run() {

				if(TextUtils.isEmpty(thumbnailPath)||!new File(thumbnailPath).exists()){
					imageName = System.currentTimeMillis()+"";

					try {
						File file = new File(imagePath);
						if(file.exists()&&file.length()>1024*1024){
							Bitmap bitmap = Bimp.revitionImageSize(file);
							if(bitmap==null){
								thumbnailPath = imagePath;
							}else {
								thumbnailPath = FileUtils.saveBitmap(context,bitmap, imageName);
							}

						}else {
							thumbnailPath = imagePath;
						}

					} catch (Exception e) {
						e.printStackTrace();
						thumbnailPath = imagePath;
					}

				}else{
					int count = FileUtils.stringNumbers(imageName, ".");
					if(count!=0){
						File file = new File(thumbnailPath);
						boolean flag = file.renameTo(new File(file.getParent(), System.currentTimeMillis()+".jpg"));
						if(flag){
							thumbnailPath = file.getAbsolutePath();
						}else{
						}
					}


				}
			}
		}.start();
	}
	public boolean isNet() {
		return isNet;
	}
	public void setNet(boolean isNet) {
		this.isNet = isNet;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(imageId);
		dest.writeString(thumbnailPath);
		dest.writeString(imagePath);
		dest.writeString(imageName);
		dest.writeByte((byte) (isNet ? 1 : 0));
	}
}
