package com.example.company.unipiplialert;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener, android.location.LocationListener {

    private final int REQUEST_FINE_LOCATION = 0;
    private final int REQUEST_SEND_SMS = 1;

    private SharedPreferences preferences;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor lightSensor;

    private SmsManager smsManager;

    private LocationManager locationManager;

    private final String log_filepath = "log.txt";

    private boolean dropped;
    private long dropTime;

    private long lightTime;

    private boolean sentSOS = false;

    // append a string to the Log file
    private void aLog(String text) {
        try {
            FileOutputStream fos = openFileOutput(log_filepath, Context.MODE_APPEND);
            fos.write(text.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // display the contents of the Log file
    private void dLog() {
        try {
            FileInputStream fis = openFileInput(log_filepath);
            byte[] data = new byte[1024];
            fis.read(data);
            Log.d("Log File:", new String(data, "UTF-8"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // UI Elements
    private TextView timerTextView;
    private Button abortBtn, sosBtn;
    private ProgressBar progressBar;

    private CountDownTimer countDownTimer = null;

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.abortBtn:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer.onFinish();
                }
                break;
            case R.id.sosBtn:
                sentSOS = !sentSOS;

                String sms_text;
                if (sentSOS) {
                    sosBtn.setText("Abort");

                    @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    sms_text = "SOS! I need help! Im currently at " + location.getLongitude() + ", " + location.getLatitude();
                    Toast.makeText(this, "sending an emergency sms to everybody", Toast.LENGTH_LONG).show();
                } else {
                    sosBtn.setText("SOS");

                    sms_text = "False alarm, everything is fine.";
                    Toast.makeText(this, "sending a false alarm to everybody that you 've already sent SOS sms", Toast.LENGTH_LONG).show();
                }

                String recipients = preferences.getString("recipients", "");
                String[] a_recipients = recipients.split(",");
                for (String a_recipient : a_recipients) {
                    smsManager.sendTextMessage(a_recipient.trim(), null, sms_text, null, null);
                }

                break;
            default:
                break;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // request permissions
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_SEND_SMS);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        smsManager = SmsManager.getDefault();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this, lightSensor , SensorManager.SENSOR_DELAY_NORMAL);

        preferences = this.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("recipients", "6947209900, 6465809900, 6978006004");
        editor.apply();

        // abort button
        abortBtn = findViewById(R.id.abortBtn);
        abortBtn.setOnClickListener(this);
        abortBtn.setVisibility(View.INVISIBLE);
        abortBtn.setEnabled(false);

        // SOS button
        sosBtn = findViewById(R.id.sosBtn);
        sosBtn.setOnClickListener(this);

        // countdownTimer TextView
        timerTextView = findViewById(R.id.textView);
        timerTextView.setText("");

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_FINE_LOCATION) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        } else if(requestCode == REQUEST_SEND_SMS){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SEND_SMS);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Greek:
                abortBtn.setText("ΔΙΑΚΟΠΤΩ");
                if(sentSOS){
                    sosBtn.setText("ΔΙΑΚΟΠΤΩ");
                }
                return true;
            case R.id.English:
                abortBtn.setText("ABORT");
                if(sentSOS){
                    sosBtn.setText("ABORT");
                }
                return true;
            case R.id.German:
                abortBtn.setText("ABGEHEN");
                if(sentSOS){
                    sosBtn.setText("ABGEHEN");
                }
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        switch (mySensor.getType()){
            //----------------------- ACCELEROMETER -----------------------
            case Sensor.TYPE_ACCELEROMETER:
                final float threshold = 16.4f;

                if(!dropped){
                    if(event.values[0] > threshold ||
                        event.values[1] > threshold ||
                        event.values[2] > threshold ){

                        dropped = true;
                        dropTime = System.currentTimeMillis();
                        abortBtn.setVisibility(View.VISIBLE);
                        abortBtn.setEnabled(true);
                        progressBar.setVisibility(View.VISIBLE);

                        Toast.makeText(this, "Did you accidentally drop your phone?", Toast.LENGTH_SHORT).show();
                    }
                } else if(countDownTimer == null){
                    countDownTimer = new CountDownTimer(30000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            int ltime = (int) (millisUntilFinished / 1000);
                            timerTextView.setText(String.valueOf(ltime));

                            MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.beep);
                            mp.start();
                        }

                        public void onFinish() {
                            timerTextView.setText("");
                            dropped = false;
                            countDownTimer = null;

                            abortBtn.setVisibility(View.INVISIBLE);
                            abortBtn.setEnabled(false);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }.start();
                }
                break;

            //----------------------- LIGHT SENSOR -----------------------
            case Sensor.TYPE_LIGHT:
                // append to the Log file every half an hour if the readings are still above the safe threshold we have set.
                if(event.values[0] > SensorManager.LIGHT_SHADE && (lightTime == 0 || System.currentTimeMillis() - lightTime > 3e4)){
                    lightTime = System.currentTimeMillis();

                    Timestamp timestamp = new Timestamp(lightTime);
                    String currentDateTime = timestamp.toString();

                    aLog("Detected increased solar radiation at " + currentDateTime + ", with " + event.values[0] + " luminance\n" );
                    Toast.makeText(this, "Reading increased solar radiation values!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
