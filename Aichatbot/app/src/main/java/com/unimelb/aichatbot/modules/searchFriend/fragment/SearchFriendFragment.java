package com.unimelb.aichatbot.modules.searchFriend.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unimelb.aichatbot.CustomViewController;
import com.unimelb.aichatbot.databinding.FragmentSearchBinding;
import com.unimelb.aichatbot.modules.chatHistory.HistoryItem;
import com.unimelb.aichatbot.modules.chatroom.activity.MessageActivity;
import com.unimelb.aichatbot.modules.common.model.FriendListItem;
import com.unimelb.aichatbot.modules.searchFriend.SearchViewModel;
import com.unimelb.aichatbot.modules.searchFriend.adapter.FriendAdapter;
import com.unimelb.aichatbot.modules.searchFriend.model.SearchFriendRequest;

/*
 *  todo test: search friend and start a new chat
 * */
public class SearchFriendFragment extends Fragment implements CustomViewController {

    private FragmentSearchBinding binding;
    private FriendAdapter friendAdapter;

    private SearchViewModel searchViewModel;

    private SearchView searchView;

    private RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initializeView();

        // searchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        initializeRecyclerView();
        initializeViewModel();
        setupSearchViewListener();
        return root;
    }

    private void setupSearchViewListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFriends(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void loadFriends() {
        //     TODO api call to get friends

    }

    public void searchFriends(String query) {
        String name = "1";
        name = query;
        SearchFriendRequest request = new SearchFriendRequest(name);
        // TODO api call to search friends and with auth token
        // SearchFriendService apiService = RetrofitFactory.create(SearchFriendService.class);
        // Call<BaseResponse<SearchFriendResponse>> call = apiService.searchFriendByName(request);
        // call.enqueue(new MyCallback<SearchFriendResponse>() {
        //     @Override
        //     public void onSuccess(BaseResponse<SearchFriendResponse> result) {
        //         // TODO show result
        //     }
        //
        //     @Override
        //     public void onError(BaseResponse error, @NonNull Throwable t) {
        //         // TODO show error message
        //     }
        // });

        //    TODO render search result
        searchViewModel.addFriend(new FriendListItem("https://api.horosama.com/random.php", "2123Bonelwa", "Are You Ready To Buy A Home Theater Audio…"));


    }


    public void startChat() {
        // TODO onclick item lister: api request and start chat with friend

        // Intent intent = new Intent(getActivity(), MessageActivity.class);
        // intent.putExtra("room_id", item.getRoomId());
        // intent.putExtra("room_name", item.getLastMessage());
        // startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void initializeView() {
        searchView = binding.searchTextSearch;

    }

    @Override
    public void initializeListener() {

    }

    @Override
    public void initializeActionBar() {

    }

    @Override
    public void initializeViewModel() {
        FriendAdapter.OnItemClickListener onItemClickListener = new FriendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, FriendListItem item) {
                Toast.makeText(getContext(), "Clicked: " + position, Toast.LENGTH_SHORT).show();
                // TODO create a new room and  chat with friend
                // Intent intent = new Intent(getContext(), ChatActivity.class);
                // intent.putExtra("historyItem", historyItem);
                // startActivity(intent);
            }
        };

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.getFriends().observe(getViewLifecycleOwner(), friends -> {
            // When data changes, update the adapter's data set
            friendAdapter = new FriendAdapter(getContext(), friends, onItemClickListener);
            recyclerView.setAdapter(friendAdapter);
        });
    }

    @Override
    public void initializeRecyclerView() {
        recyclerView = binding.recyclerViewSearch; // Updated ID
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
