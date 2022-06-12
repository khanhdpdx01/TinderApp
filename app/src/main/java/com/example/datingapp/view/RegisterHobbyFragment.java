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
import com.example.datingapp.entity.User;

import java.util.ArrayList;

public class RegisterHobbyFragment extends Fragment {
    private final int NUMBER_OF_HOBBY = 5;
    private final String[] hobbies = {"Thể thao", "Du lịch", "Âm nhạc", "Nghệ thuật", "Chơi game"};
    private final ArrayList<String> myHobbies = new ArrayList<>();
    private User user;

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
        return inflater.inflate(R.layout.fragment_register_hobby, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (int i = 1; i <= NUMBER_OF_HOBBY; ++i) {
            int idBtn = getResources().getIdentifier("btn_hobby_" + i, "id", getContext().getPackageName());
            View eventView = view.findViewById(idBtn);
            String hobby = hobbies[i - 1];

            eventView.setOnClickListener(view1 -> {
                Button button = (Button) view1;
                // enable button and else
                if (button.getBackgroundTintList() == null ||
                        button.getBackgroundTintList().getDefaultColor() == Color.parseColor("#ffffff")) {
                    button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ffdc")));
                    myHobbies.add(hobby);
                } else {
                    button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                    myHobbies.remove(hobby);
                }

            });
        }

        view.findViewById(R.id.btn_next).setOnClickListener(view1 -> {
            if (myHobbies.size() < 1) {
                Toast.makeText(getContext(), "Vui lòng chọn ít nhất một sở thích !!!", Toast.LENGTH_SHORT).show();
            } else {
                user.setHobbies(myHobbies);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                Navigation.findNavController(view1).navigate(R.id.action_registerHobbyFragment_to_registerUploadImageFragment, bundle);
            }
        });
    }
}