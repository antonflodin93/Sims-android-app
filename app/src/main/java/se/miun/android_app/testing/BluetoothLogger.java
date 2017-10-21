package se.miun.android_app.testing;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
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
import java.util.Timer;
import java.util.TimerTask;

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

    //Allowed Devices (MAC address)
    String[] filterlist = {
            "ED:0E:FF:9C:A9:6D",/* My RedBear nano address*/
            "C8:86:3A:91:0C:0C",
            "FD:49:FD:36:04:B4",
            "E9:91:4A:42:AC:3B",
    };



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
                //display batch results with scanComplete();
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
        //set scan filter and parameters
        setScanSettings();
        setScanFilter(filterlist);
        //create scanner object
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        //create callback
        mScanResults = new HashMap<>();
        mScanCallback = new BleScanCallback(mScanResults, displayDataTextView);
        //start the scan
        mBluetoothLeScanner.startScan(filters, settings, mScanCallback);
        //set scan check enable
        mScanning = true;
    }

    private void setScanSettings(){
        settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .build();
        //SCAN_MODE_LOW_LATENCY <-- use for fastest scan method
    }

    private void setScanFilter(String[] filterList){
        filters = new ArrayList<>();
        //add filter list of MAC addresses to filter
        for (int i=0; i< filterList.length ; i++) {
            ScanFilter filter = new ScanFilter.Builder().setDeviceAddress(filterList[i]).build();
            filters.add(filter);
        }
        //scanFilter = new ScanFilter.Builder().setDeviceAddress().build();
        //filters.add(scanFilter);
    }

    //check and display scan results (for test purposes)
    private void scanComplete(){
        if(mScanResults.isEmpty()){
            return;
        }
        //loop trough the map and fetch device address and rssi[dbm] value
        Set keys = mScanResults.keySet();
        for(Iterator i = keys.iterator(); i.hasNext();){
            String key = (String) i.next();

            ScanResult results = (ScanResult) mScanResults.get(key);
            //mac address
            String deviceAddress = results.getDevice().getAddress();
            int rssi = results.getRssi();
            ScanRecord scanRecord = results.getScanRecord();

            int contents = results.describeContents();
            int txPower = scanRecord.getTxPowerLevel();
            long timestampNanos = results.getTimestampNanos();

            displayDataTextView.append("\nname: " + results.getDevice().getName());
            //displayDataTextView.append("\nDeviceAddress: " + deviceAddress);
            displayDataTextView.append("\tRSSI: " + rssi );
            displayDataTextView.append("\tdist: " + calculateDistance(-56, rssi));
            //displayDataTextView.append("\tTime: " + timestampNanos);
            displayDataTextView.append("\tTx: " + txPower);
            //get manufacturer data...(Set as AdvData for beacon)
            displayDataTextView.append("\tmData: ");
            SparseArray<byte[]> manufacturerData = scanRecord.getManufacturerSpecificData();
            for(int u = 0; u < manufacturerData.size() ; u++){
                int manufacturerId = manufacturerData.keyAt(u);
                displayDataTextView.append("" + manufacturerId);
            }
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



    //algorithm used for distance calculation by Altbeacon.org
    public double calculateDistance(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }
        double ratio = rssi*1.0/txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio,10);
        }
        else {
            double accuracy =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;
            return accuracy;
        }
    }

    //timer function, if called, performs the run() function at the specified time (milliSecondDuration)
    public void timeMe(int milliSecondDuration) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            private final int MAX_TIME = 10;
            int counter = 0;

            @Override
            public void run() {
                if (counter < MAX_TIME) {
                    startScan();
                    counter++;
                } else {
                    cancel();
                    //displayDataTextView.append("counter is now: " + counter);
                    stopScan();
                }
            }
        };
        timer.schedule(task,0,milliSecondDuration);
    }

}
