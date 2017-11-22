package se.miun.android_app.EmployeeUnit;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import se.miun.android_app.Adapter.RegularMessageAdapter;
import se.miun.android_app.R;
import se.miun.android_app.Service.RegularMessageService;
import se.miun.android_app.Service.WarningMessageService;
import se.miun.android_app.Model.Message;
import se.miun.android_app.testing.Area;
import se.miun.android_app.testing.ImageTestActivity;

public class EmployeeUnitActivity extends Activity implements View.OnClickListener {

    public enum MessageType {
        WARNING, REGULAR;
    }


    private ImageButton warningMessageBtn, regularMessageBtn;
    private Context context;
    private Intent regularMessageIntent, warningMessageIntent;
    private ArrayList<Message> regularMessages;
    private ArrayList<Message> warningMessages;
    private RecyclerView messageRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RegularMessageAdapter adapter;
    private int numOfReceievedRegularMessages, numOfReceievedWarningMessages;
    private boolean clickedButton = false;
    private Bitmap bmp;
    private ImageView mapImageView;
    private Beacon beacon1, beacon2, beacon3;
    private ArrayList<Area> areas;
    float ymax, xmax;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            // When receiving regular messages
            if (intent.getAction().equals("RegularMessageService")) {
                regularMessages = (ArrayList<Message>) intent.getSerializableExtra("messages");
                if (regularMessages.size() > numOfReceievedRegularMessages) {
                    regularMessageBtn.setBackgroundColor(Color.RED);
                    numOfReceievedRegularMessages = regularMessages.size();
                }
                //ShowMessagesActivity.this.updateUIRegularMessages(intent);

                // When receiving warning messages
            } else if (intent.getAction().equals("WarningMessageService")) {
                warningMessages = (ArrayList<Message>) intent.getSerializableExtra("messages");
                if (warningMessages.size() > numOfReceievedWarningMessages) {
                    warningMessageBtn.setBackgroundColor(Color.RED);
                    numOfReceievedWarningMessages = warningMessages.size();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_unit);
        context = this;

        // Init components
        warningMessageBtn = (ImageButton) findViewById(R.id.warningMessageBtn);
        warningMessageBtn.setOnClickListener(this);
        regularMessageBtn = (ImageButton) findViewById(R.id.regularMessageBtn);
        regularMessageBtn.setOnClickListener(this);
        this.regularMessageIntent = new Intent(this, RegularMessageService.class);
        this.warningMessageIntent = new Intent(this, WarningMessageService.class);

        mapImageView = (ImageView) findViewById(R.id.mapImageView);


        // Dimension of the room in pixels
        DisplayMetrics dm = getResources().getDisplayMetrics();
        //gets maximum width and height of device in terms of pixels
        int width = dm.widthPixels;
        //50 is seekbar height
        int height = dm.heightPixels - 50;

        // Create areas
        setImageAreas();

        // The beacons locations
        beacon1 = new Beacon(0, 0);
        beacon2 = new Beacon(width, 0);
        beacon3 = new Beacon(width, height);


        // Get values from all 3 beacons
        int rssiB1 = -85;
        int rssiB2 = -95;
        int rssiB3 = -90;


        // Get radius from the beacons (in pixels)
        double radius1 = getDistance(rssiB1);
        double radius2 = getDistance(rssiB2);
        double radius3 = getDistance(rssiB3);

        // Create circles and output x and y that is within the circle
        getAreasInCircle((int) Math.floor(radius1), beacon1);
        getAreasInCircle((int) Math.floor(radius2), beacon2);
        getAreasInCircle((int) Math.floor(radius3), beacon3);

    }

    private void getAreasInCircle(int radius, Beacon beacon) {
        // Iterate through every areas
        for (int i = 0; i < areas.size(); i++) {
            //todo need to get how many ares in x and y direction to make correct calculations..
            //if (px > areas.get(i).getxmin() && px < areas.get(i).getxmax() && areas.get(i).getymin() < py && areas.get(i).getymax() > py) {
              //  Toast.makeText(ImageTestActivity.this, "Clicked Area: " + areas.get(i).getrow() + ", " + areas.get(i).getcollumn() + ", Coordinates: " + px + ", " + py +". zoomfactor: "+ zoomfactor, Toast.LENGTH_SHORT).show();
            //}
        }
    }

    private int convertGetDistanceToArea(double rssiMeters){
        return areas.size() / (int) Math.floor(rssiMeters);
        //todo need to get how many ares in x and y direction to make correct calculations..
    }



    // EXPERIMENTAL DEVELOPED FORMULA
    // Returns distance for a given rssi
    double getDistance(int rssi) {
        double e = 0.6859;
        double b = Math.pow(2389, e);
        double n = Math.pow((4447 + 50 * rssi), e);
        return b / n;
    }


    public void onResume() {
        super.onResume();

        this.startService(this.warningMessageIntent);
        this.startService(this.regularMessageIntent);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("RegularMessageService");
        intentFilter.addAction("WarningMessageService");
        this.registerReceiver(this.broadcastReceiver, intentFilter);
    }

    public void onPause() {
        super.onPause();
        if (!clickedButton) {
            this.unregisterReceiver(this.broadcastReceiver);
            this.stopService(this.regularMessageIntent);
            this.stopService(this.warningMessageIntent);
        }

        // Maybee needed
        //this.unregisterReceiver(this.broadcastReceiver);
    }

    private void setImageAreas(){

        //WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        //creates an object devicemetrics that contain information about devicemetrics
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();

        //gets maximum width and height of device in terms of pixels
        //float deviceheight = displaymetrics.widthPixels;
        //float devicewidth = displaymetrics.heightPixels;

        //set amount of areas in both row and collumn
        int collumnsize = 8;
        int rowsize = 10;

        //float sizeY = deviceheight/rowsize;
        //float sizeX = devicewidth/collumnsize;

        //meters which later should be based on floorplan image
        float floorplanmeterx = 25;
        float floorplanmetery = 40;

        //get xmax and ymax for the first area (example meters of floorplan, 25*40m^2)
        xmax = floorplanmeterx / collumnsize;
        ymax = floorplanmetery / rowsize;



        //add areas according to row and collumn sizes
        for (int r = 0; r < rowsize; r++) {
            for (int c = 0; c < collumnsize; c++) {
                Area area = new Area(xmax * (c), xmax * (c + 1), ymax * (r), ymax * (r + 1), c + 1, r + 1);
                //area.setRealLimits(c*sizeX, c*sizeX+sizeX, r*sizeY, r*sizeY+sizeY);
                areas.add(area);
                //Toast.makeText(context, "Area: " + xmax *(r) + ", " + xmax * (r+1), Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onClick(View v) {
        clickedButton = true;
        if (v.getId() == R.id.warningMessageBtn) {
            Intent myIntent = new Intent(getApplicationContext(), ShowMessagesActivity.class);
            myIntent.putExtra("MESSAGETYPE", MessageType.WARNING);
            myIntent.putExtra("WARNINGMESSAGES", warningMessages);
            warningMessageBtn.setBackgroundColor(Color.BLACK);
            context.startActivity(myIntent);

        } else if (v.getId() == R.id.regularMessageBtn) {
            Intent myIntent = new Intent(getApplicationContext(), ShowMessagesActivity.class);
            myIntent.putExtra("MESSAGETYPE", MessageType.REGULAR);
            myIntent.putExtra("REGULARMESSAGES", regularMessages);
            regularMessageBtn.setBackgroundColor(Color.BLACK);
            context.startActivity(myIntent);
        }
    }

    private class Beacon {
        // Location in pixels
        private int a, b;

        private ArrayList<Area> coverAreas = new ArrayList<>();


        // Set location
        public Beacon(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }

        public void addArea(Area area){
            coverAreas.add(area);
        }
    }


    private class Circle {
        private double radius;


        public Circle(double radius) {
            this.radius = radius;
        }

        public double getRadius() {
            return radius;
        }

        public void setRadius(double radius) {
            this.radius = radius;
        }
    }
}
