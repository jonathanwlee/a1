package edu.cornell.jjl.a1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView shakeDetection;
    private TextView accelerationValues;
    private Button startButton;
    private Button stopButton;
    private TextView barometerText;
    private Button barometerButton;
    private boolean start = false;
    private EditText thresholdInput;
    private int threshold;

    private SensorManager mSensorManager;
    private SensorManager mPressureManager;

    private Sensor mSensor;
    private Sensor mPressure;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
    }

    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSensorChanged(SensorEvent event){
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float sensorX = event.values[0];
            float sensorY = event.values[1];
            float sensorZ = event.values[2];
            //Log.w("sensorX", Float.toString(sensorX));

            if (start) {
                updateAccelerometer(sensorX, sensorY, sensorZ);
            }

            else {
            }
        }

        else if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            float millibars_of_pressure = event.values[0];
            barometerText.setText("Air Pressure: " + Float.toString(millibars_of_pressure));
            mPressureManager.unregisterListener(this);
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void updateAccelerometer(float x,float y, float z) {
        accelerationValues.setText("{" + Float.toString(x) + "," + Float.toString(y) + "," + Float.toString(z) + "}");
        shakeAlgorithm(x,y,z,threshold);
    }

    public void initViews() {
        shakeDetection = (TextView) findViewById(R.id.shakeDetection);
        accelerationValues = (TextView) findViewById(R.id.accelerationValues);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mPressureManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mPressure = mPressureManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        thresholdInput = (EditText) findViewById(R.id.thresholdInput);
        barometerButton = (Button) findViewById(R.id.barometerButton);
        barometerText = (TextView) findViewById(R.id.barometerText);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start = true;
                threshold = Integer.parseInt(thresholdInput.getText().toString());

            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start = false;
            }
        });

        barometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPressureManager.registerListener(MainActivity.this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
            }
        });


    }

    public void shakeAlgorithm(double ax, double ay, double az,double threshold) {
        Double x = Math.sqrt(Math.pow(ax,2) + Math.pow(ay,2) + Math.pow(az,2));
       // Log.w("MATH: ",x.toString());
        if (Math.sqrt(Math.pow(ax,2) + Math.pow(ay,2) + Math.pow(az,2)) < threshold) {
            shakeDetection.setText("no shake");
        }
        else {
            shakeDetection.setText("shake");
        }
    }
}
