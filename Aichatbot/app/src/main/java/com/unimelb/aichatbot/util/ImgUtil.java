package com.unimelb.aichatbot.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImgUtil {

    public static void setImgView(Context context, String url, ImageView view) {
        if (url == null || url.isEmpty()) {
            return;
        }
        if (url.contains("http")) {
            Glide.with(context).load(url).into(view);
            return;
        }
        Glide.with(context).load(MD5Util.md5Avatar(url)).into(view);
    }
}
