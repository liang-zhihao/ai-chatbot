package com.unimelb.aichatbot.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImgUtil {

    public static void setImgView(Context context, String url, ImageView view) {
        Glide.with(context).load(url).into(view);
    }
}
