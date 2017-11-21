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

    private float previousX;
    private float previousY;


    private int MAXVALUE_DRAG;


    private enum ScaleFactorType {
        ZOOM0,
        ZOOM1,
        ZOOM2,
        ZOOM3;
    }

    ScaleFactorType scaleFactorType;

    float zoomfactor;
    float xstart = 0, ystart = 0, diffy, diffx;
    private GestureDetector mGestureDetector;
    private View.OnTouchListener gestureListener;
    private float totalScrolledX, totalScrolledY;
    private float originalX, originalY;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagetest);
        context = this;
        iv = (ImageView) findViewById(R.id.mapImage);
        originalX = iv.getX();
        originalY = iv.getY();
        sbDemo = (SeekBar) findViewById(R.id.sb_demo);
        sbDemo.setOnSeekBarChangeListener(this);

        windowwidth = getWindowManager().getDefaultDisplay().getWidth();
        windowheight = getWindowManager().getDefaultDisplay().getHeight();

        MAXVALUE_DRAG = 40;
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
                        Toast.makeText(ImageTestActivity.this, "Clicked Area: " + areas.get(i).getrow() + ", " + areas.get(i).getcollumn() + ", Coordinates: " + px + ", " + py +". zoomfactor: "+ zoomfactor, Toast.LENGTH_SHORT).show();
                    }
                }

                //Toast.makeText(ImageTestActivity.this, "Clicked: " + areas.get(3).getxmin() + ", " + areas.get(3).getxmax() , Toast.LENGTH_SHORT).show();
                // Toast.makeText(ImageTestActivity.this, "Clicked: " + xmax + ", " + ymax , Toast.LENGTH_SHORT).show();
                break;

            case MotionEvent.ACTION_MOVE:


                float currentDistanceX = (event.getX() - xstart);
                float currentDistanceY = (event.getY() - ystart);

                totalScrolledX += currentDistanceX;
                totalScrolledY += currentDistanceY;
                //toast("Total: " + totalScrolledX);

                diffx = xstart - iv.getX();
                diffy = ystart - iv.getY();

                // Check if in range in x and y axis
                if(totalScrolledX < MAXVALUE_DRAG && totalScrolledX > -MAXVALUE_DRAG && totalScrolledY < MAXVALUE_DRAG && totalScrolledY > -MAXVALUE_DRAG){
                    iv.setX(event.getX() - diffx);
                    iv.setY(event.getY() - diffy);
                    previousX = event.getX() - diffx;
                    previousY = event.getY() - diffy;

                    // Check if in range of x axis but not y
                } else if(totalScrolledX < MAXVALUE_DRAG && totalScrolledX > -MAXVALUE_DRAG){
                    iv.setX(event.getX() - diffx);
                    iv.setY(previousY);
                    previousX = event.getX() - diffx;
                    totalScrolledY -= currentDistanceY;

                    // Check if in range of y axis but not x
                } else if(totalScrolledY < MAXVALUE_DRAG && totalScrolledY > -MAXVALUE_DRAG){
                    iv.setX(previousX);
                    iv.setY(event.getY() - diffy);
                    previousY = event.getY() - diffy;
                    totalScrolledX -= currentDistanceX;

                } else{
                    totalScrolledX -= currentDistanceX;
                    totalScrolledY -= currentDistanceY;
                    iv.setX(previousX);
                    iv.setY(previousY);

                }

                break;

            case MotionEvent.ACTION_UP:
               // Toast.makeText(context, "Total dragged: " + totalScrolledY, Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }



    public void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    } // end toast


    @Override
    public void onProgressChanged(SeekBar sbDemo, int progress, boolean fromUser) {
        double scalefactor = 0;
        if (progress >= 1 && progress < 15) {
            scalefactor = 1;
            //toast("" + progress);
            iv.setScaleX((float) scalefactor);
            iv.setScaleY((float) scalefactor);
            scaleFactorType = ScaleFactorType.ZOOM0;
            MAXVALUE_DRAG = 40;


        } else if (progress >= 15 && progress < 30) {
            scalefactor = 1.25;
            //toast("" + progress);
            iv.setScaleX((float) scalefactor);
            iv.setScaleY((float) scalefactor);
            scaleFactorType = ScaleFactorType.ZOOM1;
            MAXVALUE_DRAG = 180;

        } else if (progress >= 30 && progress < 50) {
            scalefactor = 2;
            //toast("" + progress);
            iv.setScaleX((float) scalefactor);
            iv.setScaleY((float) scalefactor);
            scaleFactorType = ScaleFactorType.ZOOM2;
            MAXVALUE_DRAG = 640;

        } else if (progress >= 50 && progress < 100) {
            scalefactor = 3;
            //toast("" + progress);
            iv.setScaleX((float) scalefactor);
            iv.setScaleY((float) scalefactor);
            scaleFactorType = ScaleFactorType.ZOOM3;
            MAXVALUE_DRAG = 1300;

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();


    }
}
