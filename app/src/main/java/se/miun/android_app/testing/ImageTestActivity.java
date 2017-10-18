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

                //Convert pixels to screen independent coordinates 1,33. 1,0
                //x = pxToDp(ImageTestActivity.this, x);
                //y = pxToDp(ImageTestActivity.this, y);



                //toast ("Touch the screen to discover where the regions are.");
                Toast.makeText(ImageTestActivity.this, "Clicked: " + px + ", " + py , Toast.LENGTH_SHORT).show();
               // Toast.makeText(ImageTestActivity.this, dpi + ", " , Toast.LENGTH_SHORT).show();

                //drawImage(x, y);
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
