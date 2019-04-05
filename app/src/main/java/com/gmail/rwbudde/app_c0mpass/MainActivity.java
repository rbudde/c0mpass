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
    private int fromDegree = 0;

    private float[] rotationMatrix = new float[16];
    private float[] orientation = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        setContentView(R.layout.activity_main);
        compassImage = (ImageView) findViewById(R.id.compass_image);
        degreeAsTextView = (TextView) findViewById(R.id.degree_as_text);
        infoView = (TextView) findViewById(R.id.info);
    }

    @Override
    protected void onResume() {
        super.onResume();

        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (rotationSensor == null) {
            infoView.setText(getString(R.string.will_not_work));
        } else {
            sensorManager.registerListener( this, rotationSensor, 200000 );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ROTATION_VECTOR ) {
            return;
        }
        SensorManager.getRotationMatrixFromVector(rotationMatrix , event.values);
        SensorManager.getOrientation(rotationMatrix, orientation);
        int toDegree = (int) (( Math.toDegrees(orientation[0]) + 360.0) % 360);
        degreeAsTextView.setText(String.format(getString(R.string.degree_format), toDegree));

        int delta = deltaWithDirection(fromDegree, toDegree);
        RotateAnimation ra = new RotateAnimation(-fromDegree,-fromDegree - delta, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        ra.setFillAfter(true);
        ra.setDuration(200);
        compassImage.startAnimation(ra);
        fromDegree = toDegree;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() != Sensor.TYPE_ROTATION_VECTOR ) {
            return;
        }
        infoView.setText(String.format(getString(R.string.info), accuracy));
    }

    public static int degree2view(int d) {
        return d <= 180 ? d : d - 360;
    }

    public static int view2degree(int d) {
        return d < 0 ? d + 360 : d;
    }

    public static int deltaWithDirection(int fromDegree, int toDegree) {
        int fromDegreeOpposite = degreeOfOpposite(fromDegree);
        if (fromDegree <= fromDegreeOpposite) {
            if (fromDegree <= toDegree && toDegree <= fromDegreeOpposite) {
                return toDegree - fromDegree; // clockwise
            } else {
                return -((360 - toDegree + fromDegree) % 360); // anti clockwise
            }
        } else {
            if ( fromDegreeOpposite < toDegree && toDegree <= fromDegree ) {
                return -(fromDegree - toDegree); // anti clockwise
            } else {
                return (360 - fromDegree + toDegree) % 360;
            }
        }
    }

    public static int degreeOfOpposite(int degree) {
        return (degree + 180) % 360;
    }
}