package com.unimelb.aichatbot.modules.contacts.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.unimelb.aichatbot.CustomViewController;
import com.unimelb.aichatbot.databinding.FragmentContactBinding;
import com.unimelb.aichatbot.modules.contacts.ContactAdapter;
import com.unimelb.aichatbot.modules.contacts.ContactViewModel;

/*
 * todo existing  friends and start a new chat or existing chat
 * */
public class ContactFragment extends Fragment implements CustomViewController {

    private FragmentContactBinding binding;
    private ContactAdapter contactAdapter;

    private RecyclerView recyclerView;
    private ContactViewModel contactViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentContactBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initializeRecyclerView();
        initializeViewModel();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // binding = null;
    }

    @Override
    public void initializeView() {

    }

    @Override
    public void initializeListener() {

    }

    @Override
    public void initializeActionBar() {

    }

    @Override
    public void initializeViewModel() {
        ContactAdapter.OnItemClickListener listener = (position, item) -> {
            Toast.makeText(getContext(), "Clicked " + item.getName(), Toast.LENGTH_SHORT).show();
            //     TODO start a message activity, load recent chat or start a new chat
        };


        contactViewModel =
                new ViewModelProvider(this).get(ContactViewModel.class);
        contactViewModel.getFriends().observe(getViewLifecycleOwner(), friends -> {
            // When data changes, update the adapter's data set
            contactAdapter = new ContactAdapter(getContext(), friends, listener);
            recyclerView.setAdapter(contactAdapter);
        });
    }

    @Override
    public void initializeRecyclerView() {
        recyclerView = binding.recyclerViewContacts; // Updated ID
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}