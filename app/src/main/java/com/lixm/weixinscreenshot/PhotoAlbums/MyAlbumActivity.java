package com.lixm.weixinscreenshot.PhotoAlbums;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lixm.weixinscreenshot.BaseActivity;
import com.lixm.weixinscreenshot.R;
import com.lixm.weixinscreenshot.WaitingDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author cwq
 * @version 创建时间：2015年8月27日 下午3:01:50
 * @Description: 自定义相册
 * intent.putExtra("number", number);  需要传一个图片张数
 */
public class MyAlbumActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    /**
     * 确定扫描完成
     */
    private final static int SCAN_OK = 1;
    /**
     * 确定扫描失败
     */
    private final static int SCAN_ERROR = 2;
    /**
     * 等待图标
     */
    private WaitingDialog Dialog;
    /**
     * 从内容提供者获得的数据源
     */
    private HashMap<String, List<String>> mGruopMap = new HashMap<String, List<String>>();
    /**
     * 流用于判断图片是否可用
     */
    private FileInputStream fs = null;
    /**
     * 相册适配器
     */
    private GroupAdapter adapter;
    /**
     * gridview对象
     */
    private GridView gridview_photo;
    /**
     * 返回按钮
     */
    private RelativeLayout layout_title_left;
    /**
     * 确认按钮
     */
    private LinearLayout layout_title_right;
    /**
     * 数据源的信息
     */
    private List<ImageBean> list = new ArrayList<ImageBean>();
    /**
     * 最多选择的图片张数
     */
    static int number = 0;

    @Override
    protected void onCreate(Bundle saveInBundleState) {
        super.onCreate(saveInBundleState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_myalbum);
        number = getIntent().getIntExtra("number", 0);
        getImages();
        initviews();
        listeners();
    }

    /**
     * @Title: listener
     * @Description: 设置监听事件
     * <p/>
     * {@link #onClick(View)}
     * {@link #onItemClick(AdapterView, View, int, long)}
     */
    private void listeners() {
        layout_title_right.setOnClickListener(this);
        layout_title_left.setOnClickListener(this);
        gridview_photo.setOnItemClickListener(this);
    }

    /**
     * @return List<String>    图片集
     * @Title: getList
     * @Description: 给二级界面提供一个接口获取当前以选中的图片
     */
    public static List<String> getList() {
        return SelectUtils.getList();
    }

    /**
     * @return number    图片数量
     * @Title: getNumber
     * @Description: 给二级界面提供一个接口获取当前最多可以选中的图片的数量
     */
    public static int getNumber() {
        return number;
    }

    /**
     * @Title: initview
     * @Description: 初始化界面
     */
    private void initviews() {
        gridview_photo = findViewById(R.id.gridview_photo);
        layout_title_left = findViewById(R.id.layout_title_left);
        TextView tv_style1_title = findViewById(R.id.tv_style1_title);
        tv_style1_title.setText("相册");
        TextView tv_title_right = findViewById(R.id.tv_title_right);
        tv_title_right.setText("确定");
        layout_title_right = findViewById(R.id.layout_title_right);
    }

    /**
     * @Fields mHandler : 处理子线程数据
     * <p/>
     * {@link #getImages()}
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_OK:
                    // 关闭进度条
                    Dialog.dismiss();
                    adapter = new GroupAdapter(MyAlbumActivity.this, list = subGroupOfImage(mGruopMap), gridview_photo);
                    gridview_photo.setAdapter(adapter);
                    break;
                case SCAN_ERROR:
                    Dialog.dismiss();
                    Toast.makeText(MyAlbumActivity.this,"请检查权限",Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };


    /**
     * @param mGruopMap 数据源
     * @return List<ImageBean>    转化后的数据源
     * @Title: subGroupOfImage
     * @Description: 转化数据源形式，将HashMap变为List
     */
    private List<ImageBean> subGroupOfImage(HashMap<String, List<String>> mGruopMap) {

        List<ImageBean> list = new ArrayList<ImageBean>();
        if (mGruopMap.size() == 0) {
            return list;
        }

        Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            ImageBean mImageBean = new ImageBean();
            String key = entry.getKey();
            List<String> value = entry.getValue();

            mImageBean.setFolderName(key);
            mImageBean.setImageCounts(value.size());
            if (value.size() > 0) {
                mImageBean.setTopImagePath(value.get(0));//获取该组的第一张图片
            } else {
                mImageBean.setTopImagePath("");//获取该组的第一张图片
            }


            list.add(mImageBean);
        }

        return list;

    }

    /**
     * @Title: getImages
     * @Description: 利用ContentProvider扫描手机中的图片
     * <p/>
     * {@link #mHandler}
     */
    private void getImages() {

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }

        // 显示进度条
        Dialog = new WaitingDialog(this);
        Dialog.setRoundName("正在加载");
        Dialog.show();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {


                    Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    ContentResolver mContentResolver = MyAlbumActivity.this.getContentResolver();

                    Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

                    while (mCursor.moveToNext()) {

                        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        //获取该图片的父路径名
                        String parentName = new File(path).getParentFile().getName();
                        try {
                            fs = new FileInputStream(new File(path));
                        } catch (FileNotFoundException e) {
                            e.toString();
                        }
                        //根据父路径名将图片放入到mGruopMap中
                        if (!mGruopMap.containsKey(parentName)) {
                            List<String> chileList = new ArrayList<String>();
                            if (fs != null) {
                                chileList.add(path);
                            }
                            mGruopMap.put(parentName, chileList);
                        } else {
                            if (fs != null) {
                                mGruopMap.get(parentName).add(path);
                            }
                        }
                        try {
                            fs.close();
                            fs = null;
                        } catch (Exception e) {
                            e.toString();
                        }
                    }

                    mCursor.close();

                    mHandler.sendEmptyMessage(SCAN_OK);
                } catch (Exception e) {
                    e.toString();
                    mHandler.sendEmptyMessage(SCAN_ERROR);
                }
            }
        }).start();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode== KeyEvent.KEYCODE_BACK){
            SelectUtils.getList().clear();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 == 1002) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra("path", (ArrayList<String>) SelectUtils.getList());
            setResult(1001, intent);
            finish();
            SelectUtils.getList().clear();
        }
    }

    /**
     * @Title: onClick
     * @Description: 监听事件
     * <p/>
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_left:
                SelectUtils.getList().clear();
                this.finish();
                break;

            case R.id.layout_title_right:
                Intent intent = new Intent();
                intent.putStringArrayListExtra("path", (ArrayList<String>) SelectUtils.getList());
                setResult(1001, intent);
                finish();
                SelectUtils.getList().clear();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mGruopMap != null){
            mGruopMap.clear();
            mGruopMap = null;
        }
        setContentView(R.layout.view_null);
    }

    /**
     * @Title: onItemClick
     * @Description: grideview监听事件
     * <p/>
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = list.get(position).getFolderName();
         childList = mGruopMap.get(name);

        Intent mIntent = new Intent(MyAlbumActivity.this, ShowImageActivity.class);
        //TODO 大数据传递时，出现崩溃问题，intent不能携带大数据
//        mIntent.putStringArrayListExtra("data", (ArrayList<String>) childList);
        mIntent.putExtra("name", name);
        startActivityForResult(mIntent, 1002);
    }

    public static List<String> childList;

    public static List<String> getChildList(){
        return childList;
    }
}
