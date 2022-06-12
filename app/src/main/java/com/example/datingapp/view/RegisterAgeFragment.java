package com.example.datingapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.datingapp.R;
import com.example.datingapp.databinding.FragmentRegisterAgeBinding;
import com.example.datingapp.entity.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterAgeFragment extends Fragment {
    private final int ageLimit = 13;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
    private User user;
    private FragmentRegisterAgeBinding binding;
    private DatePicker datePicker;

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
        return inflater.inflate(R.layout.fragment_register_age, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterAgeBinding.bind(view);
        datePicker = binding.ageSelectionPicker;

        binding.btnNext.setOnClickListener(view1 -> {
            int age = getAge(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

            if (age > ageLimit) {
                // code for converting date to string
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, datePicker.getYear());
                cal.set(Calendar.MONTH, datePicker.getMonth());
                cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                Date dateOfBirth = cal.getTime();
                String strDateOfBirth = dateFormatter.format(dateOfBirth);

                user.setDateOfBirth(strDateOfBirth);

                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                Navigation.findNavController(view1).navigate(R.id.action_registerAgeFragment_to_registerGenderFragment, bundle);
            } else {
                Toast.makeText(getContext(), "Tuổi của bạn phải lớn hơn " + ageLimit + " !!!", Toast.LENGTH_SHORT).show();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private int getAge(int year, int month, int day) {
        Calendar dateOfBirth = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dateOfBirth.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dateOfBirth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }
}