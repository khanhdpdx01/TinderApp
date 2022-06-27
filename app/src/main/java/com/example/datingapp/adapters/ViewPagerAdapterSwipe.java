package com.example.datingapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.datingapp.R;
import com.example.datingapp.entity.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ViewPagerAdapterSwipe extends PagerAdapter {
    Context context;
    List<String> userImages;
    User displayUs;
    LayoutInflater inflater;
    public ViewPagerAdapterSwipe(Context context, User displayUs){
        this.context = context;
        this.displayUs = displayUs;
        inflater = LayoutInflater.from(context);
        this.userImages = displayUs.getProfileImages();
    }
    public int getCount(){
        return userImages== null ? 0 : userImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.cart_img, container, false);
        //view
        ImageView images = (ImageView) view.findViewById(R.id.user_image);
        ImageView imgProfile = (ImageView) view.findViewById(R.id.img_profileswipe);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView Age = (TextView) view.findViewById(R.id.age);
        StorageReference storageReference  = FirebaseStorage.getInstance().getReference()
                .child("images/" + userImages.get(position) + ".jpg");
        Log.d("alo1", storageReference.toString());
        storageReference.getDownloadUrl().addOnSuccessListener(uri ->
                Glide.with(context)
                        .load(uri)
                        .into(images));
        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference()
                .child("images/" + userImages.get(0) + ".jpg");
        storageReference1.getDownloadUrl().addOnSuccessListener(uri ->
                Glide.with(context)
                        .load(uri)
                        .into(imgProfile));
        name.setText(displayUs.getName());

        if(displayUs.isGender()){
            Age.setText(String.valueOf(displayUs.getAge()) + " Nam");
        }
        else {
            Age.setText(String.valueOf(displayUs.getAge()) + " Nữ");
        }
        container.addView(view);
        return  view;
    }
}
