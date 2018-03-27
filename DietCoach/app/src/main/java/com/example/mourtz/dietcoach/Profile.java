package com.example.mourtz.dietcoach;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabWidget;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    private MultiAutoCompleteTextView favouriteFoods;
    private TextView username, email, height, kg, age;
    private Button submitButton;

    private void updateTextFields(){
        if(!User.info.username.equals("undefined"))         username.setText(User.info.username);
        if(!User.info.email.equals("undefined"))            email.setText(User.info.email);

        if(!User.info.age.equals("undefined"))              age.setText(String.valueOf(User.info.age));
        if(!User.info.height.equals("undefined"))           height.setText(String.valueOf(User.info.height));
        if(!User.info.kg.equals("undefined"))               kg.setText(User.info.kg);

        if(!User.info.favouriteFoods.equals("undefined"))   favouriteFoods.setText(User.info.favouriteFoods);

        if(User.info.target.equals("lose weight")){
            ((RadioGroup) findViewById(R.id.rGroup)).check(R.id.loseR);
        } else if(User.info.target.equals("keep weight")){
            ((RadioGroup) findViewById(R.id.rGroup)).check(R.id.keepR);
        } else if(User.info.target.equals("gain weight")){
            ((RadioGroup) findViewById(R.id.rGroup)).check(R.id.gainR);
        } else {
            Log.e("Fatal Error", "Something went terribly wrong");
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.loseR:
                User.info.target = "lose weight";
                break;
            case R.id.keepR:
                User.info.target = "keep weight";
                break;
            case R.id.gainR:
                User.info.target = "gain weight";
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setTitle("Profile");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,Database.food_names);

        username = findViewById(R.id.usernameT);
        email = findViewById(R.id.emailT);
        age = findViewById(R.id.ageT);
        height = findViewById(R.id.heightT);
        kg = findViewById(R.id.kgT);
        favouriteFoods = this.findViewById(R.id.favourite_foodsT);
        updateTextFields();

        favouriteFoods.setAdapter(adapter);
        favouriteFoods.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        favouriteFoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favouriteFoods.showDropDown();
            }
        });

        submitButton = findViewById(R.id.submitB);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!String.valueOf(username.getText()).isEmpty())       User.info.username = String.valueOf(username.getText());
                if(!String.valueOf(email.getText()).isEmpty())          User.info.email = String.valueOf(email.getText());

                if(!String.valueOf(age.getText()).isEmpty())            User.info.age = String.valueOf(age.getText());
                if(!String.valueOf(height.getText()).isEmpty())         User.info.height = String.valueOf(height.getText());
                if(!String.valueOf(kg.getText()).isEmpty())             User.info.kg = String.valueOf(kg.getText());

                if(!String.valueOf(favouriteFoods.getText()).isEmpty()) User.info.favouriteFoods = String.valueOf(favouriteFoods.getText());

                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                builder.setMessage("Updated your profile!")
                        .setTitle("Info!")
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
                Database.UpdateUser();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Profile.this, User.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
