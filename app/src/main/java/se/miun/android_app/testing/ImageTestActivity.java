package se.miun.android_app.testing;

import android.app.Activity;
import android.content.Context;
import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import java.util.Vector;


import se.miun.android_app.R;

import static android.R.attr.bitmap;
import static android.R.attr.progress;

public class ImageTestActivity extends Activity implements View.OnTouchListener, SeekBar.OnSeekBarChangeListener {
    private SeekBar sbDemo;
    private ImageView iv;
    int windowwidth;
    int windowheight;
    Context context;
    int THRESHOLDVALUE_SEEKBAR = 20;
    float zoomfactor;
    float xstart, ystart, xend, yend, diffy, diffx, xb, yb;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagetest);

        iv = (ImageView) findViewById(R.id.mapImage);
        sbDemo = (SeekBar) findViewById(R.id.sb_demo);
        sbDemo.setOnSeekBarChangeListener(this);
        sbDemo.setProgress(THRESHOLDVALUE_SEEKBAR);

        windowwidth = getWindowManager().getDefaultDisplay().getWidth();
        windowheight = getWindowManager().getDefaultDisplay().getHeight();



        if (iv != null) {
            iv.setOnTouchListener(this);
        }

        context = this;

    }


    // When the screen has been touched
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        super.onTouchEvent(event);
        final int eventAction = event.getAction();
        switch (eventAction) {

            case MotionEvent.ACTION_DOWN:
                xstart = event.getX();
                ystart = event.getY();
                xb = 0;
                yb = 0;

                // Get the coordinates of the point of touch
                float x = event.getX();
                float y = event.getY();

                //creates an object dm that contain information about devicemetrics
                DisplayMetrics dm = getResources().getDisplayMetrics();
                //gets maximum width and height of device in terms of pixels
                float w = dm.widthPixels;
                //50 is seekbar height
                float h = dm.heightPixels-50;

                //px is the x coordinate varying from 0-100 on screen touch, py is the same
                float px = (x / w) * 100;
                float py = (y / h) * 100;

                int collumnsize = 8;
                int rowsize = 10;
                //number of total areas
                int areasize = rowsize * collumnsize;

                Vector<Area> areas = new Vector<>(areasize);

                //get xmax and ymax for the first area, hw is used since
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
                        Toast.makeText(ImageTestActivity.this, "Clicked Area: " + areas.get(i).getrow() + ", " + areas.get(i).getcollumn() + ", Coordinates: " + px + ", " + py +". zoomfactor: "+ zoomfactor, Toast.LENGTH_SHORT).show();
                    }
                }

                //Toast.makeText(ImageTestActivity.this, "Clicked: " + areas.get(3).getxmin() + ", " + areas.get(3).getxmax() , Toast.LENGTH_SHORT).show();
                // Toast.makeText(ImageTestActivity.this, "Clicked: " + xmax + ", " + ymax , Toast.LENGTH_SHORT).show();
                break;

            case MotionEvent.ACTION_MOVE:
                //enable navigation with drag if you have a zoomed view

                if(zoomfactor>1 ) {
                    //calculates the difference between start touch and where the finger moves
                    diffx = xstart - view.getX();
                    diffy = ystart - view.getY();
                    xb = xb + diffx;
                    yb = yb + diffy;
                    //sets view continuously on touching move based on difference from above
                    view.setX(event.getX() - diffx);
                    view.setY(event.getY() - diffy);
                }
                break;

            case MotionEvent.ACTION_UP:

                break;

        }
        return true;
    }

    public void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    } // end toast

    static float pxToDp(Context context, float px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onProgressChanged(SeekBar sbDemo, int progress, boolean fromUser) {

        if(progress > THRESHOLDVALUE_SEEKBAR){
            //scaleValue = (float) (progress)/20f;
            iv.setScaleX(((float) (progress)/20f));
            iv.setScaleY(((float) (progress)/20f));
            zoomfactor = progress/20f;
        } else{
            //set scale back to default
            iv.setScaleX(((float) 1));
            iv.setScaleY(((float) 1));
            //set viewpoint back to default
            iv.setX(0);
            iv.setY(0);
            zoomfactor = progress/20f;
            xb = 0;
            yb = 0;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();

        if(progress < THRESHOLDVALUE_SEEKBAR) {
            //set scale back to default
            iv.setScaleX(((float) 1));
            iv.setScaleY(((float) 1));
            //set viewpoint back to default
            iv.setX(0);
            iv.setY(0);
            xb = 0;
            yb = 0;
        }
        /*
        int progress = seekBar.getProgress();
        iv.setScaleX(((float) (progress) / 20f));
        iv.setScaleY(((float) (progress) / 20f));
        Toast.makeText(context, "progress: " + progress, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "progress: " + progress/20f, Toast.LENGTH_SHORT).show();
        */

    }

}
