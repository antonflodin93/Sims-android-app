package se.miun.android_app.testing;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Toast;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import android.widget.ImageView;

import java.util.Vector;

import se.miun.android_app.R;

public class ImageTestActivity extends Activity implements View.OnTouchListener {



    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagetest);

        ImageView iv = (ImageView) findViewById (R.id.mapImage);
        if (iv != null) {
            iv.setOnTouchListener (this);
        }

    }




    // When the screen has been touched
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        super.onTouchEvent(event);
        final int eventAction = event.getAction();
        switch(eventAction) {
            case MotionEvent.ACTION_DOWN:




                // Get the coordinates of the point of touch
                float x = event.getX();
                float y = event.getY();

                //creates an object dm that contain information about devicemetrics
                DisplayMetrics dm  = getResources().getDisplayMetrics();
                //gets maximum width and height of device in terms of pixels
                float w = dm.widthPixels;
                float h = dm.heightPixels;

                //px is the x coordinate varying from 0-100 on screen touch, py is the same
                float px = (x/w)*100;
                float py = (y/h)*100;

                int rowsize = 4;
                int collumnsize = 6;
                //number of total areas
                int areasize = rowsize*collumnsize;

                Vector<Area> areas = new Vector<>(areasize);

                //get xmax and ymax for the first area, hw is used since
                float xmax = 100/rowsize;
                float ymax = 100/collumnsize;

                //size is used for area position in vector
                int size = 0;

                //add areas according to row and collumn sizes
                for(int c = 0; c<collumnsize; c++){
                    for(int r = 0 ; r<rowsize; r++){
                        areas.add(size, new Area(xmax*(r), xmax*(r+1), ymax*(c), ymax*(c+1), r+1, c+1));
                        size++;
                    }
                }


                //depending on where the screen is touched, write which area that was touched
                for(int i = 0; i<areasize; i++){
                    if (px > areas.get(i).getxmin() && px < areas.get(i).getxmax() && areas.get(i).getymin() < py && areas.get(i).getymax() > py ){
                        Toast.makeText(ImageTestActivity.this, "Clicked Area: " + areas.get(i).getrow() + ", " + areas.get(i).getcollumn()  + ", Coordinates: " + px  + ", " + py, Toast.LENGTH_SHORT).show();
                    }
                }

                //Toast.makeText(ImageTestActivity.this, "Clicked: " + areas.get(3).getxmin() + ", " + areas.get(3).getxmax() , Toast.LENGTH_SHORT).show();
                // Toast.makeText(ImageTestActivity.this, "Clicked: " + xmax + ", " + ymax , Toast.LENGTH_SHORT).show();

                break;


        }
        return true;
    }

    public void toast (String msg)
    {
        Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_LONG).show ();
    } // end toast

    static float pxToDp(Context context, float px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics());
    }

}
