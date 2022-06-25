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

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.datingapp.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ViewPagerAdapterSwipe extends PagerAdapter {
    Context context;
    List<String> userImages;
    LayoutInflater inflater;
    public ViewPagerAdapterSwipe(Context context, List<String> userImages){
        this.context = context;
        this.userImages = userImages;
        inflater = LayoutInflater.from(context);
    }
    public int getCount(){
        return  userImages.size();
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
        Log.d("Userima", userImages.get(position));
        Log.d("alo", images.toString());
        StorageReference storageReference  = FirebaseStorage.getInstance().getReference()
                .child("images/" + userImages.get(position) + ".jpg");
        Log.d("alo1", storageReference.toString());
        storageReference.getDownloadUrl().addOnSuccessListener(uri ->
                Glide.with(context)
                        .load(uri)
                        .into(images));
        container.addView(view);
        return  view;
    }
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
