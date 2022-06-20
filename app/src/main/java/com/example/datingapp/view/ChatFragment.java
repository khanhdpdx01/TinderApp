package com.example.datingapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.datingapp.R;
import com.example.datingapp.adapters.LikesAdapter;
import com.example.datingapp.adapters.MessagesAdapter;
import com.example.datingapp.databinding.FragmentChatBinding;
import com.example.datingapp.entity.Like;
import com.example.datingapp.entity.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;
    private MessagesAdapter messagesAdapter;
    private LikesAdapter contactsAdapter;
    private List<Message> messageList;
    private List<Like> likeList;
    private String currentUserID;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatBinding.bind(view);

        messageList = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(getContext(), messageList);

        likeList = new ArrayList<>();
        contactsAdapter = new LikesAdapter(getContext(), likeList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerViewMessages.setLayoutManager(layoutManager);
        binding.recyclerViewMessages.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewMessages.setAdapter(messagesAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerViewLikes.setLayoutManager(linearLayoutManager);
        binding.recyclerViewLikes.setAdapter(contactsAdapter);

        getUserMatchId();
    }

    // retrieve data user through key
    private void fetchMatchInformation(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance()
                .getReference().child("users");

        userDb.child(key.trim()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                Log.d("debug", "come to fetchMatchUser");
                String id = dataSnapshot.getKey();
                String name = "";
                String picture = "";
                if(dataSnapshot.child("name").getValue() != null){
                    name = dataSnapshot.child("name").getValue().toString();
                }

                Iterator i = dataSnapshot.child("profileImages").getChildren().iterator();
                if (i.hasNext()) {
                    DataSnapshot snapshot = (DataSnapshot) i.next();
                    picture = snapshot.getValue(String.class);
                }

                Log.d("mga.fetchUser", name);

                Like like = new Like(id, name, picture);
                likeList.add(like);
                contactsAdapter.setLikeList(likeList);
                contactsAdapter.notifyDataSetChanged();
            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUserMatchId() {
        DatabaseReference likeDb = FirebaseDatabase.getInstance().getReference().child("users")
                .child(this.currentUserID).child("connections").child("matches");

        likeDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot match : dataSnapshot.getChildren()){
                        fetchMatchInformation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}