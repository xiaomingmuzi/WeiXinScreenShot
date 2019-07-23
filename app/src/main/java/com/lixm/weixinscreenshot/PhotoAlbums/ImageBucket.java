package com.lixm.weixinscreenshot.PhotoAlbums;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class ImageBucket implements Parcelable {
	public int count = 0;
	public String bucketName;
	public List<ImageItem> imageList;

	protected ImageBucket(Parcel in) {
		count = in.readInt();
		bucketName = in.readString();
		imageList = in.createTypedArrayList(ImageItem.CREATOR);
	}

	public static final Creator<ImageBucket> CREATOR = new Creator<ImageBucket>() {
		@Override
		public ImageBucket createFromParcel(Parcel in) {
			return new ImageBucket(in);
		}

		@Override
		public ImageBucket[] newArray(int size) {
			return new ImageBucket[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(count);
		dest.writeString(bucketName);
		dest.writeTypedList(imageList);
	}
}
