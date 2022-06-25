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
import android.widget.Toast;

import com.example.datingapp.R;
import com.example.datingapp.Utils;
import com.example.datingapp.adapters.ViewPagerAdapterSwipe;
import com.example.datingapp.databinding.FragmentSwipeBinding;
import com.example.datingapp.entity.User;
import com.example.datingapp.utils.DepthPageTransformer;
import com.example.datingapp.utils.IFireBaseLoadDone;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SwipeFragment extends Fragment implements IFireBaseLoadDone {
    private View rootLayout;
    ViewPager viewPaper;
    ViewPagerAdapterSwipe adapter;
    DatabaseReference users;
    FragmentSwipeBinding binding;

    public SwipeFragment() {
        // Required empty public constructor
    }

    IFireBaseLoadDone iFireBaseLoadDone;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout = inflater.inflate(R.layout.fragment_swipe, container, false);

        return rootLayout;
    }

    private void loadUser() {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            List<User> userList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){
                    userList.add(userSnapshot.getValue(User.class));
                    userList.forEach(
                            user-> {
                                Log.d("User1", user.getProfileImages().get(0));
                            }
                    );
                }
                iFireBaseLoadDone.onFirebaseLoadSuccess(userList.get(0).getProfileImages());
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


        int bottomMargin = Utils.dpToPx(100);
        Point windowSize = Utils.getDisplaySize(getActivity().getWindowManager());
    }


    private void animateFab(final FloatingActionButton fab){
        fab.animate().scaleX(0.7f).setDuration(100).withEndAction(() -> fab.animate().scaleX(1f).scaleY(1f));
    }

    @Override
    public void onFirebaseLoadSuccess(List<String> userImages) {
        adapter = new ViewPagerAdapterSwipe(this.getContext(), userImages);
        viewPaper.setAdapter(adapter);
    }
    @Override
    public void onFirebaseLoadFailure(String err) {
        Toast.makeText(this.getContext(), ""+err, Toast.LENGTH_SHORT).show();
    }
}