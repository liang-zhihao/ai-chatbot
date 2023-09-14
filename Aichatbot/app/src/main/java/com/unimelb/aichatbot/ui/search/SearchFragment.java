package com.unimelb.aichatbot.ui.search;

import android.content.Context;
import android.os.Bundle;
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
        searchViewModel.getFriends().observe(getViewLifecycleOwner(), new Observer<SearchViewModel.Friend>() {
            @Override
            public void onChanged(SearchViewModel.Friend friend) {
                mData.add(friend);
            }
        });

        FriendAdapter adapter = new FriendAdapter(mData, this.getContext());
        listView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
