package se.miun.android_app.testing;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.util.Log;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by jony1 on 2017-10-17.
 */

public class BleScanCallback extends ScanCallback {



    private Map<String, ScanResult> mScanResults;

    BleScanCallback(Map<String, ScanResult> scanResults){
        mScanResults = scanResults;
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        addScanResult(result);
    }

    @Override
    public void onBatchScanResults(List<ScanResult> results){
        for(ScanResult result: results){
            addScanResult(result);
        }
    }

    @Override
    public void onScanFailed(int errorcode){
        Log.e(TAG, "BLE Scan Failed with Error code: " + errorcode);
    }

    public void addScanResult(ScanResult result){
        BluetoothDevice device = result.getDevice();
        String deviceAddress = device.getName();
        //int rssi = result.getRssi();
        mScanResults.put(deviceAddress, result);
    }
}
