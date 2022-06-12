package com.example.datingapp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class User implements Serializable {
    private String email;
    private String password;
    private boolean gender;
    private String dateOfBirth;
    private ArrayList<String> hobbies;
    private ArrayList<String> profileImages;

    public User() {
    }

    public User(boolean gender, String dateOfBirth, ArrayList<String> hobbies) {
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.hobbies = hobbies;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public ArrayList<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(ArrayList<String> hobbies) {
        this.hobbies = hobbies;
    }
}
