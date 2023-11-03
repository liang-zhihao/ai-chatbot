package com.unimelb.aichatbot.modules.common.model;

public class FriendListItem {
    String avatarUrl;
    String name;
    String description;

    public FriendListItem(String avatarUrl, String name) {
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.description = "";
    }

    public FriendListItem(String avatarUrl, String name, String description) {
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.description = description;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getName() {
        return name;
    }

    // public View getView(View convertView, ViewGroup parent, Context mContext) {
    //     convertView = LayoutInflater.from(mContext).inflate(R.layout.row_item_friend, parent, false);
    //
    //     ImageView imageAvatar = (ImageView) convertView.findViewById(R.id.image_avatar);
    //     Handler handler = new Handler() {
    //         public void handleMessage(Message msg) {
    //             switch (msg.what) {
    //                 case 0:
    //                     Bitmap bmp = (Bitmap) msg.obj;
    //                     imageAvatar.setImageBitmap(bmp);
    //                     break;
    //             }
    //         }
    //     };
    //
    //     new Thread(() -> {
    //         Bitmap bmp = getURLImage(getAvatarUrl());
    //         Message msg = new Message();
    //         msg.what = 0;
    //         msg.obj = bmp;
    //         handler.sendMessage(msg);
    //     }).start();
    //
    //     TextView textName = (TextView) convertView.findViewById(R.id.text_name);
    //     textName.setText(this.name);
    //
    //     return convertView;
    // }
    //
    // public void bindData(Context context, View itemView) {
    //     ImageView imageAvatar = itemView.findViewById(R.id.image_avatar);
    //     TextView textName = itemView.findViewById(R.id.text_name);
    //     // Handle image loading in a separate thread with proper callback to update UI
    //     loadAvatarImage(imageAvatar);
    //     textName.setText(getName());
    // }
    //
    // private void loadAvatarImage(ImageView imageView) {
    //     Handler handler = new Handler(Looper.getMainLooper()) {
    //         @Override
    //         public void handleMessage(Message msg) {
    //             if (msg.what == 0) {
    //                 Bitmap bmp = (Bitmap) msg.obj;
    //                 imageView.setImageBitmap(bmp);
    //             }
    //         }
    //     };
    //
    //     new Thread(() -> {
    //         Bitmap bmp = getURLImage(getAvatarUrl());
    //         Message msg = new Message();
    //         msg.what = 0;
    //         msg.obj = bmp;
    //         handler.sendMessage(msg);
    //     }).start();
    // }
    //
    // // load image by url
    // public Bitmap getURLImage(String url) {
    //     Bitmap bmp = null;
    //     try {
    //         URL myurl = new URL(url);
    //
    //         HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
    //         conn.setConnectTimeout(60000);
    //         conn.setDoInput(true);
    //         conn.setUseCaches(false);
    //         conn.connect();
    //         InputStream is = conn.getInputStream();
    //         bmp = BitmapFactory.decodeStream(is);
    //         is.close();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return bmp;
    // }
}
