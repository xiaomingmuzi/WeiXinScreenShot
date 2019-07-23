package com.lixm.weixinscreenshot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.lixm.weixinscreenshot.PhotoAlbums.MyAlbumActivity;

import java.io.File;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final int HEAD_INTENT_FROM_IMAGE = 1001;
    private static final int PIC_INTENT_FROM_IMAGE = 1002;
    private final int REQUEST_CODE = 0; // 请求码

    private Context mContext;
    private ImageView iv_head, iv_img;
    private EditText et_content;
    private RadioGroup rg;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mContext = this;

        iv_head = findViewById(R.id.iv_head);
        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MyAlbumActivity.class);
                intent.putExtra("number", 1);
                ((Activity) mContext).startActivityForResult(intent, HEAD_INTENT_FROM_IMAGE);
            }
        });

        et_content = findViewById(R.id.et_content);
        iv_img = findViewById(R.id.iv_img);
        iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MyAlbumActivity.class);
                intent.putExtra("number", 1);
                ((Activity) mContext).startActivityForResult(intent, PIC_INTENT_FROM_IMAGE);
            }
        });

        rg = findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(headPath)){
                    Toast.makeText(mContext,"请选择头像!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(et_content.getText().toString().trim())){
                    Toast.makeText(mContext,"请输入文字内容!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(picturePath)){
                    Toast.makeText(mContext,"请选择图片!",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(MainActivity.this,WeiXinQunActivity.class);
                intent.putExtra("HeadPath",headPath);
                intent.putExtra("Content",et_content.getText().toString().trim());
                intent.putExtra("PicPath",picturePath);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 缺少权限时, 进入权限配置页面
        if (PermissionsChecker.lacksPermissions(this, PARAM.PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PARAM.PERMISSIONS);
    }

    private String headPath, picturePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case PIC_INTENT_FROM_IMAGE:
                if (data == null) {
                    return;
                }
                List<String> pic = data.getStringArrayListExtra("path");
                if (pic != null && pic.size() > 0) {
                    picturePath = pic.get(0);
                    File mFile = new File(picturePath);
                    Uri mUri = Uri.fromFile(mFile);
                    Glide.with(this).load(mUri).asBitmap().into(iv_img);
                }
                break;
            case HEAD_INTENT_FROM_IMAGE:
                if (data == null) {
                    return;
                }
                List<String> mlist = data.getStringArrayListExtra("path");
                if (mlist != null && mlist.size() > 0) {
                    headPath = mlist.get(0);
                    File mFile = new File(headPath);
                    Uri mUri = Uri.fromFile(mFile);
                    GlideUtils.getCircleBitmap(this, mUri, iv_head);
                }
                break;
            case REQUEST_CODE:
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED)//用户拒绝开启权限
                    finish();
                else if (resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {//权限授权成功
                    Log.i(getLocalClassName(), "===========权限授权成功==============");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
