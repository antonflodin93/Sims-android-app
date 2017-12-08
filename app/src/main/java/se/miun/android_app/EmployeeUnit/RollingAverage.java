package se.miun.android_app.EmployeeUnit;

/**
 * Created by jony1 on 2017-12-08.
 * Calculates the rolling average value.
 * Call this class and pass in device ID (MAC address)
 * and the number of rolling averages you want (Recommended: 5-10).
 * Call setRssiStorage() with every new measurement and then retrieve
 * the average value with getAverageRssi()
 */

public class RollingAverage {
    //use the x last measurements to determine average
    private int[] rssiStorage;
    private int counter;
    String deviceId;

    RollingAverage(String deviceID, int averageSize){
        this.deviceId = deviceID;
        this.rssiStorage = new int[averageSize];
    }

    public void setDeviceId(String deviceId){
        this.deviceId = deviceId;
    }

    public String getDeviceId(){
        return deviceId;
    }

    //fills up the rssi container with last measured value, uses FIFO
    public void setRssiStorage(int rssi){
        //reset counter at max container size
        if(counter == rssiStorage.length){
            counter = 0;
        }
        rssiStorage[counter] = rssi;
        counter++;
    }

    //return average value of rssi from the storage container
    public int getAverageRssi(){
        int tmpRssi=0;
        for (int i = 0; i < rssiStorage.length ; i++) {
            tmpRssi += rssiStorage[i];
        }
        double averageRssi = tmpRssi / rssiStorage.length;
        return (int)Math.round(averageRssi);
    }

}
