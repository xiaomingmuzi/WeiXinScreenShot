package com.lixm.weixinscreenshot.PhotoAlbums;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.lixm.weixinscreenshot.R;

import java.util.List;


/**
 * @ClassName: ChildAdapter
 * @Description: 相册的适配器
 * @author cwq
 * @date 2015年8月27日 下午3:17:29
 *
 */
public class ChildAdapter extends BaseAdapter {
	private Point mPoint = new Point(0, 0);

	private Bitmap bitmap;

	private GridView mGridView;
	private List<String> list;
	protected LayoutInflater mInflater;
	private Context context;
	public ChildAdapter(Context context, List<String> list, GridView mGridView) {
		this.context=context;
		this.list = list;
		this.mGridView = mGridView;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}


	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		final String path = list.get(position);

		if(convertView == null){
			convertView = mInflater.inflate(R.layout.grid_child_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = convertView.findViewById(R.id.child_image);
			viewHolder.mCheckBox = convertView.findViewById(R.id.child_checkbox);

			viewHolder.mImageView.setOnMeasureListener(new MyImageView.OnMeasureListener() {

				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});

			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mImageView.setTag(path);

		viewHolder.mCheckBox.setOnCheckedChangeListener(null);
		viewHolder.mCheckBox.setChecked(false);
		for (int i = 0; i < MyAlbumActivity.getList().size(); i++) {
			if (MyAlbumActivity.getList().get(i).equals(path)) {
				viewHolder.mCheckBox.setChecked(true);
			}
		}

		viewHolder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					if (MyAlbumActivity.getList().size()== MyAlbumActivity.getNumber()) {
						Toast.makeText(context, "只能选择"+ MyAlbumActivity.getNumber()+"张", Toast.LENGTH_SHORT).show();
						viewHolder.mCheckBox.setChecked(false);
					}else {
						MyAlbumActivity.getList().add(path);
					}
				} else {
					for (int i = 0; i < MyAlbumActivity.getList().size(); i++) {
						if (MyAlbumActivity.getList().get(i).equals(path)) {
							MyAlbumActivity.getList().remove(i);
						}
					}
				}
			}
		});

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (viewHolder.mCheckBox.isChecked()) {
					viewHolder.mCheckBox.setChecked(false);
				}else {
					viewHolder.mCheckBox.setChecked(true);
				}
			}
		});

//		BitmapHelper.display(viewHolder.mImageView ,path,0, R.mipmap.person_icon_default);

		bitmap = null;
		bitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint, new NativeImageLoader.NativeImageCallBack() {

			@Override
			public void onImageLoader(Bitmap bitmap, String path) {
				ImageView mImageView = mGridView.findViewWithTag(path);
				if(bitmap != null && mImageView != null){
					mImageView.setImageBitmap(bitmap);
				}
			}
		});

		if(bitmap != null){
			viewHolder.mImageView.setImageBitmap(bitmap);
		}else{
			viewHolder.mImageView.setImageResource(R.mipmap.person_icon_default);
		}

		return convertView;
	}

	public static class ViewHolder{
		public MyImageView mImageView;
		public CheckBox mCheckBox;
	}


}
