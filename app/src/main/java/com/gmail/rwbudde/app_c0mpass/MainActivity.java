package com.gmail.rwbudde.app_c0mpass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;

    private ImageView compassImage;
    private TextView degreeAsTextView;
    private TextView infoView;

    private Sensor rotationSensor;
    private int rAccuracy = 0;
    private boolean rAvailable;
    private float degreeStored = 0.0f;

    private final float[] rotationMatrix = new float[16];
    private final float[] orientation = new float[3];

    public MainActivity() {
        // initialize as identity
        rotationMatrix[ 0] = 1;
        rotationMatrix[ 4] = 1;
        rotationMatrix[ 8] = 1;
        rotationMatrix[12] = 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        compassImage = (ImageView) findViewById(R.id.compass_image);
        degreeAsTextView = (TextView) findViewById(R.id.degree_as_text);
        infoView = (TextView) findViewById(R.id.info);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        rAvailable = sensorManager.registerListener( this, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL );
        if (rAvailable) {
            sensorManager.unregisterListener(this); // ready to go
        } else {
            sensorManager.unregisterListener(this); // app will not work, sensor not available ...
            infoView.setText(getString(R.string.will_not_work));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (rAvailable) {
            sensorManager.registerListener( this, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL );
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {
        if (!rAvailable || event.sensor.getType() != Sensor.TYPE_ROTATION_VECTOR ) {
            return;
        }

        SensorManager.getRotationMatrixFromVector(rotationMatrix , event.values);
        SensorManager.getOrientation( rotationMatrix, orientation );
        int degree = (int) ( (Math.toDegrees( orientation[0] ) + 360) % 360);
        degreeAsTextView.setText(String.format(getString(R.string.degree_format), degree));
        RotateAnimation ra = new RotateAnimation(degreeStored,-degree, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        ra.setFillAfter(true);
        ra.setDuration(200);
        compassImage.startAnimation(ra);
        degreeStored = -degree;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // unused for Sensor.TYPE_ROTATION_VECTOR?
    }
}