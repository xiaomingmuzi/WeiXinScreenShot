package com.lixm.weixinscreenshot.PhotoAlbums;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import static android.graphics.BitmapFactory.decodeStream;

public class Bimp {
    public static int max = 0;

    public static ArrayList<ImageItem> tempSelectBitmap = new ArrayList<>(); // 选择的图片的列表
    public static ArrayList<ImageItem> temp = new ArrayList<>(); // 选择的图片的临时列表
    public static ArrayList<ImageItem> dataList = new ArrayList<>();
    public static ArrayList<ImageBucket> contentList = new ArrayList<>();
    public static  void cleanAll(){
        try{
            Bimp.tempSelectBitmap.clear();
            Bimp.temp.clear();
            Bimp.dataList.clear();
            Bimp.contentList.clear();
            Res.mContextClass = null;
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static Bitmap revitionImageSize(File file) {
        try {

            BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                    file));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            decodeStream(in, null, options);
            in.close();
            int inSampleSize = 2;
                // 计算出一个数值，必须符合为2的幂（1，2，4，8，tec），赋值给inSampleSize
                // 图片宽高应大于期望的宽高的时候，才进行计算
                while ((options.outHeight / inSampleSize) > 1280
                        && (options.outWidth / inSampleSize) > 720) {
                    inSampleSize *= 2;
                }
            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;
            in = new BufferedInputStream(new FileInputStream(
                    file));
            Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
            in.close();
            return  bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
