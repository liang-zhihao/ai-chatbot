package com.unimelb.aichatbot.ui.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.unimelb.aichatbot.MainActivity;
import com.unimelb.aichatbot.R;
import com.unimelb.aichatbot.databinding.FragmentSearchBinding;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class SearchFragment extends Fragment {
    class FriendAdapter extends BaseAdapter{
        private LinkedList<SearchViewModel.Friend> mData;
        private Context mContext;

        public FriendAdapter(LinkedList<SearchViewModel.Friend> mData, Context mContext) {
            this.mData = mData;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.row_item_friend,parent,false);

            ImageView avatar = (ImageView)convertView.findViewById(R.id.image_avatar);
            Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            Bitmap bmp = (Bitmap) msg.obj;
                            avatar.setImageBitmap(bmp);
                            break;
                    }
                }
            };

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bmp = getURLImage(mData.get(position).getAvatarUrl());
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = bmp;
                    handler.sendMessage(msg);
                }
            }).start();

            TextView name = (TextView)convertView.findViewById(R.id.text_name);
            name.setText(mData.get(position).getName());

            return convertView;
        }
    }

    private FragmentSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.titleSearch;
        searchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Context mContext = this.getContext();
        final ListView listView = binding.listViewSearch;

        LinkedList<SearchViewModel.Friend> mData = new LinkedList<>();
        searchViewModel.getFriends().observe(getViewLifecycleOwner(), new Observer<List<SearchViewModel.Friend>>() {
            @Override
            public void onChanged(List<SearchViewModel.Friend> friends) {
                for (SearchViewModel.Friend f: friends) {
                    mData.add(f);
                }
            }
        });

        FriendAdapter adapter = new FriendAdapter(mData, this.getContext());
        listView.setAdapter(adapter);

        return root;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
