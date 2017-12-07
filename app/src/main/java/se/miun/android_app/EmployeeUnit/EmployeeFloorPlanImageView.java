package se.miun.android_app.EmployeeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

import se.miun.android_app.Model.FactoryObject;
import se.miun.android_app.testing.Area;


@SuppressLint({"ViewConstructor", "AppCompatCustomView"})
public class EmployeeFloorPlanImageView extends ImageView implements View.OnTouchListener {
    private String filePath;
    private String IP_ADDRESS = "http://193.10.119.34:8080";
    private Context context;
    private Bitmap bmp;
    private Boolean clicked = false;
    private Area clickedArea;
    private float startx, starty, endx, endy;
    private ArrayList<Area> objectAreas = new ArrayList<>();
    private int[][] myLocation;
    private int collumnsize, rowsize;
    private ArrayList<FactoryObject> objects;
    private FactoryObject clickedObject;
    private boolean blinking;
    private float drawXstart, drawYstart, drawXend, drawYend;
    private ArrayList<Area> currentLocationAreas;


    public void test() {
        Log.d("MYAPP", "IN IMAGEVIEW");
        this.drawXstart = 100;
        this.drawYstart = 100;
        this.drawXend = 500;
        this.drawYend = 500;
        invalidate();
    }

    public void drawNewLocation(ArrayList<Area> currentLocationAreas) {
        this.currentLocationAreas = currentLocationAreas;

        invalidate();
    }


    private enum MessageType {
        WARNING, REGULAR
    }


    // Imageview with no item selected
    public EmployeeFloorPlanImageView(final Context context, String filePath, ArrayList<FactoryObject> objects, int[][] myLocation) {
        super(context);
        this.context = context;
        this.filePath = filePath;
        this.objects = objects;
        this.myLocation = myLocation;
        getImage();
        setImageAreas();
    }


    @SuppressLint("StaticFieldLeak")
    private void getImage() {
        new AsyncTask<Void, Void, Void>() {


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
                if (bmp != null) {

                    setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.9f));
                    setScaleType(ScaleType.FIT_XY);
                    setImageBitmap(bmp);
                    setOnTouchListener(EmployeeFloorPlanImageView.this);
                    invalidate();
                }

            }

        }.execute();


    }

    private void setImageAreas() {
        collumnsize = 8;
        rowsize = 10;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        float height = display.getHeight();
        float width = display.getWidth();

        //46 is imagebutton heigth
        float sizeY = height / rowsize;
        float sizeX = width / rowsize;

        //get xmax and ymax for the first area
        float xmax = 25 / collumnsize;
        float ymax = 15 / rowsize;


        //add areas according to row and collumn sizes
        for (int r = 0; r < rowsize; r++) {
            for (int c = 0; c < collumnsize; c++) {
                Area area = new Area(xmax * (c), xmax * (c + 1), ymax * (r), ymax * (r + 1), c + 1, r + 1);
                area.setRealLimits(c * sizeX, c * sizeX + sizeX, r * sizeY, r * sizeY + sizeY);
                objectAreas.add(area);
                //Toast.makeText(context, "Area: " + xmax *(r) + ", " + xmax * (r+1), Toast.LENGTH_SHORT).show();
            }
        }


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint red = new Paint();
        red.setColor(Color.red(Color.RED));
        Paint transparent = new Paint();
        red.setColor(Color.TRANSPARENT);
        if(currentLocationAreas != null) {
            for (Area area : currentLocationAreas) {
                canvas.drawRect(area.getXstart(), area.getYstart(), area.getXend(), area.getYend(), transparent);
                Log.d("DRAWNEW", "x, y: " + area.getXstart() + ", " + area.getXend() + ", " + area.getYstart() + ", " + area.getYend());
            }
        }

        /*

        int locationmax = 0;
        for (int x = 0; x < collumnsize; x++) {
            for (int y = 0; y < rowsize; y++) {
                if (myLocation[y][x] > locationmax) {
                    locationmax = myLocation[y][x];
                }
            }
        }

        //draws rectangle on locations
        int currentarea  = 0;
        if (locationmax > 0) {
            for (int x = 0; x < collumnsize; x++) {
                for (int y = 0; y < rowsize; y++) {
                    if (myLocation[y][x] == locationmax) {
                        //draws rect based on limits of the current area in loop, all areas 0-79. 10*8
                       // canvas.drawRect(objectAreas.get(currentarea).getXstart(), objectAreas.get(currentarea).getYstart(), objectAreas.get(currentarea).getXend(), objectAreas.get(currentarea).getYend(), transparent);
                    }
                    currentarea++;
                }
            }
        }

        //canvas.drawRect((objectAreas.get(((5 + 0) * (5 + 0)) ).getxmin()*collumnsize), (objectAreas.get(((5 + 0) * (5 + 0)) ).getymin()*rowsize), (objectAreas.get(((5 + 0) * (5 + 0)) ).getxmax()*collumnsize), (objectAreas.get(((5 + 0) * (5 + 0)) ).getymax()*rowsize), transparent);
         canvas.drawRect(drawXstart, objectAreas.get(1).getYstart(), objectAreas.get(1).getXend(), (objectAreas.get(1).getYend()-(objectAreas.get(1).getYend()- objectAreas.get(1).getYstart())/2), transparent);
*/
        /*if (clickedObject != null) {

            // Just to show the area
            if(blinking) {
                //canvas.drawRect(820.8f, 710.4f, 907.2f, 852.48f, red);
                //canvas.drawRect(startx, starty, endx, endy, red);
            }
            else {
                canvas.drawRect(820.8f, 710.4f, 907.2f, 852.48f, transparent);
                //canvas.drawRect(startx, starty, endx, endy, transparent);
            }

        }*/
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        super.onTouchEvent(event);
        final int eventAction = event.getAction();
        switch (eventAction) {

            case MotionEvent.ACTION_DOWN:
                clicked = true;

                // Get the coordinates of the point of touch
                float x = event.getX();
                float y = event.getY();

                //creates an object dm that contain information about devicemetrics
                DisplayMetrics dm = getResources().getDisplayMetrics();
                //gets maximum width and height of device in terms of pixels
                float w = dm.widthPixels;
                //50 is seekbar height
                float h = dm.heightPixels;


                //px is the x coordinate varying from 0-100 on screen touch, py is the same
                float px = (x / w) * 25;
                float py = (y / h) * 15;

                //depending on where the screen is touched
                for (int i = 0; i < objectAreas.size(); i++) {
                    if (px > objectAreas.get(i).getxmin() && px < objectAreas.get(i).getxmax() && objectAreas.get(i).getymin() < py && objectAreas.get(i).getymax() > py) {
                        clickedArea = objectAreas.get(i);
                        // Check if there is any object in the area clicked
                        checkObjectForArea();
                        //Toast.makeText(context, "Clicked Area: " + objectAreas.get(i).getrow() + ", " + objectAreas.get(i).getcollumn() + ", Coordinates: " + px + ", " + py, Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "sx: " + startx + "sy: " + starty + "ex: " + endx + "ey: " + endy, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context, "Clicked Area: " + areas.get(i).getxmin() + "-" + areas.get(i).getxmax() + ", " + areas.get(i).getymin() + "-" + areas.get(i).getymax(), Toast.LENGTH_SHORT).show();
                    }
                }

                invalidate();
                break;
        }
        return true;
    }

    // Check if there is any object in the area that was clicked
    void checkObjectForArea() {

        // Check if there is any object in the given area
        for (FactoryObject object : objects) {
    /*
            if(object.getObjectId() == 1){
                Toast.makeText(context, clickedArea.getrow() + " >= " + object.getAreaXStart(), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, clickedArea.getrow() + " <= " + object.getAreaXEnd(), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, clickedArea.getcollumn() + " >= " + object.getAreaYStart(), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, clickedArea.getcollumn() + " <= " + object.getAreaYEnd(), Toast.LENGTH_SHORT).show();

            }
*/

            if (clickedArea.getrow() >= object.getAreaXStart() && clickedArea.getrow() <= object.getAreaXEnd() && clickedArea.getcollumn() >= object.getAreaYStart() && clickedArea.getcollumn() <= object.getAreaYEnd()) {
                //Toast.makeText(context, "Its object " + object.getObjectName(), Toast.LENGTH_SHORT).show();
                clickedObject = object;
                setCoordinatesForObject(clickedObject);


            }
        }
    }

    // Sets limits to be able to draw object
    void setCoordinatesForObject(FactoryObject factoryObject) {
        for (Area a : objectAreas) {
            if (a.getrow() == factoryObject.getAreaXStart()) {
                startx = a.getXstart();
            } else if (a.getrow() == factoryObject.getAreaXEnd()) {
                endx = a.getXend();
            } else if (a.getcollumn() == factoryObject.getAreaYStart()) {
                starty = a.getYstart();
            } else if (a.getcollumn() == factoryObject.getAreaYEnd()) {
                endy = a.getYend();
            }
        }

        //blink();
    }

/*

    private void blink(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 2000;
                try{Thread.sleep(timeToBlink);}catch (Exception e) {}
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(blinking){
                            blinking = false;
                        }else{
                            blinking = true;
                        }
                        invalidate();
                        blink();
                    }
                });
            }
        }).start();
    }

    */
}

