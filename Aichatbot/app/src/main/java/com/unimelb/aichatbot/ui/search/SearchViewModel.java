package com.unimelb.aichatbot.ui.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unimelb.aichatbot.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class SearchViewModel extends ViewModel {
    class Friend {
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

    class NewFriend extends Friend {
        public NewFriend(String avatarUrl, String name) {
            super(avatarUrl, name);
        }

        public View getView(View convertView, ViewGroup parent, Context mContext) {
            convertView = super.getView(convertView, parent, mContext);

            Button button = (Button) convertView.findViewById(R.id.button_friend);
            button.setVisibility(View.VISIBLE);

            return convertView;
        }

    }

    class FriendWithChat extends Friend {
        String chat;


        public FriendWithChat(String avatarUrl, String name, String chat) {
            super(avatarUrl, name);
            this.chat = chat;
        }

        public View getView(View convertView, ViewGroup parent, Context mContext) {
            convertView = super.getView(convertView, parent, mContext);

            TextView textChat = (TextView) convertView.findViewById(R.id.text_chat);
            textChat.setText(this.chat);
            textChat.setVisibility(View.VISIBLE);

            return convertView;
        }
    }
    private final MutableLiveData<String> mText;
    private final MutableLiveData<List<Friend>> mFriends;


    public SearchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Friends");
        mFriends = new MutableLiveData<>();

        List<Friend> friends = new LinkedList<>();
        friends.add(new Friend("https://s3-alpha-sig.figma.com/img/9493/1b8b/405a3aec1b73b0d0d03ab04af51540b1?Expires=1695600000&Signature=VHjMb~yZfycvcAd3bIoVoncGm87wmNu5CKVmvjti6y9jx3J4cYj-1pgf0Vn5r8Oa453wywtZ4wro7z048LRLoRvUjnHA6eP4qaoo-C4brtQZ4ibruG7RZNRSqyZpApOe91N-3t~IoHLoq2XvRkah7uVPLs6c5b3UooldTgeLoTVoMPjgfLxFgTImY5f4Z82XrLTs5XyjGE5e5-ENnqWY-IvM2BQJF1tHMaiFUMg5mdHsqMxrcmSMW6djz1Q5KKhL89uRlKpLwPisHTeKCFEpIGoRbLYcyag1gUcRJKzcwPEgu4F~Nf9pe-Rdty65W5~E5wPkT~j2ujhBKQgVxueHKA__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Beth Murphy"));
        friends.add(new Friend("https://s3-alpha-sig.figma.com/img/9261/dba4/707be4fc468fe699496620292830adad?Expires=1695600000&Signature=NFoM2wIG4tu~zOzeMrRLLMjsyl1YIlkZcqykvdCiDrimnhxI8MazU0VHcLAXw8Ne4-GBFA~UAuoUz~3NVyAWcVvTuCp6j7YGffY6P-YDKy1oxFxERw69TaP-66DC7UTyoCsOZhDTiecildcSyd4F4fTP508AjpTTU0s5lNIOI98-qMKtPkZTxysRD9nTgRuR55cp-k-5wyjl97m4~jMStX3KEzRd46ehy-vOcMbTQsUyFazW7M7Tmz6EJrSzW8VrPGolADk8p6idXE8ySTILtX0tNRWWtFXOKBWJW5W-mP7rJoEhFuP1tkndthx7qh3yEt1VWsWeXRcSOFlvLgWaiw__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Bonelwa"));
        friends.add(new Friend("https://s3-alpha-sig.figma.com/img/75d2/4e87/1d3808434e5dbd8fee65aacbda0a1170?Expires=1695600000&Signature=R2vrlOuUoL2F~-6UiQGJ~YYNsepwzwau3AJ1Hpi36EKFKeJAV9rQmAQhcWk~GPCf03tZGWiES2zft~8KtG1NmluqJMsNz69mdgbrz4qCjqZs7EVHAunSSeJgOFwghccFJcxckx1YEO-cZT96og8~AHmEoV2YfG46w8lcqHlu41kH1CXqzg4Psk-jIstERP68KpIFuqNuEo~ilwyqc29W2RpscMT9wJ55Zj2cxqy5lVlfgOY0hHHzGLEZ91qMoFvBD1o9jO-alsrdZKXnJgrDHfIR9Xn8MXJkYvsPYqMoKU1GsE4GOJZpLSLLERClc5Q76omCgIl-CZBek2DkphLTbg__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Leonardo Oliveira"));
        friends.add(new NewFriend("https://s3-alpha-sig.figma.com/img/9493/1b8b/405a3aec1b73b0d0d03ab04af51540b1?Expires=1695600000&Signature=VHjMb~yZfycvcAd3bIoVoncGm87wmNu5CKVmvjti6y9jx3J4cYj-1pgf0Vn5r8Oa453wywtZ4wro7z048LRLoRvUjnHA6eP4qaoo-C4brtQZ4ibruG7RZNRSqyZpApOe91N-3t~IoHLoq2XvRkah7uVPLs6c5b3UooldTgeLoTVoMPjgfLxFgTImY5f4Z82XrLTs5XyjGE5e5-ENnqWY-IvM2BQJF1tHMaiFUMg5mdHsqMxrcmSMW6djz1Q5KKhL89uRlKpLwPisHTeKCFEpIGoRbLYcyag1gUcRJKzcwPEgu4F~Nf9pe-Rdty65W5~E5wPkT~j2ujhBKQgVxueHKA__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Beth Murphy"));
        friends.add(new NewFriend("https://s3-alpha-sig.figma.com/img/9261/dba4/707be4fc468fe699496620292830adad?Expires=1695600000&Signature=NFoM2wIG4tu~zOzeMrRLLMjsyl1YIlkZcqykvdCiDrimnhxI8MazU0VHcLAXw8Ne4-GBFA~UAuoUz~3NVyAWcVvTuCp6j7YGffY6P-YDKy1oxFxERw69TaP-66DC7UTyoCsOZhDTiecildcSyd4F4fTP508AjpTTU0s5lNIOI98-qMKtPkZTxysRD9nTgRuR55cp-k-5wyjl97m4~jMStX3KEzRd46ehy-vOcMbTQsUyFazW7M7Tmz6EJrSzW8VrPGolADk8p6idXE8ySTILtX0tNRWWtFXOKBWJW5W-mP7rJoEhFuP1tkndthx7qh3yEt1VWsWeXRcSOFlvLgWaiw__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Bonelwa"));
        friends.add(new NewFriend("https://s3-alpha-sig.figma.com/img/75d2/4e87/1d3808434e5dbd8fee65aacbda0a1170?Expires=1695600000&Signature=R2vrlOuUoL2F~-6UiQGJ~YYNsepwzwau3AJ1Hpi36EKFKeJAV9rQmAQhcWk~GPCf03tZGWiES2zft~8KtG1NmluqJMsNz69mdgbrz4qCjqZs7EVHAunSSeJgOFwghccFJcxckx1YEO-cZT96og8~AHmEoV2YfG46w8lcqHlu41kH1CXqzg4Psk-jIstERP68KpIFuqNuEo~ilwyqc29W2RpscMT9wJ55Zj2cxqy5lVlfgOY0hHHzGLEZ91qMoFvBD1o9jO-alsrdZKXnJgrDHfIR9Xn8MXJkYvsPYqMoKU1GsE4GOJZpLSLLERClc5Q76omCgIl-CZBek2DkphLTbg__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Leonardo Oliveira"));
        friends.add(new Friend("https://s3-alpha-sig.figma.com/img/9493/1b8b/405a3aec1b73b0d0d03ab04af51540b1?Expires=1695600000&Signature=VHjMb~yZfycvcAd3bIoVoncGm87wmNu5CKVmvjti6y9jx3J4cYj-1pgf0Vn5r8Oa453wywtZ4wro7z048LRLoRvUjnHA6eP4qaoo-C4brtQZ4ibruG7RZNRSqyZpApOe91N-3t~IoHLoq2XvRkah7uVPLs6c5b3UooldTgeLoTVoMPjgfLxFgTImY5f4Z82XrLTs5XyjGE5e5-ENnqWY-IvM2BQJF1tHMaiFUMg5mdHsqMxrcmSMW6djz1Q5KKhL89uRlKpLwPisHTeKCFEpIGoRbLYcyag1gUcRJKzcwPEgu4F~Nf9pe-Rdty65W5~E5wPkT~j2ujhBKQgVxueHKA__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Beth Murphy"));
        friends.add(new Friend("https://s3-alpha-sig.figma.com/img/9261/dba4/707be4fc468fe699496620292830adad?Expires=1695600000&Signature=NFoM2wIG4tu~zOzeMrRLLMjsyl1YIlkZcqykvdCiDrimnhxI8MazU0VHcLAXw8Ne4-GBFA~UAuoUz~3NVyAWcVvTuCp6j7YGffY6P-YDKy1oxFxERw69TaP-66DC7UTyoCsOZhDTiecildcSyd4F4fTP508AjpTTU0s5lNIOI98-qMKtPkZTxysRD9nTgRuR55cp-k-5wyjl97m4~jMStX3KEzRd46ehy-vOcMbTQsUyFazW7M7Tmz6EJrSzW8VrPGolADk8p6idXE8ySTILtX0tNRWWtFXOKBWJW5W-mP7rJoEhFuP1tkndthx7qh3yEt1VWsWeXRcSOFlvLgWaiw__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Bonelwa"));
        friends.add(new Friend("https://s3-alpha-sig.figma.com/img/75d2/4e87/1d3808434e5dbd8fee65aacbda0a1170?Expires=1695600000&Signature=R2vrlOuUoL2F~-6UiQGJ~YYNsepwzwau3AJ1Hpi36EKFKeJAV9rQmAQhcWk~GPCf03tZGWiES2zft~8KtG1NmluqJMsNz69mdgbrz4qCjqZs7EVHAunSSeJgOFwghccFJcxckx1YEO-cZT96og8~AHmEoV2YfG46w8lcqHlu41kH1CXqzg4Psk-jIstERP68KpIFuqNuEo~ilwyqc29W2RpscMT9wJ55Zj2cxqy5lVlfgOY0hHHzGLEZ91qMoFvBD1o9jO-alsrdZKXnJgrDHfIR9Xn8MXJkYvsPYqMoKU1GsE4GOJZpLSLLERClc5Q76omCgIl-CZBek2DkphLTbg__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Leonardo Oliveira"));
        friends.add(new FriendWithChat("https://s3-alpha-sig.figma.com/img/9493/1b8b/405a3aec1b73b0d0d03ab04af51540b1?Expires=1695600000&Signature=VHjMb~yZfycvcAd3bIoVoncGm87wmNu5CKVmvjti6y9jx3J4cYj-1pgf0Vn5r8Oa453wywtZ4wro7z048LRLoRvUjnHA6eP4qaoo-C4brtQZ4ibruG7RZNRSqyZpApOe91N-3t~IoHLoq2XvRkah7uVPLs6c5b3UooldTgeLoTVoMPjgfLxFgTImY5f4Z82XrLTs5XyjGE5e5-ENnqWY-IvM2BQJF1tHMaiFUMg5mdHsqMxrcmSMW6djz1Q5KKhL89uRlKpLwPisHTeKCFEpIGoRbLYcyag1gUcRJKzcwPEgu4F~Nf9pe-Rdty65W5~E5wPkT~j2ujhBKQgVxueHKA__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Beth Murphy","Unmatched Toner Cartridge  fa s b j k b ha jQuality 20"));
        friends.add(new FriendWithChat("https://s3-alpha-sig.figma.com/img/9261/dba4/707be4fc468fe699496620292830adad?Expires=1695600000&Signature=NFoM2wIG4tu~zOzeMrRLLMjsyl1YIlkZcqykvdCiDrimnhxI8MazU0VHcLAXw8Ne4-GBFA~UAuoUz~3NVyAWcVvTuCp6j7YGffY6P-YDKy1oxFxERw69TaP-66DC7UTyoCsOZhDTiecildcSyd4F4fTP508AjpTTU0s5lNIOI98-qMKtPkZTxysRD9nTgRuR55cp-k-5wyjl97m4~jMStX3KEzRd46ehy-vOcMbTQsUyFazW7M7Tmz6EJrSzW8VrPGolADk8p6idXE8ySTILtX0tNRWWtFXOKBWJW5W-mP7rJoEhFuP1tkndthx7qh3yEt1VWsWeXRcSOFlvLgWaiw__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Bonelwa","Are You Ready To Buy A Home Theater Audio…"));
        friends.add(new FriendWithChat("https://s3-alpha-sig.figma.com/img/75d2/4e87/1d3808434e5dbd8fee65aacbda0a1170?Expires=1695600000&Signature=R2vrlOuUoL2F~-6UiQGJ~YYNsepwzwau3AJ1Hpi36EKFKeJAV9rQmAQhcWk~GPCf03tZGWiES2zft~8KtG1NmluqJMsNz69mdgbrz4qCjqZs7EVHAunSSeJgOFwghccFJcxckx1YEO-cZT96og8~AHmEoV2YfG46w8lcqHlu41kH1CXqzg4Psk-jIstERP68KpIFuqNuEo~ilwyqc29W2RpscMT9wJ55Zj2cxqy5lVlfgOY0hHHzGLEZ91qMoFvBD1o9jO-alsrdZKXnJgrDHfIR9Xn8MXJkYvsPYqMoKU1GsE4GOJZpLSLLERClc5Q76omCgIl-CZBek2DkphLTbg__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Leonardo Oliveira","29 Motivational Quotes For Business And "));

        mFriends.postValue(friends);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<Friend>> getFriends() {
        return  mFriends;
    }
}
