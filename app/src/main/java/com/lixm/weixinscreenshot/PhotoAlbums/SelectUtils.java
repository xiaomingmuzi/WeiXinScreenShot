package com.lixm.weixinscreenshot.PhotoAlbums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/5/26.
 */
public class SelectUtils {

    /**
     * 图片地址集
     */
    private static List<String> url_list = new ArrayList<String>();

    public static List getList(){
        return url_list;
    }

    public static void addurl(String url){
        url_list.add(url);
    }

    public static Object temp;
}
