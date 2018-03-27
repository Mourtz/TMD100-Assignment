package com.example.mourtz.dietcoach;

import android.content.Intent;
import android.os.Debug;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class User extends AppCompatActivity {

    private Button profileButton;
    private Button programButton;
    private Button doctorButton;

    public static UserInfo info;

    public static String GetName(){
        if(User.info.username.equals("undefined")){
            return " (" + User.info.email.split("@")[0] + ")";
        } else {
            return " (" + User.info.username + ")";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        info = new UserInfo();

        // disable all the buttons till we fetch user's info from Firebase
        profileButton = (Button) findViewById(R.id.profileB);
        profileButton.setEnabled(false);
        programButton = (Button) findViewById(R.id.programB);
        programButton.setEnabled(false);
        doctorButton = (Button) findViewById(R.id.become_doctorB);
        doctorButton.setEnabled(false);

        Database.ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot user = dataSnapshot.child(MainActivity.user.getUid());

                if(!user.exists()){
                    Database.UpdateUser();
                } else {
                    info = user.getValue(UserInfo.class);
                }

                // check if hes a admin
                if(info.admin){
                    Intent intent = new Intent(User.this, Admin.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                // check if hes a doctor
                if(info.doctor){
                    setTitle("Doctor"+GetName());
                    programButton.setText("Show Pending Programs");
                } else {
                    setTitle("User"+GetName());
                    if(info.hasProgram){ programButton.setText("Show Weekly Program"); }
                }

                profileButton.setEnabled(true);
                programButton.setEnabled(true);
                doctorButton.setEnabled(!info.doctor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User.this, Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        programButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(!info.doctor){
                    if(!info.hasProgram){
                        Database.ref.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                DataSnapshot program = dataSnapshot.child("program_requests");

                                if(info.kg.equals("undefined") || info.height.equals("undefined") || info.age.equals("undefined")){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(User.this);
                                    builder.setMessage("You have to give your weight, height and age to request a program!")
                                            .setTitle("Warning!")
                                            .setPositiveButton(android.R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                } else {
                                    if(program.getValue() != null){
                                        if(((String)program.getValue()).isEmpty()){
                                            Database.db.getReference("program_requests").setValue(MainActivity.user.getUid());
                                            AlertDialog.Builder builder = new AlertDialog.Builder(User.this);
                                            builder.setMessage("We have received your request!")
                                                    .setTitle("Info!")
                                                    .setPositiveButton(android.R.string.ok, null);
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                        } else {
                                            if(((String)program.getValue()).contains(MainActivity.user.getUid())){
                                                AlertDialog.Builder builder = new AlertDialog.Builder(User.this);
                                                builder.setMessage("You have already requested a weekly program!")
                                                        .setTitle("Reminder!")
                                                        .setPositiveButton(android.R.string.ok, null);
                                                AlertDialog dialog = builder.create();
                                                dialog.show();
                                            } else {
                                                Database.db.getReference("program_requests").setValue(
                                                        (program.getValue() + ",") + MainActivity.user.getUid()
                                                );
                                                AlertDialog.Builder builder = new AlertDialog.Builder(User.this);
                                                builder.setMessage("We have received your request!")
                                                        .setTitle("Info!")
                                                        .setPositiveButton(android.R.string.ok, null);
                                                AlertDialog dialog = builder.create();
                                                dialog.show();
                                            }
                                        }
                                    } else {
                                        Database.db.getReference("program_requests").setValue(MainActivity.user.getUid());
                                        AlertDialog.Builder builder = new AlertDialog.Builder(User.this);
                                        builder.setMessage("We have received your request!")
                                                .setTitle("Info!")
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Intent intent = new Intent(User.this, Program.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else { // if is not a doctor
                    Intent intent = new Intent(User.this, PendingPrograms.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
        });

        doctorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            Database.ref.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataSnapshot dr = dataSnapshot.child("doctor_requests");

                    if(dr.getValue() != null){
                        if(((String) dr.getValue()).isEmpty()){
                            Database.db.getReference("doctor_requests").setValue(MainActivity.user.getUid());
                            AlertDialog.Builder builder = new AlertDialog.Builder(User.this);
                            builder.setMessage("We have received your request!")
                                    .setTitle("Info!")
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            if(((String) dr.getValue()).contains(MainActivity.user.getUid())){
                                AlertDialog.Builder builder = new AlertDialog.Builder(User.this);
                                builder.setMessage("You have already requested to become a doctor!")
                                        .setTitle("Reminder!")
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else {
                                Database.db.getReference("doctor_requests").setValue(
                                        (dr.getValue() + ",") + MainActivity.user.getUid()
                                );
                                AlertDialog.Builder builder = new AlertDialog.Builder(User.this);
                                builder.setMessage("We have received your request!")
                                        .setTitle("Info!")
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    }
                    else {
                        Database.db.getReference("doctor_requests").setValue(MainActivity.user.getUid());
                        AlertDialog.Builder builder = new AlertDialog.Builder(User.this);
                        builder.setMessage("We have received your request!")
                                .setTitle("Info!")
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(User.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
