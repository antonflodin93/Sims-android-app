package se.miun.android_app;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ImageTestActivity extends AppCompatActivity implements SensorEventListener, View.OnTouchListener {

    // Image that is going to be the pointer that indicates the person location
    private ImageView indicatorImage;

    private static int INDICATORSIZE = 100;

    // Background map image
    private ImageView mapImage;

    // Offset for compass calibration
    private int offsetdegrees = 83;

    // Current degrees the user turned
    private float currentDegree = 0f;



    private SensorManager mSensorManager;

    private TextView currentDegreesText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinatetest);

        // TextView that will tell the user the degree
        currentDegreesText = (TextView) findViewById(R.id.currentDegreesText);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        indicatorImage = (ImageView) findViewById(R.id.indicatorImage);
        mapImage = (ImageView) findViewById(R.id.mapImage);
        mapImage.setOnTouchListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register the sensor listener
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Stop the listener, this is to save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Get the angle around the z-axis rotated by the user
        float degree = Math.round(sensorEvent.values[0]- offsetdegrees);

        currentDegreesText.setText("Heading: " + Float.toString(degree) + " degrees");

        // Create a rotation animation
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);


        // Time the animation will operate on
        ra.setDuration(210);

        // After the animation, the pointer will have the correct direction
        ra.setFillAfter(true);

        // Start the animation
        indicatorImage.startAnimation(ra);

        currentDegree = -degree;

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {   }


    // When the screen has been touched
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        super.onTouchEvent(event);
        int eventAction = event.getAction();
        switch(eventAction) {
            case MotionEvent.ACTION_DOWN:
                // Get the coordinates of the point of touch
                float x = event.getX();
                float y = event.getY();

                placeImage(x, y);
                break;
        }
        return true;
    }

    private void placeImage(float X, float Y) {

        int x = (int) X;
        int y = (int) Y;

        // Place the indicator at the position
        indicatorImage.layout(x, y, x+INDICATORSIZE, y+INDICATORSIZE);
        Toast.makeText(ImageTestActivity.this, x + ", " + y , Toast.LENGTH_SHORT).show();
    }


}
