package se.miun.android_app.EmployeeUnit;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

import se.miun.android_app.Adapter.RegularMessageAdapter;
import se.miun.android_app.Model.Message;
import se.miun.android_app.R;
import se.miun.android_app.Service.RegularMessageService;
import se.miun.android_app.Service.WarningMessageService;
import se.miun.android_app.testing.Area;

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


    private ArrayList<Area> areas;
    private Beacon beacon1, beacon2, beacon3;
    private Circle testCircle;
    //test of my location
    //store my location in x,y (area coordinate), occupied area = 1
    private int[][] myLocation = new int[][]{
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
    };



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

        /*

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL("http://193.10.119.34:8080/WS/webapi/floorplans/testimage.png").openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // log error
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (bmp != null)
                    mapImageView.setImageBitmap(bmp);
            }

        }.execute();
        */

        // The beacons locations
        beacon1 = new Beacon(0, 0);
        //beacon2 = new Beacon(width, 0);
        //beacon3 = new Beacon(width, height);

        // Get values from all 3 beacons
        int rssiB1 = -85;
        int rssiB2 = -95;
        int rssiB3 = -90;

        // Get radius from the beacons (in pixels)
        double radius1 = getDistance(rssiB1);
        double radius2 = getDistance(rssiB2);
        double radius3 = getDistance(rssiB3);

        //get radius in area blocks
        int radiiArea1 = meterToAreaBlockDistance(radius1, 35/8);

        //test with occupied circle Area
        testCircle = new Circle(0);
        testCircle.setRadius(getDistance(-85));
        // Create circles and output x and y that is within the circle
        getAreasInCircle( radiiArea1, beacon1, testCircle); /*test...*/
//
//        getAreasInCircle((int) Math.floor(radius2), beacon2);
//        getAreasInCircle((int) Math.floor(radius3), beacon3);


    }



    private void getAreasInCircle(int radius, Beacon beacon, Circle circle) {
        int xAreas=8, yAreas= 10;  //replace with xMax, yMax for dynamic control...

        // Iterate through every areas
        for (int x = 0;  /*#nr or areas in x direction*/ x < xAreas; x++) {
            for(int y = 0; /*#nr of areas in y direction*/ y < yAreas; y++){
                //compare (circle equation)...
                if( (x-beacon.a)*(x-beacon.a)+(y-beacon.getB())*(y-beacon.getB()) <= radius*radius ){
                    //store occupied area inside the circle
                    circle.setArea(x,y);
                }
            }
        }
    }
    //need to store every circle area somewhere in an array and pull everyone in there to compare
    //for an accurate assessment of the location...
    //one to do this would be to add +1 for every beacon found, thus
    //you could just print out the highest value in the matrix.
    //for example if the area reads: +3 then you know 3 beacons are detected in that
    //area and thus it is highly likely you are in that area.
    private void setLocationArea(Circle circle){
        int xAreas=8, yAreas= 10;  //replace with xMax, yMax for dynamic control...

        // Iterate through every areas
        for (int x = 0;  /*#nr or areas in x direction*/ x < xAreas; x++) {
            for(int y = 0; /*#nr of areas in y direction*/ y < yAreas; y++){
                //compare all circles
                if( circle.getArea(x,y) == 1 ){
                    //set myLocation to +1 in specified area
                    myLocation[x][y] += 1;
                }
            }
        }
    }
    private void clearLocationArea(){
        for(int x=0;x<8;x++){
            for(int y=0;y<10;y++){
                myLocation[x][y] = 0;
            }
        }
    }

    private int meterToAreaBlockDistance(double rssiMeters, float xAreaPerMeter){
        //need to know areas/meter from floorplans, pass in as distanceToPixelCount...
        //as in 1 meter represents x amount of pixels(area blocks) in x or y direction.
        //  (# area count in x direction )/ (# meters  )
        return Math.round( (float)rssiMeters / xAreaPerMeter );
        //xAreaPerMeter = xmax
    }

    // EXPERIMENTALLY DEVELOPED FORMULA
    // Returns distance for a given rssi
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
        if(!clickedButton){
            this.unregisterReceiver(this.broadcastReceiver);
            this.stopService(this.regularMessageIntent);
            this.stopService(this.warningMessageIntent);
        }

        // Maybee needed
        //this.unregisterReceiver(this.broadcastReceiver);
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

        //Area of floorplan, (8x10 default) containing circle area of detected beacon.
        private int[][] circleArea = new int[8][10];

        public Circle(double radius) {
            this.radius = radius;
        }

        public double getRadius() {
            return radius;
        }

        public void setRadius(double radius) {
            this.radius = radius;
        }

        public void setArea(int x, int y){
            //set specified area as occupied by the beacon circle
            this.circleArea[x][y] = 1;
        }
        public int getArea(int x, int y){
            return circleArea[x][y];
        }
    }


}
