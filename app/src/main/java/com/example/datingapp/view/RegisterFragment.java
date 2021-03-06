package com.example.datingapp.view;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.datingapp.R;
import com.example.datingapp.databinding.FragmentRegisterBinding;
import com.example.datingapp.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class RegisterFragment extends Fragment {
    private final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private final Context context = getContext();
    private FragmentRegisterBinding binding;
    private User user;

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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentRegisterBinding.bind(view);


        binding.btnNext.setOnClickListener(view1 -> {
            String email = binding.email.getText().toString();
            String fullname = binding.fullname.getText().toString();
            String password = binding.password.getText().toString();
            String confirmPassword = binding.confirmPassword.getText().toString();
            if (mFirebaseAuth.getCurrentUser() != null) {
                mFirebaseAuth.signOut();
            }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getContext(), "Vui l??ng nh???p email!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Vui l??ng nh???p m???t kh???u!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(fullname)) {
                Toast.makeText(getContext(), "Vui l??ng nh???p h??? v?? t??n!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isEmail(email)) {
                Toast.makeText(getContext(), "Email kh??ng ????ng ?????nh d???ng!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(getContext(), "M???t kh???u qu?? ng???n, nh???p tr??n 6 k?? t???!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(), "M???t kh???u kh??ng tr??ng v???i m???t kh???u x??c nh???n", Toast.LENGTH_SHORT).show();
                return;
            }

            mFirebaseAuth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            boolean isExists = task.getResult().getSignInMethods().isEmpty();
                            if (!isExists) {
                                Toast.makeText(getContext(), "Email ???? t???n t???i!", Toast.LENGTH_SHORT).show();
                                Log.d("DEBUG", "Da ton tai");
                            } else {
                                user = new User();
                                user.setEmail(email);
                                user.setName(fullname);
                                user.setPassword(password);

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("user", user);
                                Navigation.findNavController(view1).navigate(R.id.action_registerFragment_to_registerAgeFragment, bundle);
                            }
                        }
                    });
        });
    }

    private boolean isEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

}