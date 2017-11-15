package se.miun.android_app.MasterUnit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

import se.miun.android_app.R;
import se.miun.android_app.testing.Area;

public class FloorplanActivity extends Activity implements View.OnTouchListener{
    private ImageView floorplanImageView;
    private TextView textViewFloorPlan;
    private String filePath;
    private String IP_ADDRESS = "http://193.10.119.34:8080";
    private Context context;
    private int numofemployees = 0;
    private String building, floor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floorplan);
        context = this;
        filePath = getIntent().getStringExtra("filePath");
        building = getIntent().getStringExtra("building");
        floor = getIntent().getStringExtra("floor");

        floorplanImageView = (ImageView) findViewById(R.id.floorplanImageView);
        textViewFloorPlan = (TextView) findViewById(R.id.textViewFloorPlan);

        new AsyncTask<Void, Void, Void>() {
            Bitmap bmp;
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    InputStream in = new URL(IP_ADDRESS + filePath).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Toast.makeText(context, "Error loading image from server", Toast.LENGTH_LONG).show();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (bmp != null)
                    floorplanImageView.setImageBitmap(bmp);
            }

        }.execute();

        if(floorplanImageView != null){
            floorplanImageView.setOnTouchListener(this);
        }

        textViewFloorPlan.setText(building + "/" + floor + " (" + numofemployees + ")");



    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        super.onTouchEvent(event);
        final int eventAction = event.getAction();
        switch (eventAction) {


            case MotionEvent.ACTION_DOWN:

                // Get the coordinates of the point of touch
                float x = event.getX();
                float y = event.getY();

                //creates an object dm that contain information about devicemetrics
                DisplayMetrics dm = getResources().getDisplayMetrics();
                //gets maximum width and height of device in terms of pixels
                float w = dm.widthPixels;
                //50 is seekbar height
                float h = dm.heightPixels - 50;

                //px is the x coordinate varying from 0-100 on screen touch, py is the same
                float px = (x / w) * 100;
                float py = (y / h) * 100;

                int collumnsize = 8;
                int rowsize = 10;
                //number of total areas
                int areasize = rowsize * collumnsize;

                Vector<Area> areas = new Vector<>(areasize);

                //get xmax and ymax for the first area
                float xmax = 100 / collumnsize;
                float ymax = 100 / rowsize;

                //size is used for area position in vector
                int size = 0;

                //add areas according to row and collumn sizes
                for (int c = 0; c < rowsize; c++) {
                    for (int r = 0; r < collumnsize; r++) {
                        areas.add(size, new Area(xmax * (r), xmax * (r + 1), ymax * (c), ymax * (c + 1), r + 1, c + 1));
                        size++;
                    }
                }

                //depending on where the screen is touched, write which area that was touched
                for (int i = 0; i < areasize; i++) {
                    if (px > areas.get(i).getxmin() && px < areas.get(i).getxmax() && areas.get(i).getymin() < py && areas.get(i).getymax() > py) {
                        Toast.makeText(context, "Clicked Area: " + areas.get(i).getrow() + ", " + areas.get(i).getcollumn() + ", Coordinates: " + px + ", " + py, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        return false;
    }
}
