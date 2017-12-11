package se.miun.android_app.EmployeeUnit;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jony1 on 2017-11-24.
 */

public class BleScanner {

    private BluetoothAdapter    mBluetoothAdapter;
    private ScanCallback        mScanCallback;
    private List<ScanFilter>    filters;
    private boolean             mScanning;
    private BluetoothLeScanner  mBluetoothLeScanner;
    private ScanSettings        settings;
    private Map<String, Integer> scanResults;



    //Filter ble devices on MAC address.
    private String[] filterlist = {
            "ED:0E:FF:9C:A9:6D",
            "EE:2B:8F:54:76:14",
            "D4:C4:D4:66:72:C5",
            "C8:D8:A1:7F:29:7A",
            "D7:1F:BE:CB:E0:16",
            "EB:09:BD:0E:78:37",
    };




    BleScanner(Map<String, Integer> scanResults, BluetoothAdapter mBluetoothAdapter){
        this.scanResults = scanResults;
        this.mBluetoothAdapter = mBluetoothAdapter;
    }

    public void startScan(int scanMode){
        //exit if scan is already going
        if(mScanning){
            Log.e("BLE", "BLE already scanning");
            return;
        }
        //exit if bluetooth is NOT turned on
        else if (! bluetoothEnable() ){
            Log.e("BLE", "Bluetooth not enabled");
            return;
        }
        else {
            //set scan filter and parameters
            setScanSettings(scanMode);
            setScanFilter(filterlist);
            //create scanner object
            mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            //callback on scans
            mScanCallback = new EmployeeScanCallback(scanResults);


            //temp scanfilter(to get MAC addresses from the beacons...
            List<ScanFilter> filtersTmp = new ArrayList<>();
            //no scan filter
            ScanFilter scanFilterTmp = new ScanFilter.Builder().build();
            filtersTmp.add(scanFilterTmp);
            //end of temp scanfilters




            //start the scan
            mBluetoothLeScanner.startScan(filtersTmp, settings, mScanCallback);
            mScanning = true;

            Log.i("Scans", "Starting Scans");
        }
    }

    //stop scanning for ble devices
    public void stopScan(){
        if(mScanning && mBluetoothAdapter != null && mBluetoothAdapter.isEnabled() && mBluetoothLeScanner != null) {
            mBluetoothLeScanner.stopScan(mScanCallback);
            Log.i("Scans", "Stopping Scans");
        }
        mScanCallback = null;
        mScanning = false;
    }

    private void setScanSettings(int scanMode){
        //0 for low power scan (batch scan results)
        switch (scanMode){
            case 0:             settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                    .build();
                    break;
            case 1:            settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
                    break;
            default:             settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
                    break;
        }
    }

    //setup of filter settings for the scan (filters on mac address)
    private void setScanFilter(String[] filterList){
        filters = new ArrayList<>();
        //add filter list of MAC addresses to filter
        for (String aFilterList : filterList) {
            ScanFilter filter = new ScanFilter.Builder().setDeviceAddress(aFilterList).build();
            filters.add(filter);
        }
    }

    //check if bluetooth is enabled or disabled
    private boolean bluetoothEnable(){
        if (mBluetoothAdapter == null || mBluetoothAdapter.isEnabled()==false  ) {
            return false;
        }
        return true;
    }

    public Map<String, Integer> getResults(){
        return scanResults;
    }

}