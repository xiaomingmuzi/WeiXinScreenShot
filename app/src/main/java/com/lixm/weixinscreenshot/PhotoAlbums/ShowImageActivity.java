package com.lixm.weixinscreenshot.PhotoAlbums;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lixm.weixinscreenshot.R;

import java.util.List;

/** 
* @ClassName: ShowImageActivity 
* @Description: 二级相册
* @author cwq
* @date 2015年8月28日 下午2:51:47 
*  
*/
public class ShowImageActivity extends Activity implements OnClickListener {
	/**grideview的对象*/
	private GridView mGridView;
	/**从上一个界面获取到的数据*/
	private List<String> list;
	/**适配器*/
	private ChildAdapter adapter;
	/**返回按钮*/
	private RelativeLayout layout_title_left;
	/**确定按钮*/
	private LinearLayout layout_title_right;
	/**标题名称*/
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_image_activity);
//		list = getIntent().getStringArrayListExtra("data");
		list = MyAlbumActivity.getChildList();
		name=getIntent().getStringExtra("name");
		initviews();
		listeners();
	}

	/**
	* @Title: listener 
	* @Description: 设置监听事件
	* 
	* {@link #onClick(View)}
	*
	*/
	private void listeners() {
		layout_title_left.setOnClickListener(this);
		layout_title_right.setOnClickListener(this);
	}

	/** 
	* @Title: initview 
	* @Description: 初始化界面
	*
	*/
	private void initviews() {
		mGridView = findViewById(R.id.child_grid);
		
		adapter = new ChildAdapter(this, list, mGridView);
		mGridView.setAdapter(adapter);
		layout_title_left= findViewById(R.id.layout_title_left);
		TextView tv_style1_title= findViewById(R.id.tv_style1_title);
		tv_style1_title.setEms(7);
		tv_style1_title.setEllipsize(TruncateAt.END);
		tv_style1_title.setSingleLine(true);
		tv_style1_title.setText(name);
		TextView tv_title_right= findViewById(R.id.tv_title_right);
		tv_title_right.setText("确定");
		layout_title_right= findViewById(R.id.layout_title_right);
	}
	/** 
	* @Title: initview 
	* @Description: 监听事件
	*
	*/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_title_left:
			this.finish();
			break;

		case R.id.layout_title_right:
			Intent intent=new Intent();
			setResult(1002, intent);
			finish();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		setContentView(R.layout.view_null);
	}

}
