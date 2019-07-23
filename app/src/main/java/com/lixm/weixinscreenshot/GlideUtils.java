package com.lixm.weixinscreenshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * Describe:
 * <p>
 * Author: Lixm
 * Date: 2019/7/19
 */
public class GlideUtils {

    public static void getCircleBitmap(final Context context, Uri uri, final ImageView img) {
        Glide.with(context).load(uri).asBitmap().centerCrop().placeholder(R.mipmap.default_avatar).centerCrop().into(new BitmapImageViewTarget(img) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                img.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
}
