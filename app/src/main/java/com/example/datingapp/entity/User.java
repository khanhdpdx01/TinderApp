package com.example.datingapp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class User implements Serializable {
    private  String userId;
    private String email;
    private String name;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public ArrayList<String> getProfileImages() {
        return profileImages;
    }

    public void setProfileImages(ArrayList<String> profileImages) {
        this.profileImages = profileImages;
    }
    public int getAge() {
        Calendar dateOfBirth = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        String[] str = this.dateOfBirth.split("-");
        dateOfBirth.set(Integer.parseInt(str[2]), Integer.parseInt(str[0]), Integer.parseInt(str[1]));
        int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dateOfBirth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }
}
