package com.example.datingapp.entity;

import java.io.Serializable;


public class Message implements Serializable {
    private String id;
    private String name;
    private String content;
    private int count;
    private String picture;

    public Message() {
    }

    public Message(String id, String name, String content, int count, String picture) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.count = count;
        this.picture = picture;
    }

//    @BindingAdapter({"android:picture"})
//    public static void loadImage(CircleImageView view, String imageUrl) {
//        Picasso.get().load(imageUrl)
//                .placeholder(R.drawable.ic_baseline_image_24)
//                .error(R.drawable.ic_baseline_error_24)
//                .fit()
//                .into(view);
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
