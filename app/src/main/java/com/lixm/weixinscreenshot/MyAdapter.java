package com.lixm.weixinscreenshot;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Describe:
 * <p>
 * Author: Lixm
 * Date: 2019/7/19
 */
public class MyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ChatBean> beans;
    private int[] heads = new int[]{R.mipmap.head1, R.mipmap.head2, R.mipmap.head3, R.mipmap.head4,
            R.mipmap.head5, R.mipmap.head6, R.mipmap.head7, R.mipmap.head8, R.mipmap.head9, R.mipmap.head10};

    public MyAdapter(Context context, ArrayList<ChatBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public int getCount() {
        return beans.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return beans.get(position).getType();
    }

    Holder1 holder1, holder2;

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ChatBean bean = beans.get(i);
        if (getItemViewType(i) == 0) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.left_item, null);
                holder1 = new Holder1();
                holder1.content = view.findViewById(R.id.content);
                holder1.head = view.findViewById(R.id.head);
                view.setTag(holder1);
            } else holder1 = (Holder1) view.getTag();

            holder1.content.setText(bean.getContent());
            holder1.head.setBackgroundResource(heads[i]);
        } else {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.right_item, null);
                holder2 = new Holder1();
                holder2.content = view.findViewById(R.id.content);
                holder2.tv_time = view.findViewById(R.id.tv_time);
                holder2.head = view.findViewById(R.id.head);

                holder2.img_rafl=view.findViewById(R.id.img_rafl);
                holder2.img=view.findViewById(R.id.iv);
                view.setTag(holder2);
            } else holder2 = (Holder1) view.getTag();


            File mFile = new File(bean.getHeadPath());
            Uri mUri = Uri.fromFile(mFile);
            Glide.with(context).load(mUri).asBitmap().into(holder2.head);
            if (bean.getContentType()==0){
                holder2.tv_time.setVisibility(View.GONE);
                holder2.content.setVisibility(View.VISIBLE);
                holder2.img.setVisibility(View.GONE);
                holder2.content.setText(bean.getContent());
            }else{
                holder2.tv_time.setVisibility(View.VISIBLE);
                long time = System.currentTimeMillis() / 1000 - 360;
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                holder2.tv_time.setText(sdf.format(new Date(Long.valueOf(time + "000"))));

                holder2.content.setVisibility(View.GONE);
                holder2.img.setVisibility(View.VISIBLE);
                File picFile = new File(bean.getPicPath());
                Uri picUri = Uri.fromFile(picFile);
                Glide.with(context).load(picUri).asBitmap().into(holder2.img);
            }
        }
        return view;
    }

    class Holder1 {
        ImageView head;
        TextView content, tv_time;
        RoundAngleFrameLayout img_rafl;
        ImageView img;
    }


    /**
     * 获取时分秒
     *
     * @return
     */
    public static String getCurrentHMSTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String date = format.format(new Date());
        return date;
    }

}
