package com.example.datingapp.view;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.datingapp.R;
import com.example.datingapp.Utils;
import com.example.datingapp.adapters.ViewPagerAdapterSwipe;
import com.example.datingapp.databinding.FragmentSwipeBinding;
import com.example.datingapp.entity.User;
import com.example.datingapp.utils.DepthPageTransformer;
import com.example.datingapp.utils.IFireBaseLoadDone;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class SwipeFragment extends Fragment implements IFireBaseLoadDone {
    private View rootLayout;
    ViewPager viewPaper;
    ViewPagerAdapterSwipe adapter;
    DatabaseReference users;
    DatabaseReference userCurrentRef;
    FragmentSwipeBinding binding;
    FloatingActionButton back, skip, like;
    User displayUser;
    String currentUserID;
    User currentUser;
    List<String> unLikeUs = new ArrayList<>();
    public SwipeFragment() {
        // Required empty public constructor
    }

    IFireBaseLoadDone iFireBaseLoadDone;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout = inflater.inflate(R.layout.fragment_swipe, container, false);
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userCurrentRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserID);
        userCurrentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser =  dataSnapshot.getValue(User.class);
                currentUser.setUserId(dataSnapshot.getKey());
                for (DataSnapshot e : dataSnapshot.child("connections")
                        .child("unLike").getChildren()) {
                    unLikeUs.add(e.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFireBaseLoadDone.onFirebaseLoadFailure(databaseError.getMessage());
            }
        });
        return rootLayout;
    }

    private void loadUser() {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            List<User> userList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){

                    User user1 =  userSnapshot.getValue(User.class);
                    user1.setUserId(userSnapshot.getKey());
                    if(user1.isGender() != currentUser.isGender() && user1.getUserId() != currentUserID && ( unLikeUs == null || !unLikeUs.contains(user1.getUserId()))){
                        userList.add(user1);
                    }

                }
                if(unLikeUs != null){
                    for(int i=0; i< unLikeUs.size(); i++){
                        Log.d("unls", unLikeUs.get(i));
                    }
                }
                if(userList.size() >0 ){
                    Random rand = new Random();
                    int index = rand.nextInt(userList.size());
                    iFireBaseLoadDone.onFirebaseLoadSuccess(userList.get(index));
                    displayUser = userList.get(index);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFireBaseLoadDone.onFirebaseLoadFailure(databaseError.getMessage());
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSwipeBinding.bind(view);
        users = FirebaseDatabase.getInstance().getReference("users");
        iFireBaseLoadDone = this;
        loadUser();
        viewPaper = binding.viewPagerMain;
        viewPaper.setPageTransformer(true,new DepthPageTransformer());
        skip = binding.fabSkip;
        like = binding.fabLike;
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(displayUser == null){
                    Toast.makeText(getContext(), "Quẹt hết rồi!", Toast.LENGTH_SHORT).show();
                }
                else {
                    onSkip();
                }

            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(displayUser == null){
                    Toast.makeText(getContext(), "Quẹt hết rồi!", Toast.LENGTH_SHORT).show();
                }
                else {
                    onLike();
                }
            }
        });
    }
    public void onSkip(){
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(this.currentUserID).child("connections").child("unLike").push().setValue(displayUser.getUserId());
        unLikeUs.add(displayUser.getUserId());
        loadUser();
    }
    public void onLike(){
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(this.currentUserID).child("connections").child("like").push().setValue(displayUser.getUserId());
        DatabaseReference checkStatus = FirebaseDatabase.getInstance().getReference().child("users")
                .child(displayUser.getUserId()).child("connections").child("like");
        DatabaseReference currentUS = FirebaseDatabase.getInstance().getReference().child("users")
                .child(this.currentUserID).child("connections").child("matches");
        DatabaseReference userMatch = FirebaseDatabase.getInstance().getReference().child("users")
                .child(displayUser.getUserId()).child("connections").child("matches");

        checkStatus.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        for(DataSnapshot likeSnapshot:dataSnapshot.getChildren()){
                            if(likeSnapshot.getValue(String.class).equals(currentUserID)){
                                UUID uuid = UUID.randomUUID();
                                currentUS.child(displayUser.getUserId()).child("chat_id").setValue(uuid.toString());
                                userMatch.child(currentUserID).child("chat_id").setValue(uuid.toString());
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
        loadUser();
    }


    private void animateFab(final FloatingActionButton fab){
        fab.animate().scaleX(0.7f).setDuration(100).withEndAction(() -> fab.animate().scaleX(1f).scaleY(1f));
    }

    @Override
    public void onFirebaseLoadSuccess(User user) {
        adapter = new ViewPagerAdapterSwipe(this.getContext(), user);
        viewPaper.setAdapter(adapter);
    }
    @Override
    public void onFirebaseLoadFailure(String err) {
        Toast.makeText(this.getContext(), ""+err, Toast.LENGTH_SHORT).show();
    }
}