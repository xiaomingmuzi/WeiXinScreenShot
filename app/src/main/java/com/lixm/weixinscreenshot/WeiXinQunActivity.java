package com.lixm.weixinscreenshot;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class WeiXinQunActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_xin_qun);

        String content=getIntent().getStringExtra("Content");
        String headPath=getIntent().getStringExtra("HeadPath");
        String PicPath=getIntent().getStringExtra("PicPath");

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ListView listView = findViewById(R.id.listview);
        ArrayList<ChatBean> beans = new ArrayList<>();
//        beans.add(new ChatBean(0, "后来就穿普通的了"));
//        beans.add(new ChatBean(0, "有帮忙看的也好"));
        beans.add(new ChatBean(0, "我婆婆来了，做饭挺好吃的，到家能吃上饭了"));
        beans.add(new ChatBean(0, "我穿的普通的，长筒的怕勒腿肚子，腿粗"));
        beans.add(new ChatBean(0, "恒温冲奶器我买完，感觉是不是有点鸡肋啊"));
//        beans.add(new ChatBean(0, "必须用清洗剂啊！洗奶瓶"));
        beans.add(new ChatBean(0, "我周围的人，都说不要买消毒的"));
//        beans.add(new ChatBean(0, "明天主要参加活动"));
//        beans.add(new ChatBean(0, "奶有腥味吧"));


        ChatBean chatBean=new ChatBean();
        chatBean.setContentType(1);
        chatBean.setType(1);
        chatBean.setPicPath(PicPath);
        chatBean.setHeadPath(headPath);
        beans.add(chatBean);

        ChatBean chatBean1=new ChatBean();
        chatBean1.setContentType(0);
        chatBean1.setType(1);
        chatBean1.setContent(content);
        chatBean1.setHeadPath(headPath);
        beans.add(chatBean1);
        MyAdapter adapter = new MyAdapter(this, beans);
        listView.setAdapter(adapter);
    }
}
