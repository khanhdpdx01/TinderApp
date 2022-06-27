package com.example.datingapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.datingapp.MainActivity;
import com.example.datingapp.R;
import com.example.datingapp.databinding.FragmentAccountBinding;
import com.example.datingapp.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.stream.Collectors;

public class AccountFragment extends Fragment {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FragmentAccountBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentAccountBinding.bind(view);

        binding.btnLogout.setOnClickListener(view1 -> {
            if (firebaseAuth.getCurrentUser() != null) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        Query query = FirebaseDatabase.getInstance()
                .getReference().child("users").child(firebaseAuth.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    Log.d("DEBUG", user.getName());

                    String imageLink = user.getProfileImages().get(0);

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                            .child("images/" + imageLink + ".jpg");

                    storageReference.getDownloadUrl().addOnSuccessListener(uri ->
                            Glide.with(getContext())
                                    .load(uri)
                                    .into(binding.ivProfile));

                    binding.profileName.setText(user.getName());
                    binding.age.setText("" + user.getAge());
                    binding.gender.setText((user.isGender() ? "Nam" : "Nữ"));
                    binding.interestsBeforematch.setText(user.getHobbies().stream().collect(Collectors.joining()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
