package se.miun.android_app.testing;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by jony1 on 2017-10-17.
 */

public class BleScanCallback extends ScanCallback {

    private TextView displayTextView;
    private Map<String, ScanResult> mScanResults;


    //constructor
    BleScanCallback(Map<String, ScanResult> scanResults, TextView displayText){
        displayTextView = displayText;
        mScanResults = scanResults;

    }

    //function gets called every time a scan is successful
    //this mode is used with the low_latency setting (primarily) but also works for
    // batch scan results
    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        /* Test of live update of rssi values */
        int rssi = result.getRssi();
        String deviceAddress = result.getDevice().getAddress();
        String deviceName = result.getDevice().getName();
        String bleTime = timeConverter(result);
        //display "live" data
        displayTextView.append( "\nName: " + deviceName );
        displayTextView.append( "\t " + deviceAddress );
        displayTextView.append( "\t RSSI: " + rssi );
        displayTextView.append( "\t Time: " + bleTime );
        displayTextView.append( "\t dist: " + getDistance(rssi) );

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

    //batch results
    //note: this is primarily used with the low_power scan setting and results are deliverd in
    //batches every ~4.5 seconds
    @Override
    public void onBatchScanResults(List<ScanResult> results){
        for(ScanResult result: results){
            addScanResult(result);
        }
    }

    //scanning fails
    @Override
    public void onScanFailed(int errorCode){
        Log.e(TAG, "BLE Scan Failed with Error code: " + errorCode);
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
    private String timeConverter(ScanResult result){
        //received signal
        long rxTimestampMillis = System.currentTimeMillis() -
                SystemClock.elapsedRealtime() +
                result.getTimestampNanos() / 1000000;

        Date rxDate = new Date(rxTimestampMillis);

        return new SimpleDateFormat("HH:mm:ss.SSS", Locale.ENGLISH).format(rxDate);
    }

    //estimate distance based on rssi level (experimentally derived formula)
    //only valid up to ~9m (-90 rssi value)
    double getDistance(int rssi) {
        //check for max? distance...
        if(rssi < -100){
            return 9.0;
        }
        else  {
            double e = 0.6859;
            double b = Math.pow(2389, e);
            double n = Math.pow((4447 + 50 * rssi), e);
            return b / n;
        }
    }


    //storage container for beacon data
    private class BeaconData{
        private String id;
        private int rssi;
        private String time;

        public BeaconData(String id, int rssi, String time){
            this.id = id;
            this.rssi = rssi;
            this.time = time;
        }

        public String getId(){
            return id;
        }

        public void setRssi(int rssi){
            this.rssi = rssi;
        }
        public int getRssi(){
            return rssi;
        }

        public void setTime(String time){
            this.time = time;
        }
        public String getTime(){
            return time;
        }

    }

}

