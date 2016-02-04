package edu.cornell.jjl.a1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView shakeDetection;
    TextView accelerationValues;
    Button startButton;
    Button stopButton;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private boolean start = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
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
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }

        float sensorX = event.values[0];
        float sensorY = event.values[1];
        float sensorZ = event.values[2];
        if (start) {
            updateAccelerometer(sensorX, sensorY, sensorZ);
        }

        else {

        }
    }

    public void updateAccelerometer(float x,float y, float z) {
        accelerationValues.setText(Float.toString(x) + Float.toString(y) + Float.toString(z));
    }

    public void initViews() {
        shakeDetection = (TextView) findViewById(R.id.shakeDetection);
        accelerationValues = (TextView) findViewById(R.id.accelerationValues);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start = true;
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start = false;
            }
        });
    }

    public void shakeAlgorithm(double ax, double ay, double az,double threshold) {
        if (Math.sqrt(Math.pow(ax,2) + Math.pow(ay,2) + Math.pow(az,2)) < threshold) {
            shakeDetection.setText("no shake");
        }
        else {
            shakeDetection.setText("shake");
        }
    }
}
