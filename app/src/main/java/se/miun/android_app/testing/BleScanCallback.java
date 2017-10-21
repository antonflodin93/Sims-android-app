package se.miun.android_app.testing;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by jony1 on 2017-10-17.
 */

public class BleScanCallback extends ScanCallback {

    private TextView displayTextView;
    private Map<String, ScanResult> mScanResults;


    //allowed devices for scan result
    //private String[] AllowedDevices = {"IgB_Rej",
    //                                  "IgB_SoP"};



    //constructor
    BleScanCallback(Map<String, ScanResult> scanResults, TextView displayText){
        displayTextView = displayText;
        mScanResults = scanResults;

    }

    //function gets called every time a scan is successful
    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        /* Test of live update of rssi values */
        int rssi = result.getRssi();
        String deviceAddress = result.getDevice().getName();
        String bleTime = timeConverter(result);
        //display "live" data
        displayTextView.append( "\nDevice Name: " + deviceAddress );
        displayTextView.append( "\tRSSI: " + rssi );
        displayTextView.append( "\tTime: " + bleTime );


        //test mData
//        SparseArray<byte[]> manufacturerData = result.getScanRecord().getManufacturerSpecificData();
//        displayTextView.append("\tmData: ");
//        for(int u = 0; u < manufacturerData.size() ; u++) {
//            int manufacturerId = manufacturerData.keyAt(u);
//            displayTextView.append("" + manufacturerId);
//        }
        /*end test*/

        //store result
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
        mScanResults.put(deviceAddress, result);

        //test for filter on name...
//          String deviceAddress = result.getDevice().getName();
//        String deviceName = result.getDevice().getName();
//        //only add valid devices to list...
//        for(int i=0; i<AllowedDevices.length; i++){
//            if(deviceName == AllowedDevices[i]){
//                mScanResults.put(deviceAddress, result);
//            }
//        }
    }

    //convert timestamp to normal time
    public String timeConverter(ScanResult result){
        //received signal
        long rxTimestampMillis = System.currentTimeMillis() -
                SystemClock.elapsedRealtime() +
                result.getTimestampNanos() / 1000000;

        Date rxDate = new Date(rxTimestampMillis);

        String sDate = new SimpleDateFormat("HH:mm:ss.SSS").format(rxDate);

        return sDate;
    }

}
