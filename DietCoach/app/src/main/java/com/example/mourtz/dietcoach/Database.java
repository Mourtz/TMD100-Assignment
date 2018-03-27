package com.example.mourtz.dietcoach;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {

    public static FirebaseDatabase db;
    public static DatabaseReference ref;

    public static final String[] food_names = new String[Food.foods.length];

    public Database(){
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();

        for(int i = 0; i < Food.foods.length; ++i){
            food_names[i] = Food.foods[i].name;
        }
    }

    public static void updateVal(String s, String v) {
        db.getReference(s).setValue(v);
    }

    public static void UpdateUser(){
        db.getReference(MainActivity.user.getUid()).setValue(User.info);
    }
}
