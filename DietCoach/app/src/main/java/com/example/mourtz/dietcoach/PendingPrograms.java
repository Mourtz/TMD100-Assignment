package com.example.mourtz.dietcoach;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class PendingPrograms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_programs);

        setTitle("Pending Programs");

        final TableLayout layout = findViewById(R.id.tableL);

        Database.ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot program = dataSnapshot.child("program_requests");

                String val = (String) program.getValue();

                if(val != null && !val.isEmpty()){
                    final String[] requests = val.split(",");

                    for(int i = 0; i < requests.length; ++i){
                        final TableRow tr = new TableRow(PendingPrograms.this);
                        final int finalI = i;

                        if(requests[finalI].isEmpty()) continue;

                        //-------------------- User's Id Text View --------------------
                        TextView tv = new TextView(PendingPrograms.this);
                        tv.setText(requests[i]);
                        tr.addView(tv);

                        //-------------------- Accept Button --------------------
                        Button abt = new Button(PendingPrograms.this);
                        abt.setText("Accept");
                        abt.setBackgroundTintList(ContextCompat.getColorStateList(PendingPrograms.this, R.color.green));
                        abt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Database.db.getReference(requests[finalI]).child("hasProgram").setValue(true);
                                String new_requests = "";
                                for(int z = 0; z < requests.length; ++z){
                                    if(z != finalI){
                                        if(z != 0 && !new_requests.isEmpty()) new_requests += ",";
                                        new_requests += requests[z];
                                    }
                                }
                                Database.db.getReference("program_requests").setValue(new_requests);
                                layout.removeView(tr);
                            }
                        });
                        tr.addView(abt);

                        //-------------------- Decline Button --------------------
                        Button dbt = new Button(PendingPrograms.this);
                        dbt.setText("Decline");
                        dbt.setBackgroundTintList(ContextCompat.getColorStateList(PendingPrograms.this, R.color.red));
                        dbt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String new_requests = "";
                                for(int z = 0; z < requests.length; ++z){
                                    if(z != finalI){
                                        if(z != 0 && !new_requests.isEmpty()) new_requests += ",";
                                        new_requests += requests[z];
                                    }
                                }
                                Database.db.getReference("program_requests").setValue(new_requests);
                                layout.removeView(tr);
                            }
                        });
                        tr.addView(dbt);

                        // append TableRow to the current Activity
                        layout.addView(tr);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PendingPrograms.this, User.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
