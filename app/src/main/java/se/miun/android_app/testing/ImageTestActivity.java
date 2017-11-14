package se.miun.android_app.testing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import android.widget.ImageView;


import java.util.Vector;

import se.miun.android_app.R;

public class ImageTestActivity extends Activity implements SeekBar.OnSeekBarChangeListener, View.OnTouchListener {
    private SeekBar sbDemo;
    private ImageView iv;
    int windowwidth;
    int windowheight;
    Context context;
    int THRESHOLDVALUE_SEEKBAR = 20;
    int MAXVALUE_DRAG_X = 1000;
    float zoomfactor;
    float xstart = 0, ystart = 0, diffy, diffx, totalDraggedX = 0, totalDraggedY = 0;
    private GestureDetector mGestureDetector;
    private View.OnTouchListener gestureListener;
    private float totalScrolledX, totalScrolledY;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagetest);
        context = this;

        iv = (ImageView) findViewById(R.id.mapImage);
        sbDemo = (SeekBar) findViewById(R.id.sb_demo);
        sbDemo.setOnSeekBarChangeListener(this);

        windowwidth = getWindowManager().getDefaultDisplay().getWidth();
        windowheight = getWindowManager().getDefaultDisplay().getHeight();
        /*

        mGestureDetector = new GestureDetector(this,
                new GestureDetector.OnGestureListener() {
                    @Override
                    public boolean onDown(MotionEvent e) {

                        return true;
                    }



                    @Override
                    public void onShowPress(MotionEvent e) {
                        //toast("ON ShowPress");

                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        //toast("ON SINGLETAPUP");
                        return false;
                    }

                    // e1 The first down motion event that started the scrolling.
                    // e2 The move motion event that triggered the current onScroll.
                    // distance x The distance along the X axis that has been scrolled since the last call to onScroll.

                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {


                        xstart = e1.getX();
                        ystart = e1.getY();

                        float currentdistance = (e2.getX()-xstart);

                        totalScrolledX += currentdistance;
                        //toast("Total: " + totalScrolledX);

                        diffx = xstart - iv.getX();
                        diffy = ystart - iv.getY();

                        if(totalScrolledX < MAXVALUE_DRAG_X && totalScrolledX > -MAXVALUE_DRAG_X){
                            iv.setX(e2.getX() - diffx);
                            iv.setY(e2.getY() - diffy);
                        } else{
                            totalScrolledX -= currentdistance;
                        }





                        return true;
                    }


                    @Override
                    public void onLongPress(MotionEvent e) {
                        toast("ON LONG PRESS");

                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        //toast("" + (e1.getX() - e2.getX()));


                        return true;
                    }
                });

        gestureListener = new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    toast("Total: " + totalScrolledX);
                    return true;
                } else if(mGestureDetector.onTouchEvent(event)){
                    return true;
                }
                return false;
            }
        };

        iv.setOnTouchListener(gestureListener);

    }
    */

        iv.setOnTouchListener(this);


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
                       // Toast.makeText(ImageTestActivity.this, "Clicked Area: " + areas.get(i).getrow() + ", " + areas.get(i).getcollumn() + ", Coordinates: " + px + ", " + py +". zoomfactor: "+ zoomfactor, Toast.LENGTH_SHORT).show();
                    }
                }

                //Toast.makeText(ImageTestActivity.this, "Clicked: " + areas.get(3).getxmin() + ", " + areas.get(3).getxmax() , Toast.LENGTH_SHORT).show();
                // Toast.makeText(ImageTestActivity.this, "Clicked: " + xmax + ", " + ymax , Toast.LENGTH_SHORT).show();
                break;

            case MotionEvent.ACTION_MOVE:


                diffx = xstart - view.getX();
                diffy = ystart - view.getY();

                float currentDragX = totalDraggedX + (event.getX()-xstart);
                float currentDragY = totalDraggedY + (event.getY()-ystart);

                if (zoomfactor > 1 && inRange(currentDragX, currentDragY)) {
                    float tempx = currentDragX;
                    float tempy = currentDragY;
                    totalDraggedX = tempx;
                    totalDraggedY = tempy;

                    view.setX(event.getX() - diffx);
                    view.setY(event.getY() - diffy);
                }





            /*
                float currentdistancex = (event.getX()-xstart);

                totalScrolledX += currentdistancex;
                //toast("Total: " + totalScrolledX);

                diffx = xstart - iv.getX();
                diffy = ystart - iv.getY();

                if(totalScrolledX < MAXVALUE_DRAG_X && totalScrolledX > -MAXVALUE_DRAG_X){
                    iv.setX(event.getX() - diffx);
                    iv.setY(event.getY() - diffy);
                } else{
                    totalScrolledX -= currentdistancex;
                }

*/




                break;
        }
        return true;
    }

    private boolean inRange(float xvalue, float yvalue) {
        if(xvalue < MAXVALUE_DRAG_X && xvalue > -MAXVALUE_DRAG_X && yvalue < MAXVALUE_DRAG_X && yvalue > -MAXVALUE_DRAG_X ){
            return true;
        } else{
            toast("Not in range: " + xvalue + ", " + yvalue);
        }
        return false;

    }

    public void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    } // end toast


    @Override
    public void onProgressChanged(SeekBar sbDemo, int progress, boolean fromUser) {
    /*
        if (progress > THRESHOLDVALUE_SEEKBAR) {
            //scaleValue = (float) (progress)/20f;
            iv.setScaleX(((float) (progress) / 20f));
            iv.setScaleY(((float) (progress) / 20f));
            zoomfactor = progress / 20f;
            totalDraggedX = 0;
            totalDraggedY = 0;
            totalScrolledX = 0;
            totalScrolledY = 0;
        } else {
            //set scale back to default
            iv.setScaleX(((float) 1));
            iv.setScaleY(((float) 1));
            //set viewpoint back to default
            iv.setX(0);
            iv.setY(0);
            zoomfactor = progress / 20f;
            totalDraggedX = 0;
            totalDraggedY = 0;
            totalScrolledX = 0;
            totalScrolledY = 0;
        }
*/
        double scalefactor = 1;
        if(progress >=1  && progress < 25){
            scalefactor = 1;
            toast("" + progress);


        } else if(progress >= 25 && progress < 50){
            scalefactor = 1.25;
            toast("" + progress);
        } else if(progress >= 50 && progress < 75){
            scalefactor = 2;
            toast("" + progress);
        }

        else if(progress >= 75 && progress < 100){
            scalefactor = 3;
            toast("" + progress);
        }

        iv.setScaleX((float) scalefactor);
        iv.setScaleY((float) scalefactor);


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();

        if (progress < THRESHOLDVALUE_SEEKBAR) {
            //set scale back to default
            iv.setScaleX(((float) 1));
            iv.setScaleY(((float) 1));
            //set viewpoint back to default
            iv.setX(0);
            iv.setY(0);
            totalDraggedX = 0;
            totalDraggedY = 0;
        }
    }
}
