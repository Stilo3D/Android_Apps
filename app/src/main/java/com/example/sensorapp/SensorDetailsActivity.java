package com.example.sensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {

    public static final String EXTRA_SENSOR_TYPE_PARAMETER = "EXTRA_SENSOR_TYPE";

    private SensorManager sensorManager;
    private Sensor sensorLight;
    private TextView sensorLightTextView, sensorLightValueView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        int sensorType = getIntent().getIntExtra(EXTRA_SENSOR_TYPE_PARAMETER, Sensor.TYPE_LIGHT);
        sensorLightTextView = findViewById(R.id.name_details);
        sensorLightValueView = findViewById(R.id.values_details);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorLight = sensorManager.getDefaultSensor(sensorType);

        if (sensorLight == null) {
            sensorLightTextView.setText(R.string.missing_sensor);
            sensorLightValueView.setText(R.string.missing_data);
        } else {
            sensorLightTextView.setText(sensorLight.getName());
            //sensorLightValueView.setText(sensorLight.getVendor());
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sensorLight != null) sensorManager.registerListener(this,sensorLight,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {  //when sensor change, display its value

        int sensorType = event.sensor.getType();
        float currentValue = event.values[0];

        switch(sensorType){
            case Sensor.TYPE_GRAVITY:
            case Sensor.TYPE_LIGHT:
                sensorLightValueView.setText(String.valueOf(currentValue));
                break;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}