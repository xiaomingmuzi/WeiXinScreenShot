package com.lixm.weixinscreenshot.PhotoAlbums;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/** 
* @ClassName: MyImageView 
* @Description: 自定义imageview
* @author cwq
* @date 2015年8月27日 下午3:18:25 
*  
*/
public class MyImageView extends ImageView {
	private OnMeasureListener onMeasureListener;
	
	public void setOnMeasureListener(OnMeasureListener onMeasureListener) {
		this.onMeasureListener = onMeasureListener;
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		if(onMeasureListener != null){
			onMeasureListener.onMeasureSize(getMeasuredWidth(), getMeasuredHeight());
		}
	}

	public interface OnMeasureListener{
		void onMeasureSize(int width, int height);
	}
	
}
