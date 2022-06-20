package com.example.datingapp.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.datingapp.R;
import com.example.datingapp.adapters.ChatAdapter;
import com.example.datingapp.databinding.FragmentChatPrivateBinding;
import com.example.datingapp.entity.Chat;
import com.example.datingapp.entity.Like;
import com.example.datingapp.entity.Message;
import com.example.datingapp.enumurator.TypeMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatPrivateFragment extends Fragment {

    private FragmentChatPrivateBinding binding;
    private List<Chat> chatList;
    private ChatAdapter chatAdapter;
    private String currentUserId;
    private Like user;
    private DatabaseReference mDatabaseUser, mDatabaseChat;

    public ChatPrivateFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ChatPrivateFragment newInstance(String param1, String param2) {
        ChatPrivateFragment fragment = new ChatPrivateFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (getArguments() != null) {
            user = (Like) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_private, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatPrivateBinding.bind(view);

        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("chats");

        if (user != null) {
            mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("users")
                    .child(currentUserId).child("connections").child("matches")
                    .child(user.getId().trim()).child("ChatId");

            binding.textName.setText(user.getName());
            StorageReference storageReference  = FirebaseStorage.getInstance().getReference()
                    .child("images/" + user.getPicture() + ".jpg");

            storageReference.getDownloadUrl().addOnSuccessListener(uri ->
                    Glide.with(getContext())
                            .load(uri)
                            .placeholder(R.drawable.ic_baseline_image_24)
                            .error(R.drawable.ic_baseline_error_24)
                            .into(binding.imageProfile));

            getChatId();
        } else {
            Toast.makeText(getContext(), "Chat failure !!!", Toast.LENGTH_SHORT).show();
            return;
        }


        chatList = new ArrayList<>();
        chatAdapter = new ChatAdapter(getContext(), chatList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        binding.recyclerViewMessages.setLayoutManager(layoutManager);
        binding.recyclerViewMessages.setAdapter(chatAdapter);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Navigation.findNavController(view).navigate(R.id.chatFragment, bundle);
            }
        });

        binding.edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.edMessage.getText().toString().trim())) {
                    binding.btnSend.setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_baseline_keyboard_voice_24,
                                    getActivity().getTheme()));
                } else {
                    binding.btnSend.setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_baseline_send_24,
                                    getActivity().getTheme()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        initBtnClick();
    }

    private void initBtnClick(){
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(binding.edMessage.getText().toString().trim())) {
                    sendTextMessage(binding.edMessage.getText().toString().trim());
                }

                binding.edMessage.setText(null);
            }
        });
    }

    private void sendTextMessage(String text) {
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String today = format.format(date);

        Calendar currentDateTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        String currentTime = df.format(currentDateTime);

        DatabaseReference newMessageDb = mDatabaseChat.push();

        Chat chat = new Chat(text, currentUserId, user.getId(), TypeMessage.TEXT,
                today + " " + currentTime, "");

        newMessageDb.setValue(chat);
    }

    private void getChatId() {
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String chatId = dataSnapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getChatMessages(chatId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getChatMessages(String chatId) {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    if (dataSnapshot.exists()) {
                        Chat chat = dataSnapshot.getValue(Chat.class);
                        chatList.add(chat);
                        chatAdapter.setChatList(chatList);
                        chatAdapter.notifyDataSetChanged();
                    }
                }

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}