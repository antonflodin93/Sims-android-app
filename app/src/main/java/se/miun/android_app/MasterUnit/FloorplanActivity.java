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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

import se.miun.android_app.R;
import se.miun.android_app.testing.Area;

public class FloorplanActivity extends Activity{
    private ImageView floorplanImageView;
    private TextView textViewFloorPlan;
    private LinearLayout floorplanLinearLayout;
    private String filePath;
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

        //floorplanImageView = (ImageView) findViewById(R.id.floorplanImageView);
        textViewFloorPlan = (TextView) findViewById(R.id.textViewFloorPlan);
        floorplanLinearLayout = (LinearLayout) findViewById(R.id.floorplanLinearLayout);

        textViewFloorPlan.setText(building + "/" + floor + " (" + numofemployees + ")");

        floorplanImageView = new FloorplanImageView(context, filePath);

        floorplanLinearLayout.addView(floorplanImageView);
    }



}
