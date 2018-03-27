package com.example.mourtz.dietcoach;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class Program extends AppCompatActivity {

    private static TextView days[] = new TextView[7];
    private TextView noteT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);

        setTitle("Program");

        days[0] = findViewById(R.id.MondayT);
        days[0].setMovementMethod(new ScrollingMovementMethod());

        days[1] = findViewById(R.id.TuesdayT);
        days[1].setMovementMethod(new ScrollingMovementMethod());

        days[2] = findViewById(R.id.WednesdayT);
        days[2].setMovementMethod(new ScrollingMovementMethod());

        days[3] = findViewById(R.id.ThursdayT);
        days[3].setMovementMethod(new ScrollingMovementMethod());

        days[4] = findViewById(R.id.FridayT);
        days[4].setMovementMethod(new ScrollingMovementMethod());

        days[5] = findViewById(R.id.SaturdayT);
        days[5].setMovementMethod(new ScrollingMovementMethod());

        days[6] = findViewById(R.id.SundayT);
        days[6].setMovementMethod(new ScrollingMovementMethod());


        int bmi = Math.round((Float.parseFloat(User.info.kg) / Float.parseFloat(User.info.height)) / Float.parseFloat(User.info.height));
        String diagnosis;
        if(bmi < 18){
            diagnosis = "underweight";
        } else if(bmi < 24){
            diagnosis = "normal";
        } else if(bmi < 24){
            diagnosis = "normal";
        } else if(bmi < 29){
            diagnosis = "overweight";
        } else {
            diagnosis = "obese";
        }

        int t_calories = 0;
        if(User.info.target.equals("lose weight")){
            t_calories = 1800;
        } else if(User.info.target.equals("keep weight")){
            t_calories = 2300;
        } else if(User.info.target.equals("gain weight")){
            t_calories = 3500;
        }
        t_calories -= Math.min(Integer.parseInt(User.info.age)*5, 400);

        Random rand = new Random();
        for(int i = 0; i < 7; ++i){
            int f = rand.nextInt(Food.foods.length);
            int cal = 0;
            String text = "";
            for(;;){
                if(User.info.favouriteFoods.equals("undefined")){
                    text += Food.foods[f].grams + "g of " + Food.foods[f].name + "\n";
                    cal += Food.foods[f].calories;
                    if(cal >= t_calories) break;
                } else {
                    if(User.info.favouriteFoods.contains(Food.foods[f].name)){
                        text += Food.foods[f].grams + "g of " + Food.foods[f].name + "\n";
                        cal += Food.foods[f].calories;
                        if(cal >= t_calories) break;
                    }
                }

                if(++f == Food.foods.length) f = rand.nextInt(Food.foods.length);
            }
            days[i].setText(text + "\nTotal Calories: " + cal);
        }

        noteT = findViewById(R.id.NotesT);
        noteT.setMovementMethod(new ScrollingMovementMethod());
        noteT.setText("You 've been classified as\n" + diagnosis + ".The suggested daily\nintake of calories\nfor you is ~" + t_calories + "cal");
    }

    @Override
    public void onBackPressed() {
        User.info.hasProgram = false;
        Database.UpdateUser();

        Intent intent = new Intent(Program.this, User.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
