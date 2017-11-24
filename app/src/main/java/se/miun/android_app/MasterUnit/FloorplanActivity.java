package se.miun.android_app.MasterUnit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

import se.miun.android_app.R;

public class FloorplanActivity extends AppCompatActivity {
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

        textViewFloorPlan.setText(building + "/" + floor + " (" + numofemployees + ")");



    }


}