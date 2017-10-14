package se.miun.android_app.testing;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

//import se.miun.sims.R;
import se.miun.android_app.R;

public class BluetoothLogger extends AppCompatActivity implements View.OnClickListener {
    private Button bleScan, bleOnOff;
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;  /*variable needed to be passed into startActivityForResult
    which must be > 0 ... */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_logger);

        //init buttons
        bleScan = (Button) findViewById(R.id.bleScanButton);
        bleScan.setOnClickListener(this);
        bleOnOff = (Button) findViewById(R.id.bleOnOffButton);
        bleOnOff.setOnClickListener(this);

        //init bluetooth
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    @Override
    public void onClick(View v){
        if( v.getId() == R.id.bleScanButton ){
            //Start bluetooth scan for beacon advertisements

        }
        else if( v.getId() == R.id.bleOnOffButton ){
            // Ensures Bluetooth is available on the device and it is enabled. If not,
            // displays a dialog requesting user permission to enable Bluetooth.
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else if (mBluetoothAdapter.isEnabled()){
                //turn off bluetooth
                mBluetoothAdapter.disable();
            }

        }
    }
}
