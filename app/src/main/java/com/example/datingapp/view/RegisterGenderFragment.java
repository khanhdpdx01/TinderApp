package com.example.datingapp.view;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.datingapp.R;
import com.example.datingapp.databinding.FragmentRegisterGenderBinding;
import com.example.datingapp.entity.User;

public class RegisterGenderFragment extends Fragment {
    private User user;
    private FragmentRegisterGenderBinding binding;
    private Button btnMale, btnFemale;
    private boolean isChoice = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_gender, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentRegisterGenderBinding.bind(view);

        btnMale = binding.btnMale;
        btnFemale = binding.btnFemale;

        btnMale.setOnClickListener(view1 -> {
            btnMale.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ffdc")));
            btnFemale.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
            user.setGender(true);
            isChoice = true;
        });

        btnFemale.setOnClickListener(view1 -> {
            btnFemale.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ffdc")));
            btnMale.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
            user.setGender(false);
            isChoice = true;
        });

        binding.btnNext.setOnClickListener(view1 -> {
            if (!isChoice) {
                Toast.makeText(getContext(), "Vui lòng chọn giới tính !!!", Toast.LENGTH_SHORT).show();
            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                Navigation.findNavController(view1).navigate(R.id.action_registerGenderFragment_to_registerHobbyFragment, bundle);
            }
        });

    }
}