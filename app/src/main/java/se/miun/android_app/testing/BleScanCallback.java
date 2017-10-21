package se.miun.android_app.testing;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by jony1 on 2017-10-17.
 */

public class BleScanCallback extends ScanCallback {

    int timeKeeper = 0;
    private TextView displayTextView;
    private Map<String, ScanResult> mScanResults;

    //allowed devices for scan result
    private String[] AllowedDevices = {"IgB_Rej", "IgB_SoP"};



    //constructor
    BleScanCallback(Map<String, ScanResult> scanResults, TextView displayText){
        displayTextView = displayText;
        mScanResults = scanResults;
    }

    //function gets called everytime a scan is successful
    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        /* Test of live update of rssi values */
//        int rssi = result.getRssi();
//        String deviceAddress = result.getDevice().getAddress();
//        displayTextView.append("\nDevice address: " + deviceAddress);
//        displayTextView.append("\tRSSI: " + rssi);
        /*end live update rssi test*/

        addScanResult(result);
    }

    //store batch results
    @Override
    public void onBatchScanResults(List<ScanResult> results){
        for(ScanResult result: results){
            addScanResult(result);
        }
    }

    //scanning fails
    @Override
    public void onScanFailed(int errorcode){
        Log.e(TAG, "BLE Scan Failed with Error code: " + errorcode);
    }

    //Store batch result (1 value for each device)
    public void addScanResult(ScanResult result){
        String deviceAddress = result.getDevice().getName();
//        String deviceName = result.getDevice().getName();
//        //only add valid devices to list...
//        for(int i=0; i<AllowedDevices.length; i++){
//            if(deviceName == AllowedDevices[i]){
//                mScanResults.put(deviceAddress, result);
//            }
//        }
        mScanResults.put(deviceAddress, result);
    }

}
