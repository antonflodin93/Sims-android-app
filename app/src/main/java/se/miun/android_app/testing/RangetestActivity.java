package se.miun.android_app.testing;

import android.app.Activity;
import android.os.RemoteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import java.util.Collection;

import se.miun.android_app.R;
import se.miun.android_app.testing.BeaconDistance;

public class RangetestActivity extends Activity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;
    private TextView distanceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rangetest);

        // Initialize the beacon manager, set layout of the beacons that will be used, bind it to this activity
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);

        // Initialize the list that will display beacon information
        distanceList = (EditText) findViewById(R.id.distanceList);

    }

    // Release resources
    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    // Called when the beacon service is running and ready to accept your commands through the BeaconManager
    @Override
    public void onBeaconServiceConnect() {

        try {
            // Add a region that we will listen to
            ///Region region = new Region("myRangingUniqueId", null, null, null);
            Region region = new Region("myRangingUniqueId", Identifier.parse("e20a39f4-73f5-4bc4-a12f-17d1ad07a961"), Identifier.parse("1122"), Identifier.parse("3344"));
            // e20a39f4-73f5-4bc4-a12f-17d1ad07a961
            // 1122
            // 3344

            // Start listening for beacons in the region
            beaconManager.startRangingBeaconsInRegion(region);
            beaconManager.startMonitoringBeaconsInRegion(region);
        } catch (RemoteException e) {    }

        // Log.i(TAG, "connect");


        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            // When a user enters a region
            @Override
            public void didEnterRegion(Region region) {
                displayText("ENTERED");
            }

            // When a user exits a region
            @Override
            public void didExitRegion(Region region) {
                displayText("EXIT");
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {

            }
        });
        beaconManager.addRangeNotifier(new RangeNotifier() {

            // When beacons are in region
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    try{

                        // For every beacon connected, display a distance using BeaconDistance class
                        for(Beacon beacon : beacons){
                            BeaconDistance bc = new BeaconDistance(beacon);
                            double distance = bc.getDistance();
                            displayText(String.valueOf(distance) + " (distance)");
                        }

                    }catch(Exception e){
                        Log.i(TAG, e.getMessage());
                    }
                }
            }
        });


    }

    //
    private void displayText(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                distanceList.append(line+"\n");
            }
        });
    }
}