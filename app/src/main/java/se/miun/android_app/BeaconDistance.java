package se.miun.android_app;
import org.altbeacon.beacon.Beacon;
import java.util.Collection;


public class BeaconDistance {
    private Beacon beacon;
    private double distance;

    public BeaconDistance(Beacon beacon){
        this.beacon = beacon;
    }

    public double getDistance(){
        this.distance = 0;
        return distance;
    }


}
