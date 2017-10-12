package se.miun.android_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import se.miun.android_app.testing.ImageTestActivity;
import se.miun.android_app.testing.RangetestActivity;
import se.miun.android_app.testing.RetrofitTestActivity;

public class MainActivity extends Activity implements View.OnClickListener{
    private Button rangeTestBtn, testCoordinateSystemBtn, databaseTestBtn, retrofitTestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the buttons
        rangeTestBtn = (Button) findViewById(R.id.rangeTestBtn);
        rangeTestBtn.setOnClickListener(this);
        testCoordinateSystemBtn = (Button) findViewById(R.id.testCoordinateSystemBtn);
        testCoordinateSystemBtn.setOnClickListener(this);
        retrofitTestBtn = (Button) findViewById(R.id.retrofitTestBtn);
        retrofitTestBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // Check which button that has been pressed
        if( view.getId() == R.id.rangeTestBtn ) {
            // Start the rangetest activity
            Intent myIntent = new Intent(getApplicationContext(), RangetestActivity.class);
            this.startActivity(myIntent);
        } else if(view.getId() == R.id.testCoordinateSystemBtn){
            // Start the coordinate system test activity
            Intent myIntent = new Intent(getApplicationContext(), ImageTestActivity.class);
            this.startActivity(myIntent);
        } else if(view.getId() == R.id.retrofitTestBtn) {
            // Start Retrofit test activity
            Intent myIntent = new Intent(getApplicationContext(), RetrofitTestActivity.class);
            this.startActivity(myIntent);
        }
    }
}
