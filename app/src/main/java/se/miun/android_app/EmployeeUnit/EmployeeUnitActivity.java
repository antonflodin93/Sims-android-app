package se.miun.android_app.EmployeeUnit;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import se.miun.android_app.Adapter.RegularMessageAdapter;
import se.miun.android_app.Api.ApiClient;
import se.miun.android_app.Api.ApiInterface;
import se.miun.android_app.Model.Floor;
import se.miun.android_app.Model.Message;
import se.miun.android_app.R;
import se.miun.android_app.Service.BuildingMessageService;
import se.miun.android_app.Service.RegularMessageService;
import se.miun.android_app.Service.WarningMessageService;
import se.miun.android_app.testing.Area;

public class EmployeeUnitActivity extends Activity implements View.OnClickListener {

    private static final int HTTP_RESPONSE_ACCEPTED = 200;

    public enum MessageType {
        WARNING, REGULAR
    }


    private ImageButton warningMessageBtn, regularMessageBtn;
    private Context context;
    private Intent regularMessageIntent, warningMessageIntent, objectMessageIntent;
    private ArrayList<Message> regularMessages;
    private LinearLayout floorplanLinearLayout;
    private RelativeLayout headerLayout;
    private ArrayList<Message> warningMessages, warningFloorMessages = new ArrayList<>();
    private ArrayList<Integer> messagesToBeAck = new ArrayList<>();
    private RecyclerView messageRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RegularMessageAdapter adapter;
    private int numOfReceievedRegularMessages, numOfReceievedWarningMessages;
    private boolean clickedButton = false;
    private Bitmap bmp;
    private EmployeeFloorPlanImageView employeeFloorPlanImageView;
    private MediaPlayer warningSignal, regularSignal;
    float xmax, ymax;
    private int floorId = 1;
    private int buildingId = 1;
    private Floor floor;
    int rowsize, collumnsize;
    private int employeeID;
    private boolean hasMinSdk;
    private boolean dialogActive = false;
    private boolean startedWarningService = false;
    private ArrayList<Area> locationAreas;
    private boolean enteredBuilding = false;

    //ble
    private BluetoothAdapter mBluetoothAdapter;
    private BleScanner mBleScanner;
    Map<String, Integer> scanResults;

    //triangulation
    Thread pollThread;  //needed for continues update of scan methods

    private ArrayList<Area> areas;
    private Beacon beacon1, beacon2, beacon3, beacon4;

    Map<String, Circle> circleContainer;

    //test of my location
    //store my location in x,y (area coordinate), occupied area >= 1
    private int[][] myLocation = new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
    };


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            // When receiving regular messages
            if (intent.getAction().equals("RegularMessageService")) {
                regularMessages = (ArrayList<Message>) intent.getSerializableExtra("messages");
                if (regularMessages.size() > numOfReceievedRegularMessages) {
                    regularMessageBtn.setImageResource(R.drawable.regularmessage_unread);
                    numOfReceievedRegularMessages = regularMessages.size();
                    regularSignal.start();


                }

                // When receiving warning messages
            } else if (intent.getAction().equals("WarningMessageService")) {
                warningMessages = (ArrayList<Message>) intent.getSerializableExtra("messages");
                if (warningMessages.size() > numOfReceievedWarningMessages) {
                    warningMessageBtn.setBackgroundColor(Color.RED);
                    numOfReceievedWarningMessages = warningMessages.size();

                    warningSignal.start();
                }
            } else if (intent.getAction().equals("BuildingMessageService")) {
                ArrayList<Message> tempMessages = (ArrayList<Message>) intent.getSerializableExtra("messages");
                // Check if there is any messages that is not acknowledged
                if (!tempMessages.isEmpty() && !dialogActive) {
                    displayDialog(tempMessages.get(0));
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_unit);
        context = this;

        employeeID = getIntent().getIntExtra("employeeId", 0);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            hasMinSdk = true;
        } else {
            hasMinSdk = false;
            Toast.makeText(context, "SDK version must be 21 or greater to be able to track location", Toast.LENGTH_SHORT).show();
        }

        // Init components
        warningMessageBtn = (ImageButton) findViewById(R.id.warningMessageBtn);
        warningMessageBtn.setOnClickListener(this);
        regularMessageBtn = (ImageButton) findViewById(R.id.regularMessageBtn);
        regularMessageBtn.setOnClickListener(this);
        floorplanLinearLayout = (LinearLayout) findViewById(R.id.floorplanLinearLayout);

        warningSignal = MediaPlayer.create(context, R.raw.warningsignal);
        regularSignal = MediaPlayer.create(context, R.raw.regularsignal);

        this.regularMessageIntent = new Intent(this, RegularMessageService.class);
        this.warningMessageIntent = new Intent(this, WarningMessageService.class);
        this.objectMessageIntent = new Intent(this, BuildingMessageService.class);


        // Get floor info and set imageview
        getFloorPlanInfo(floorId);


        setupAreas();

//Only start scans if minimum sdk is achieved
        if (hasMinSdk) {
            setupBeaconAndScanner();
        }

    }


    private void enterFloor(int floorId) {
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call;
        call = apiInterface.enterFloorEmployee(floorId, employeeID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == HTTP_RESPONSE_ACCEPTED) {


                } else {
                    try {
                        Toast.makeText(context, "Error: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enterBuilding(int buildingId) {
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call;
        call = apiInterface.enterBuildingEmployee(buildingId, employeeID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == HTTP_RESPONSE_ACCEPTED) {


                } else {
                    try {
                        Toast.makeText(context, "Error: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            // comment

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void exitBuilding() {
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call;
        call = apiInterface.exitBuildingEmployee(employeeID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == HTTP_RESPONSE_ACCEPTED) {


                } else {
                    try {
                        Toast.makeText(context, "Error: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void changeFloorplan(final int floorId){

        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call;
        call = apiInterface.changeFloorEmployee(floorId,employeeID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == HTTP_RESPONSE_ACCEPTED) {
                    getFloorPlanInfo(floorId);
                } else {
                    try {
                        Toast.makeText(context, "Error: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupAreas() {

        collumnsize = 8;
        rowsize = 10;
        //number of total areas

        locationAreas = new ArrayList<>();

        //get xmax and ymax for the first area
        xmax = 15 / collumnsize;
        ymax = 10 / rowsize;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        float height = display.getHeight() * 0.9f;
        Log.d("TEST", "" + height);
        float width = display.getWidth();
        float sizeX = height / rowsize;
        float sizeY = width / collumnsize;

        //size is used for area position in vector
        int size = 0;

        //add areas according to row and collumn sizes
        for (int c = 0; c < collumnsize; c++) {
            for (int r = 0; r < rowsize; r++) {
                Area area = new Area(xmax * (r), xmax * (r + 1), ymax * (c), ymax * (c + 1), c, r);
                area.setRealLimits(sizeY * c, sizeY * (c + 1), r * sizeX, sizeX * (r + 1));

                locationAreas.add(area);
            }
        }
    }

    // For tracking location
    private void setupBeaconAndScanner() {
        // The beacons locations
        beacon1 = new Beacon(7, 1, "EB:09:BD:0E:78:37"); // right side when entering building
        beacon2 = new Beacon(4, 9, "D7:1F:BE:CB:E0:16"); // between tanks
        beacon3 = new Beacon(0, 0, "D4:C4:D4:66:72:C5"); // At stairs
        beacon4 = new Beacon(0, 0, "EE:2B:8F:54:76:14");



        //init map containers
        scanResults = new HashMap<>();
        circleContainer = new HashMap<>();

        /* Check and enable bluetooth if it is disabled */
        if (!bluetoothEnable()) {
            requestBluetoothEnable();
        }
        //init bluetooth manager
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        //access android bluetooth object
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBleScanner = new BleScanner(scanResults, mBluetoothAdapter);
        //start scans
        mBleScanner.startScan(1);   // 0 for slow scan mode, 1 for fast scan mode

        //Create a new thread to run updateLocation()
        pollThread = new Thread() {
            @Override
            public void run() {
                //loop "forever"
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(5000);   // 5 seconds


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //run the scan update functions
                                processScanResults();
                                updateLocation();
                            }
                        });


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        //start the thread
        pollThread.start();
    }


    // Get floorplan from db
    private void getFloorPlanInfo(int floorId) {
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<Floor> call;
        call = apiInterface.getFloorById(floorId);
        call.enqueue(new Callback<Floor>() {
            @Override
            public void onResponse(Call<Floor> call, Response<Floor> response) {
                if (response.code() == HTTP_RESPONSE_ACCEPTED) {
                    floor = response.body();
                    // Set floorplan
                    if(employeeFloorPlanImageView != null){
                        floorplanLinearLayout.removeView(employeeFloorPlanImageView);
                    }
                    employeeFloorPlanImageView = new EmployeeFloorPlanImageView(context, floor.getFloorPlanFilePath(), floor.getObjects(), myLocation);
                    employeeFloorPlanImageView.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 0, 0.8f));
                    floorplanLinearLayout.addView(employeeFloorPlanImageView);
                    if(!startedWarningService) {
                        EmployeeUnitActivity.this.objectMessageIntent.putExtra("buildingId", buildingId);
                        EmployeeUnitActivity.this.objectMessageIntent.putExtra("employeeId", employeeID);
                        EmployeeUnitActivity.this.startService(EmployeeUnitActivity.this.objectMessageIntent);
                        startedWarningService = true;
                    }


                } else {
                    try {
                        Toast.makeText(context, "Error: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Floor> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // When user gets new floorplan messages
    private void displayDialog(final Message message) {
        warningSignal.start();
        warningSignal.start();
        dialogActive = true;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setTitle("ALERT: NEW WARNING MESSAGE, NEED CONFIRMATION");
        dialog.setMessage(message.getMessageText());
        dialog.setCancelable(false);
        dialog.setPositiveButton("Yes, I understand", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                acknowledgeMessage(Integer.parseInt(message.getMessageId()));
            }
        });

        dialog.show();

    }

    private void acknowledgeMessage(int messageId) {
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call;
        call = apiInterface.acknowledgeMessage(messageId, employeeID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == HTTP_RESPONSE_ACCEPTED) {
                    dialogActive = false;
                    Toast.makeText(EmployeeUnitActivity.this, "Acknowledged message", Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        Toast.makeText(context, "Error: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLocation() {
        //needs to be a callback function to function in this way,
        // otherwise need to implement everything in the employeeScanCallback class
        // and use the callbacks in there
        if (circleContainer != null) {

            //clear previous myLocation
            clearLocationArea();

            for (Map.Entry<String, Circle> entry : circleContainer.entrySet()) {
                String deviceAddress = entry.getKey();  //key
                Circle mCircle = entry.getValue();        //value

                //go trough location circles and estimate myLocation
                setLocationArea(mCircle);
            }

            //show myLocation with toast/log (test purposes)
            String locationz = " ";
            for(int y = 0; y < rowsize; y++) {
                locationz += "\n";
                for (int x = 0; x < collumnsize; x++) {
                    locationz += myLocation[y][x];
                }
            }
            Log.i("Location", locationz);

            //process myLocation ares into screen dots
            drawAreas();

        } else {
            Log.e("Container", "MAP circleContainer NULLPTR EXCEPTION");
        }

    }

    private void processScanResults() {

        if (scanResults != null) {

            //fetch results
            scanResults = mBleScanner.getResults();

            if (!circleContainer.isEmpty()) {
                circleContainer.clear();
            }

            //go trough the map
            for (Map.Entry<String, Integer> entry : scanResults.entrySet()) {
                String deviceAddress = entry.getKey();      //key
                int rssi = entry.getValue();                //value
                Log.i("AvgRssi", deviceAddress + " " + String.valueOf(rssi));


                //get distance in meters from beacon
                double dist = getDistance(rssi);
                Log.i("distance", deviceAddress + " " + dist);
                //convert from meters to area blocks
                int distArea = meterToAreaBlockDistance(dist, xmax);
                //create new circle object
                Circle nCircle = new Circle(dist);

                //calculate the area coverage of the circle
                //compare to beacon position (1,2,3 => first floor, >4 second floor)
                if (deviceAddress.equals(beacon1.getDeviceID())) {
                    getAreasInCircle(distArea, beacon1, nCircle);
                    Log.i("beacon", "Using Beacon 1");

                } else if (deviceAddress.equals(beacon2.getDeviceID())) {
                    getAreasInCircle(distArea, beacon2, nCircle);
                    Log.i("beacon", "Using Beacon 2");

                } else if (deviceAddress.equals(beacon3.getDeviceID())) {
                    getAreasInCircle(distArea, beacon3, nCircle);
                    Log.i("beacon", "Using Beacon 3");

                } else if (deviceAddress.equals(beacon4.getDeviceID())) {
                    // Check which floor employee goes to
                    if (rssi < -70) {
                        if(floorId != 1){
                            floorId = 1;
                            getFloorPlanInfo(floorId);

                            Log.i("beacon", "Beacon4 to weak SNR");

                            changeFloorplan(floorId);

                        }

                        Log.i("beacon", "Using Beacon 4");

                    } else {
                        if(floorId != 2){
                            floorId = 2;
                            getFloorPlanInfo(floorId);
                            changeFloorplan(floorId);
                        }
                    }
                } else {

                    //getAreasInCircle(distArea, beacon1, nCircle);
                    Log.e("LegalDevice", "Device ID not in list");

                }

                //store new circles in map, clear map if needed
                circleContainer.put(deviceAddress, nCircle);
            }
        } else {
            Log.e("Container", "MAP scanResults NULLPTR EXCEPTION");
        }
    }


    //check if bluetooth is enabled or disabled
    private boolean bluetoothEnable() {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            return false;
        }
        return true;
    }

    //request enable / turn on bluetooth
    private void requestBluetoothEnable() {
        // displays a dialog requesting user permission to enable Bluetooth.
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        int REQUEST_ENABLE_BT = 1;
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    private void getAreasInCircle(int radius, Beacon beacon, Circle circle) {

        // Iterate through every area
        for (int x = 0; x < collumnsize; x++) {
            for (int y = 0; y < rowsize; y++) {
                //compare (using circle equation)
                if ((x - beacon.a) * (x - beacon.a) + (y - beacon.getB()) * (y - beacon.getB()) <= radius * radius) {
                    //store occupied area inside the circle
                    circle.setOccupiedArea(x, y);
                }
                //this is for reuse, clear area "between" measurements.
                //might be unnecessary...
                else {
                    circle.clearOccupiedArea(x, y);
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
    private void setLocationArea(Circle circle) {
        //int xAreas=8, yAreas= 10;  //replace with xMax, yMax for dynamic control...

        // Iterate through every areas
        for (int x = 0;  /*#nr or areas in x direction*/ x < collumnsize; x++) {
            for (int y = 0; /*#nr of areas in y direction*/ y < rowsize; y++) {
                //compare all circles
                if (circle.getArea(x, y) == 1) {
                    //set myLocation to +1 in specified area
                    myLocation[y][x] += 1;
                }
            }
        }
    }

    private void clearLocationArea() {
        for (int x = 0; x < collumnsize; x++) {
            for (int y = 0; y < rowsize; y++) {
                myLocation[y][x] = 0;
            }
        }
    }

    private int meterToAreaBlockDistance(double rssiMeters, float areaUnitsPerMeter) {
        //need to know areas/meter from floorplans, pass in as distanceToPixelCount...
        //as in 1 meter represents x amount of pixels(area blocks) in x or y direction.
        //  (# area count in x direction )/ (# meters  )
        return Math.round((float) rssiMeters / areaUnitsPerMeter);
        //xAreaPerMeter = xmax
    }

    // EXPERIMENTALLY DEVELOPED FORMULA
    // Returns distance for a given rssi
    double getDistance(int rssi) {
        //check for max? distance...
        if (rssi < -100) {
            return 15.0;
        } else {
            double e = 0.6859;
            double b = Math.pow(2389, e);
            double n = Math.pow((4447 + 50 * rssi), e);
            return (b / n)+0.5;
        }
    }


    public void onResume() {
        super.onResume();

        this.startService(this.warningMessageIntent);
        this.startService(this.regularMessageIntent);
        EmployeeUnitActivity.this.regularMessageIntent.putExtra("employeeId", employeeID);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("RegularMessageService");
        intentFilter.addAction("WarningMessageService");
        intentFilter.addAction("BuildingMessageService");
        this.registerReceiver(this.broadcastReceiver, intentFilter);

        // Simulate that user enters building 1 and floor 1
        enterBuilding(buildingId);
        enterFloor(floorId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // User exits building
        exitBuilding();
    }

    public void onPause() {
        super.onPause();
        if (!clickedButton) {
            this.unregisterReceiver(this.broadcastReceiver);
            this.stopService(this.regularMessageIntent);
            this.stopService(this.warningMessageIntent);
            this.stopService(this.objectMessageIntent);
            // User exits building
            exitBuilding();
        }

        // Maybee needed
        //this.unregisterReceiver(this.broadcastReceiver);

        if (hasMinSdk) {
            //stop polling the scan update functions
            pollThread.interrupt();
        }

    }

    @Override
    public void onClick(View v) {
        clickedButton = true;
        if (v.getId() == R.id.warningMessageBtn) {
            Intent myIntent = new Intent(getApplicationContext(), ShowMessagesActivity.class);
            myIntent.putExtra("MESSAGETYPE", MessageType.WARNING);
            myIntent.putExtra("WARNINGMESSAGES", warningMessages);
            myIntent.putExtra("employeeId", employeeID);
            warningMessageBtn.setBackgroundColor(Color.BLACK);
            context.startActivity(myIntent);

        } else if (v.getId() == R.id.regularMessageBtn) {
            Intent myIntent = new Intent(getApplicationContext(), ShowMessagesActivity.class);
            myIntent.putExtra("MESSAGETYPE", MessageType.REGULAR);
            myIntent.putExtra("REGULARMESSAGES", regularMessages);
            regularMessageBtn.setImageResource(R.drawable.regularmessage);
            context.startActivity(myIntent);
        }
    }


    private class Beacon {
        // Location in pixels
        private int a, b;
        private String deviceID;
        private ArrayList<Area> coverAreas = new ArrayList<>();


        // Set location
        public Beacon(int a, int b, String deviceID) {
            if (deviceID == null) {
                this.deviceID = "00:00:00:00:00:00";
                Log.e("555", "No Device ID set, Setting default 00:");
            } else {
                this.deviceID = deviceID;
            }
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

        public void addArea(Area area) {
            coverAreas.add(area);
        }

        public String getDeviceID() {
            return deviceID;
        }
    }


    private class Circle {
        private double radius;

        //Area of floorplan, (8x10 default) containing circle area of detected beacon.
        private int[][] circleArea = new int[collumnsize][rowsize];

        public Circle(double radius) {
            this.radius = radius;
        }

        public double getRadius() {
            return radius;
        }

        public void setRadius(double radius) {
            this.radius = radius;
        }

        public void setOccupiedArea(int x, int y) {
            //set specified area as occupied by the beacon circle
            this.circleArea[x][y] = 1;
        }

        public void clearOccupiedArea(int x, int y) {
            this.circleArea[x][y] = 0;
        }

        public int getArea(int x, int y) {
            return circleArea[x][y];
        }
    }

    public void drawAreas() {
        Log.d("MYAPP", "UPDATE LOCATIOn");

        // find the maximum value for beacons
        int locationmax = 0;
        for (int x = 0; x < collumnsize; x++) {
            for (int y = 0; y < rowsize; y++) {
                if (myLocation[y][x] > locationmax) {
                    locationmax = myLocation[y][x];
                    Log.d("MYAPP", " location max" + x + ", " + y);
                }
            }
        }

        ArrayList<Area> currentLocationAreas = new ArrayList<>();

        //draws rectangle on locations
        int currentarea = 0;
        if (locationmax > 0) {
            for (int x = 0; x < collumnsize; x++) {
                for (int y = 0; y < rowsize; y++) {
                    if (myLocation[y][x] == locationmax) {
                        int row = locationAreas.get(currentarea).getrow();
                        int column = locationAreas.get(currentarea).getcollumn();
                        for (Area area : locationAreas) {
                            if (area.getcollumn() == column && area.getrow() == row) {
                                currentLocationAreas.add(area);
                                Log.d("MYAPP", "Inside " + row + ", " + column);
                            }
                        }

                    }
                    currentarea++;
                }
            }
        }

        if (employeeFloorPlanImageView != null) {
            if (currentLocationAreas != null) {
                employeeFloorPlanImageView.drawNewLocation(currentLocationAreas);
            } else {
                Log.d("MYAPP", "currentLocationAreas null");
            }
        } else {
            Log.d("MYAPP", "imageview null");
        }
    }

}