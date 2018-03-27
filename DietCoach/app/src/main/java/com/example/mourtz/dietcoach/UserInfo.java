package com.example.mourtz.dietcoach;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserInfo {
    Boolean admin;
    Boolean doctor;
    Boolean hasProgram;
    String username;
    String email;

    String kg;
    String height;
    String age;

    String favouriteFoods;
    String target;

    UserInfo(){
        admin = hasProgram = doctor = false;
        email = MainActivity.user.getEmail();
        age = username = favouriteFoods = kg = height = "undefined";
        target = "lose weight";
    }

    @Exclude
    public void trim(){
        username.trim();
        email.trim();
        kg.trim();
        height.trim();
        favouriteFoods.trim();
        target.trim();
    }
}
