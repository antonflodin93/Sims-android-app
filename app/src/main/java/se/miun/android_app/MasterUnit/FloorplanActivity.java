package se.miun.android_app.MasterUnit;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import se.miun.android_app.Adapter.ObjectAdapter;
import se.miun.android_app.Model.FactoryObject;
import se.miun.android_app.Model.Floor;
import se.miun.android_app.R;

public class FloorplanActivity extends Activity implements View.OnClickListener{
    static private ImageView floorplanImageView, floorplanImageViewDrawed;
    private TextView textViewFloorPlan;
    private LinearLayout floorplanLinearLayout;
    private String filePath;
    private Context context;
    private int numofemployees = 0;
    private String building;
    private Floor floor;
    private ArrayList<FactoryObject> objects;
    private ObjectAdapter objectAdapter;
    private Button listObjectsBtn;
    private ListView objectListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floorplan);
        context = this;
        filePath = getIntent().getStringExtra("filePath");
        building = getIntent().getStringExtra("building");
        floor = (Floor) getIntent().getSerializableExtra("floor");

        // Get objects for the floorplan
        objects = floor.getObjects();

        textViewFloorPlan = (TextView) findViewById(R.id.textViewFloorPlan);
        floorplanLinearLayout = (LinearLayout) findViewById(R.id.floorplanLinearLayout);
        textViewFloorPlan.setText(building + "/" + floor.getFloorLevel() + " (" + numofemployees + ")");
        floorplanImageView = new FloorplanImageView(context, filePath, objects);
        floorplanLinearLayout.addView(floorplanImageView);
        listObjectsBtn = (Button) findViewById(R.id.listObjectsBtn);
        listObjectsBtn.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.listObjectsBtn){
            // Display dialog
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
            builderSingle.setCancelable(false);
            builderSingle.setIcon(android.R.drawable.ic_dialog_info);
            builderSingle.setTitle("Objects in " + building + "/" + floor.getFloorLevel());

            objectAdapter= new ObjectAdapter (FloorplanActivity.this, 0, objects);
            //objectListView.setAdapter(objectAdapter);

            builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });



            builderSingle.setAdapter(objectAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    floorplanLinearLayout.removeView(floorplanImageView);
                    floorplanImageView = new FloorplanImageView(context, filePath, objects, objectAdapter.getItem(which));
                    floorplanLinearLayout.addView(floorplanImageView);

                    //Toast.makeText(context, "CLICKED " + objectAdapter.getItem(which).getObjectName(), Toast.LENGTH_SHORT).show();


                }
            });
            builderSingle.show();
        }
    }
}
