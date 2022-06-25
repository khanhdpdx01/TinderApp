package com.example.datingapp.utils;

import com.example.datingapp.entity.User;

import java.util.List;

public interface IFireBaseLoadDone {
    void onFirebaseLoadSuccess(List<String> userImages);
    void onFirebaseLoadFailure(String err);
}
