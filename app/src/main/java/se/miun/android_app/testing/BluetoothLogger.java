package se.miun.android_app.testing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//import se.miun.sims.R;
import se.miun.android_app.R;

public class BluetoothLogger extends AppCompatActivity implements View.OnClickListener {
    private Button bleScan, bleOnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_logger);

        //init buttons
        bleScan = (Button) findViewById(R.id.bleScanButton);
        bleScan.setOnClickListener(this);
        bleOnOff = (Button) findViewById(R.id.bleOnOffButton);
        bleOnOff.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if( v.getId() == R.id.bleScanButton ){
            //Start bluetooth scanning for beacon advertisements

        }
        else if( v.getId() == R.id.bleOnOffButton ){
            //turn bluetooth on/off

        }
    }
}
