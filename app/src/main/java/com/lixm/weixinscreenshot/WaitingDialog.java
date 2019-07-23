package com.lixm.weixinscreenshot;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

public class WaitingDialog extends Dialog {

	private String text;
	public TextView txtView;

	public WaitingDialog(Context context) {
		super(context, R.style.WaitingDialog);
	}

	public WaitingDialog(Context context, int theme) {
		super(context, theme);
		setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_waiting_dialog);
		txtView = findViewById(R.id.text_waiting_dialog);
		txtView.setText(text);
	}

	public void setRoundName(String text) {
		this.text = text;
		if (txtView != null) {
			txtView.setText(text);
		}
	}

	@Override
	public void show() {
		if(TextUtils.isEmpty(text)){
			text ="请稍后。。。";
		}
		super.show();
	}
}
