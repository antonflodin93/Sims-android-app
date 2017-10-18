package se.miun.android_app.EmployeeUnit;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import se.miun.android_app.R;
import se.miun.android_app.testing.ImageTestActivity;
import se.miun.android_app.testing.RangetestActivity;

public class EmployeeUnitActivity extends Activity /*implements View.OnClickListener*/ {

    private Button imageTestBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_unit);
        /*imageTestBtn = (Button) findViewById(R.id.imageTestBtn);
        imageTestBtn.setOnClickListener(this);*/
    }

   /* @Override
    public void onClick(View view) {
        // Check which button that has been pressed
        if( view.getId() == R.id.imageTestBtn ) {
            // Start the ImageTest activity
            Intent myIntent = new Intent(getApplicationContext(), ImageTestActivity.class);
            this.startActivity(myIntent);
        }

    }*/
}
