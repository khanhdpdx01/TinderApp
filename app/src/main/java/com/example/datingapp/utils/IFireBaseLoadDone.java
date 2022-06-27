package com.example.datingapp.utils;

import com.example.datingapp.entity.User;

import java.util.List;

public interface IFireBaseLoadDone {
    void onFirebaseLoadSuccess(User user);
    void onFirebaseLoadFailure(String err);
}
