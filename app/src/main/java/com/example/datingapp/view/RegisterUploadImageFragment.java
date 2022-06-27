package com.example.datingapp.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.datingapp.R;
import com.example.datingapp.entity.User;
import com.example.datingapp.utils.BitmapScaler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterUploadImageFragment extends Fragment {
    private final int NUMBER_OF_IMAGE = 6;
    private final ImageView[] imagesViews = new ImageView[NUMBER_OF_IMAGE];
    private final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private final Map<Integer, Bitmap> imageHasChoice = new HashMap<>();
    private FirebaseUser firebaseUser = null;
    private ImageView imageView;
    private User user;
    private int numberOfImageHasChoice = 0, currentImageChoice = 0;

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
        return inflater.inflate(R.layout.fragment_register_upload_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (int i = 1; i <= NUMBER_OF_IMAGE; ++i) {
            int idImgView = getResources().getIdentifier("image_view_" + i, "id", getContext().getPackageName());
            View eventView = view.findViewById(idImgView);

            int pos = i;
            eventView.setOnClickListener(view1 -> {
                imageView = (ImageView) view1;
                // luu vi tri image chon hien tai
                currentImageChoice = pos;
                proceedChooseImage();
            });
        }

        view.findViewById(R.id.btn_register).setOnClickListener(view1 -> {
            if (numberOfImageHasChoice < 2) {
                Toast.makeText(getContext(), "Chọn tối thiểu hai bức ảnh !!!", Toast.LENGTH_SHORT).show();
            } else {
                ArrayList<String> profileImageNames = new ArrayList<>();

                String userId = registerAccountWithEmail(user.getEmail(), user.getPassword(), view1);
//                Toast.makeText(getContext(), userId, Toast.LENGTH_SHORT).show();
                // luu thong tin user vao realtime database
                saveUser(userId);
                // upload anh len cloud storage
                imageHasChoice.forEach((key, value) -> {
                    String imageName = userId + "_" + key.toString();
                    uploadImage(imageName, value, getContext());
                    profileImageNames.add(imageName);
                });
                // cap nhat thong tin image cho user
                updateImageForUser(userId, profileImageNames);
                Navigation.findNavController(view).navigate(R.id.loginFragment);
            }
        });

    }

    private void proceedChooseImage() {
        String[] colors = {"Take photo", "Choose photo"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pick photo");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            Bitmap bitmap = null;
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                bitmap = (Bitmap) data.getExtras().get("data");

            } else {
                Uri selectedImage = data.getData();
                ImageView image = new ImageView(getContext());
                image.setImageURI(selectedImage);

                bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
            }
            // scale image ma khong lam hay doi ti le anh
            Bitmap bMapScaled = BitmapScaler.scaleToFitWidth(bitmap, imageView.getWidth());
            bMapScaled = BitmapScaler.scaleToFitHeight(bMapScaled, imageView.getHeight());

            imageView.setImageBitmap(bMapScaled);

            numberOfImageHasChoice++;
            imageHasChoice.put(currentImageChoice, bMapScaled);
        }
    }

    private String registerAccountWithEmail(String email, String password, View view) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
                        Log.d("DEBUG", user.toString());
                    }
                });

        firebaseUser = mFirebaseAuth.getCurrentUser();
        while (firebaseUser == null) {
            firebaseUser = mFirebaseAuth.getCurrentUser();
        }
//        mFirebaseAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("DEBUG", "signInWithEmail:success");
//                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.d("DEBUG", "FAILD");
//                        }
//                    }
//                });
        return firebaseUser.getUid();
    }

    private String saveUser(String userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");

//        String userId = UUID.randomUUID().toString();
        Map<String, Object> users = new HashMap<>();
        users.put("gender", user.isGender());
        users.put("dateOfBirth", user.getDateOfBirth());
        users.put("hobbies", user.getHobbies());
        users.put("name", user.getName());

        Log.d("DEBUG", ref.getRef().toString());
        ref.child(userId).setValue(users);

        return userId;
    }

    private void uploadImage(String imageName, Bitmap bitmap, Context context) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference imagesRef = storageRef.child("images");
        StorageReference ref = storageRef.child("images/" + imageName + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "Tải ảnh lên thất bại !!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }

    private void updateImageForUser(String userId, ArrayList<String> images) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");

        Map<String, Object> users = new HashMap<>();
        users.put(userId + "/profileImages", images);

        ref.updateChildren(users);
    }
}