package com.unimelb.aichatbot.modules.searchFriend.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unimelb.aichatbot.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Friend {
    String avatarUrl;
    String name;

    public Friend(String avatarUrl, String name) {
        this.avatarUrl = avatarUrl;
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getName() {
        return name;
    }

    public View getView(View convertView, ViewGroup parent, Context mContext) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.row_item_friend,parent,false);

        ImageView imageAvatar = (ImageView)convertView.findViewById(R.id.image_avatar);
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Bitmap bmp = (Bitmap) msg.obj;
                        imageAvatar.setImageBitmap(bmp);
                        break;
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bmp = getURLImage(getAvatarUrl());
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bmp;
                handler.sendMessage(msg);
            }
        }).start();

        TextView textName = (TextView)convertView.findViewById(R.id.text_name);
        textName.setText(this.name);

        return convertView;
    }

    // load image by url
    public Bitmap getURLImage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.connect();
            InputStream is = conn.getInputStream();
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
