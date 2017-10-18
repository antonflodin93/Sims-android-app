package se.miun.android_app.testing;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

//import se.miun.sims.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import se.miun.android_app.R;

public class BluetoothLogger extends AppCompatActivity implements View.OnClickListener {
    //ui variables
    private Button bleScan, bleOnOff;
    private TextView displayDataTextView;

    //Bluetooth Variables
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;

    //scanning variables and objects
    private HashMap mScanResults;
    List<ScanFilter> filters;
    private boolean mScanning;
    private BluetoothLeScanner mBluetoothLeScanner;
    private ScanCallback mScanCallback;
    private ScanSettings settings;
    private ScanFilter scanFilter;

    //Scan filter Settings (Scan only for certain device name & address)
    private String deviceName = "Iggesund SIMS";
    private String deviceAddress = "e20a39f4-73f5-4bc4-a12f-17d1ad07a961";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_logger);

        //init buttons
        bleScan = (Button) findViewById(R.id.bleScanButton);
        bleScan.setOnClickListener(this);
        bleOnOff = (Button) findViewById(R.id.bleOnOffButton);
        bleOnOff.setOnClickListener(this);

        displayDataTextView = (TextView) findViewById(R.id.displayDataTextView);
        displayDataTextView.setText("");

        //init bluetooth manager
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        //access android bluetooth object
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    @Override
    public void onClick(View v){
        //toggle scan for bluetooth low energy adv...
        if( v.getId() == R.id.bleScanButton ){
            if(mScanning && bluetoothEnable()){
                Toast.makeText(getApplicationContext(), "Stopping Scan", Toast.LENGTH_SHORT).show();
                stopScan();
                //display results...
                scanComplete();
            }
            else if (bluetoothEnable()){
                Toast.makeText(getApplicationContext(), "Starting Scan", Toast.LENGTH_SHORT).show();
                startScan();
            }
        }
        else if( v.getId() == R.id.bleOnOffButton ){
            //toggle bluetooth on/off
            if (!bluetoothEnable()) {
                requestBluetoothEnable();
            }
            else if (bluetoothEnable()){
                requestBluetoothDisable();
            }
        }
    }

    //start scanning for bluetooth low energy advertisements
    private void startScan(){
        //check if scanner is already scanning
        if(mScanning){
            return;
        }

        //build scansettings with filters for device...
        settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();

        //create filter list
        filters = new ArrayList<>();
        scanFilter = new ScanFilter.Builder()
                /*.setDeviceName(deviceName)
                .setDeviceAddress(deviceAddress)
                */.build();
        filters.add(scanFilter); //currently breaks the program...

        //create scanner object
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        //create callback
        mScanResults = new HashMap<>();
        mScanCallback = new BleScanCallback(mScanResults);
        //start the scan
        mBluetoothLeScanner.startScan(filters, settings, mScanCallback);
        //set scan check enable
        mScanning = true;
    }

    //check and display scan results
    private void scanComplete(){
        if(mScanResults.isEmpty()){
            return;
        }
        //loop trough the map and fetch device address and rssi[dbm] value
        Set keys = mScanResults.keySet();
        for(Iterator i = keys.iterator(); i.hasNext();){
            String key = (String) i.next();

            ScanResult results = (ScanResult) mScanResults.get(key);
            String deviceAddress = results.getDevice().getAddress();
            int rssi = results.getRssi();

            //display id and rssi value(range: -127 - 126 dbm )
            displayDataTextView.append("\nDevice address: " + deviceAddress);
            displayDataTextView.append("\tRSSI: " + rssi );
        }
    }

    //stop scanning for ble devices
    private void stopScan(){
        if(mScanning && mBluetoothAdapter != null && mBluetoothAdapter.isEnabled() && mBluetoothLeScanner != null) {
            mBluetoothLeScanner.stopScan(mScanCallback);
        }
        mScanCallback = null;
        mScanning = false;
    }

    //check if bluetooth is enabled or disabled
    private boolean bluetoothEnable(){
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            return false;
        }
        return true;
    }

    //request enable / turn on bluetooth
    private void requestBluetoothEnable(){
        // displays a dialog requesting user permission to enable Bluetooth.
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    //disable bluetooth
    private void requestBluetoothDisable(){
        mBluetoothAdapter.disable();
    }
}
