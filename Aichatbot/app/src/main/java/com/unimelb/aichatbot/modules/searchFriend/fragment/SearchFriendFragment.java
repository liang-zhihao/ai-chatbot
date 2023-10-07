package com.unimelb.aichatbot.modules.searchFriend.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.unimelb.aichatbot.databinding.FragmentSearchBinding;
import com.unimelb.aichatbot.modules.searchFriend.SearchViewModel;
import com.unimelb.aichatbot.modules.searchFriend.model.Friend;

import java.util.LinkedList;
import java.util.List;

public class SearchFriendFragment extends Fragment {
    class FriendAdapter extends BaseAdapter{
        private LinkedList<Friend> mData;
        private Context mContext;

        public FriendAdapter(LinkedList< Friend> mData, Context mContext) {
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
            return mData.get(position).getView(convertView, parent, mContext);
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

        LinkedList<Friend> mData = new LinkedList<>();
        searchViewModel.getFriends().observe(getViewLifecycleOwner(), new Observer<List<Friend>>() {
            @Override
            public void onChanged(List<Friend> friends) {
                for (Friend f: friends) {
                    mData.add(f);
                }
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
