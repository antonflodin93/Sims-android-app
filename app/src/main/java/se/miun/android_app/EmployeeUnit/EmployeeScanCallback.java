package se.miun.android_app.EmployeeUnit;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.util.Log;

import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by jony1 on 2017-11-25.
 */

class EmployeeScanCallback extends ScanCallback {
    Map<String, Integer> scanResults;

    EmployeeScanCallback(Map<String, Integer> scanResults) {
        this.scanResults = scanResults;
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
        int rssi = result.getRssi();
        String deviceAddress = result.getDevice().getAddress();
        scanResults.put(deviceAddress, rssi);
    }

}