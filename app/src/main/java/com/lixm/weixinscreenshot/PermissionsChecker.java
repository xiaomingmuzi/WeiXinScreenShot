package com.lixm.weixinscreenshot;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * User: LXM
 * Date: 2016-04-26
 * Time: 09:21
 * Detail:适配Android6.0权限检测器
 */
public class PermissionsChecker {
//    private final Context mContext;
//
//    public PermissionsChecker(Context context) {
//        mContext = context.getApplicationContext();
//    }

    // 判断权限集合
    public static boolean lacksPermissions(Context context,String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(context,permission)) {
                Log.i("权限","缺少的权限=======" + permission);
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否缺少权限
     *
     * @param permission
     * @return true 缺少权限；false 已开启
     */
    public static boolean lacksPermission(Context context,String permission) {
        try {
            return ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            return true;
        }

    }

    // 含有全部的权限
    public static boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}
