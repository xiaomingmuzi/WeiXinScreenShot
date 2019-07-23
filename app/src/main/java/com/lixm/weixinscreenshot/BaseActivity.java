package com.lixm.weixinscreenshot;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Describe:
 * <p>
 * Author: Lixm
 * Date: 2019/7/19
 */
public class BaseActivity extends Activity {

    private int color_id =R.color.color_ed;

    protected SystemBarTintManager tintManager;

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        boolean flag = isFullScreen();
        if (!flag) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(true);
                tintManager = new SystemBarTintManager(this);
                tintManager.setStatusBarTintEnabled(true);
                int color = getTitleColors();
                tintManager.setStatusBarTintResource(color);//通知栏所需颜色
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(Color.TRANSPARENT);
                }
            }
        }
    }

    /**
     * 设置状态栏颜色  颜色ID需要先写入color文件里面
     */
    protected int getTitleColors() {
        return color_id;
    }

    /**
     * 默认返回false，子类可重写此方法返回true实现全屏
     *
     * @return 是否是全屏Activity
     */
    protected boolean isFullScreen() {
        return false;
    }
}
