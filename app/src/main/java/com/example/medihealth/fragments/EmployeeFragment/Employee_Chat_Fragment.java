package com.example.medihealth.fragments.EmployeeFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihealth.R;
import com.example.medihealth.adapters.chat.RecentChatRecyclerAdapter;
import com.example.medihealth.models.ChatRoom;
import com.example.medihealth.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class Employee_Chat_Fragment extends Fragment {
    RecyclerView recyclerView;
    RecentChatRecyclerAdapter adapter;

    public Employee_Chat_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View iteamView = inflater.inflate(R.layout.fragment_employee__chat_, container, false);
        recyclerView = iteamView.findViewById(R.id.recyler_view);
        setupRecyclerView();
        return iteamView;
    }

    private void setupRecyclerView() {
        Query query = FirebaseUtil.allChatroomCollectionReference()
                .whereArrayContains("userIds", FirebaseUtil.currentUserId())
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatRoom> options = new FirestoreRecyclerOptions.Builder<ChatRoom>()
                .setQuery(query, ChatRoom.class).build();

        adapter = new RecentChatRecyclerAdapter(options, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
}