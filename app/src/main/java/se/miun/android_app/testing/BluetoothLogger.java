package se.miun.android_app.testing;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.miun.android_app.R;

//import se.miun.sims.R;

public class BluetoothLogger extends AppCompatActivity implements View.OnClickListener {
    private TextView displayDataTextView;
    private EditText editFileName;

    //Bluetooth Variables
    private BluetoothAdapter mBluetoothAdapter;

    List<ScanFilter> filters;
    private boolean mScanning;
    private BluetoothLeScanner mBluetoothLeScanner;
    private ScanCallback mScanCallback;
    private ScanSettings settings;

    //Allowed Devices (MAC address)
    String[] filterlist = {
            "ED:0E:FF:9C:A9:6D",
            "EE:2B:8F:54:76:14",
            "D4:C4:D4:66:72:C5",
            "C8:D8:A1:7F:29:7A",
            "D7:1F:BE:CB:E0:16",
            "EB:09:BD:0E:78:37",
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_logger);

        //init buttons
        Button bleScan = (Button) findViewById(R.id.bleScanButton);
        bleScan.setOnClickListener(this);
        Button bleOnOff = (Button) findViewById(R.id.bleOnOffButton);
        bleOnOff.setOnClickListener(this);
        Button saveToFile = (Button) findViewById(R.id.saveToFileButton);
        saveToFile.setOnClickListener(this);

        displayDataTextView = (TextView) findViewById(R.id.displayDataTextView);
        displayDataTextView.setText("");
        //edit filename
        editFileName = (EditText) findViewById(R.id.editSaveFileText);

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
        else if( v.getId() == R.id.saveToFileButton ){
            if(isExternalStorageWritable()){
                bleLoggData();
                Toast.makeText(getApplicationContext(), "Saved file to /Downloads", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Save to File FAILED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //start scanning for bluetooth low energy advertisements
    private void startScan(){
        //check if scanner is already scanning
        if(mScanning){
            Toast.makeText(getApplicationContext(), "Already Scanning", Toast.LENGTH_SHORT).show();
            return;
        }
        //set scan filter and parameters
        setScanSettings();
        setScanFilter(filterlist);
        //create scanner object
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        //create callback
        HashMap mScanResults = new HashMap<>();

        //temp scanfilter(to get MAC addresses from the beacons...
        List<ScanFilter> filtersTmp = new ArrayList<>();
                //no scan filter
        ScanFilter scanFilterTmp = new ScanFilter.Builder().build();
        filtersTmp.add(scanFilterTmp);
        //end of temp scanfilters

        mScanCallback = new BleScanCallback(mScanResults, displayDataTextView);
        //start the scan
        mBluetoothLeScanner.startScan(filters /* REPLACE WITH 'filters' when testing is complete*/, settings, mScanCallback);
        //set scan check enable
        mScanning = true;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    //store ble scan data in file under /downloads
    public void bleLoggData(){
        String fileName = editFileName.getText().toString();
        String content = displayDataTextView.getText().toString();
        File file;
        FileOutputStream outputStream;
        try {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName + ".txt");

            outputStream = new FileOutputStream(file);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //ble scan settings (fast/slow scan mode etc.)
    private void setScanSettings(){
        settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .build();
    }

    //setup of filter settings for the scan (filters on mac address)
    private void setScanFilter(String[] filterList){
        filters = new ArrayList<>();
        //add filter list of MAC addresses to filter
        for (int i=0; i< filterList.length ; i++) {
            ScanFilter filter = new ScanFilter.Builder().setDeviceAddress(filterList[i]).build();
            filters.add(filter);
        }
    }

    //stop scanning for ble devices
    public void stopScan(){
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
        int REQUEST_ENABLE_BT = 1;
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    //disable bluetooth
    private void requestBluetoothDisable(){
        mBluetoothAdapter.disable();
    }

    //check and display scan results (for test purposes)
//    private void scanComplete(){
//        if(mScanResults.isEmpty()){
//            return;
//        }
//        //loop trough the map and fetch device address and rssi[dbm] value
//        Set keys = mScanResults.keySet();
//        for(Iterator i = keys.iterator(); i.hasNext();){
//            String key = (String) i.next();
//
//            ScanResult results = (ScanResult) mScanResults.get(key);
//            //mac address
//            String deviceAddress = results.getDevice().getAddress();
//            int rssi = results.getRssi();
//            ScanRecord scanRecord = results.getScanRecord();
//
//            int contents = results.describeContents();
//            int txPower = scanRecord.getTxPowerLevel();
//            long timestampNanos = results.getTimestampNanos();
//
//            displayDataTextView.append("\nname: " + results.getDevice().getName());
//            //displayDataTextView.append("\nDeviceAddress: " + deviceAddress);
//            displayDataTextView.append("\tRSSI: " + rssi );
//            displayDataTextView.append("\tdist: " + calculateDistance(-56, rssi));
//            //displayDataTextView.append("\tTime: " + timestampNanos);
//            displayDataTextView.append("\tTx: " + txPower);
//            //get manufacturer data...(Set as AdvData for beacon)
//            displayDataTextView.append("\tmData: ");
//            SparseArray<byte[]> manufacturerData = scanRecord.getManufacturerSpecificData();
//            for(int u = 0; u < manufacturerData.size() ; u++){
//                int manufacturerId = manufacturerData.keyAt(u);
//                displayDataTextView.append("" + manufacturerId);
//            }
//        }
//    }

}
