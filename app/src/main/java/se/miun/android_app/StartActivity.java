package se.miun.android_app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class StartActivity extends Activity implements View.OnClickListener {

    private Button startEmployeeUnitAcvitivy, startMasterUnitAcvitivy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startEmployeeUnitAcvitivy = (Button) findViewById(R.id.startEmployeeUnitAcvitivy);
        startEmployeeUnitAcvitivy.setOnClickListener(this);

        startMasterUnitAcvitivy = (Button) findViewById(R.id.startMasterUnitAcvitivy);
        startMasterUnitAcvitivy.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.startEmployeeUnitAcvitivy){
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            intent.putExtra("userType", "EMPLOYEE");
            startActivity(intent);
        } else if(view.getId() == R.id.startMasterUnitAcvitivy){
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            intent.putExtra("userType", "MASTER");
            startActivity(intent);
        }

    }
}
