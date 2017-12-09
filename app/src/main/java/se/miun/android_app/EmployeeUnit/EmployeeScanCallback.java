package se.miun.android_app.EmployeeUnit;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by jony1 on 2017-11-25.
 */

class EmployeeScanCallback extends ScanCallback {
    private Map<String, Integer> scanResults;
    //calculate average rssi values for beacon 1-3
    private RollingAverage rB1, rB2, rB3;

    EmployeeScanCallback(Map<String, Integer> scanResults) {
        this.scanResults = scanResults;

        //hardcoded addresses...
        //do rolling average on 10 measurements
        rB1 = new RollingAverage("EB:09:BD:0E:78:37", 10);
        rB2 = new RollingAverage("D7:1F:BE:CB:E0:16", 10);
        rB3 = new RollingAverage("D4:C4:D4:66:72:C5", 10);
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        addResults(result);
    }

    @Override
    public void onBatchScanResults(List<ScanResult> results){
        for(ScanResult result: results){
            addResults(result);
        }
    }

    @Override
    public void onScanFailed(int errorCode){
        Log.e(TAG, "BLE Scan Failed with Error code: " + errorCode);
    }

    private void addResults(ScanResult result){
        //get MAC address and rssi value from scans
        int rssi = result.getRssi();
        String deviceAddress = result.getDevice().getAddress();

        //compute rolling average if device id is beacon 1-3
        //else just put in the normal rssi value (unfiltered)
        rssi = setRollingAverage(deviceAddress, rssi);
        if(rssi != 0) {
            scanResults.put(deviceAddress, rssi);
        }
        else{
            scanResults.put(deviceAddress, result.getRssi() );
        }
    }

    public Map<String, Integer> getResults(){
        return scanResults;
    }

    private int setRollingAverage(String deviceId, int rssi){
        if(deviceId.equals( rB1.getDeviceId() ) ){
            //todo beacon 1
            rB1.setRssiStorage(rssi);
            return rB1.getAverageRssi();
        }
        else if (deviceId.equals( rB2.getDeviceId() ) ){
            //todo beacon 2
            rB2.setRssiStorage(rssi);
            return rB2.getAverageRssi();
        }
        else if (deviceId.equals( rB3.getDeviceId() ) ){
            //todo beacon 3
            rB3.setRssiStorage(rssi);
            return rB3.getAverageRssi();
        }
        else {
            //do nothing
            Log.e("0005","No legal Device ID set");
            return 0;
        }
    }

}
