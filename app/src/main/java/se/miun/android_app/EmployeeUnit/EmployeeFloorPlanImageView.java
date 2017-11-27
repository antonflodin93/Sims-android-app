package se.miun.android_app.EmployeeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.InputStream;
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
    private ArrayList<Area> areas = new ArrayList<>();
    private ArrayList<FactoryObject> objects;
    private FactoryObject clickedObject;
    private enum MessageType {
        WARNING, REGULAR
    }


    // Imageview with no item selected
    public EmployeeFloorPlanImageView(final Context context, String filePath, ArrayList<FactoryObject> objects) {
        super(context);
        this.context = context;
        this.filePath = filePath;
        this.objects = objects;
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
        int collumnsize = 25;
        int rowsize = 25;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        float height = display.getHeight();
        float width = display.getWidth();

        float sizeY = height / rowsize;
        float sizeX = width / rowsize;

        //get xmax and ymax for the first area
        float xmax = 100 / collumnsize;
        float ymax = 100 / rowsize;


        //add areas according to row and collumn sizes
        for (int r = 0; r < rowsize; r++) {
            for (int c = 0; c < collumnsize; c++) {
                Area area = new Area(xmax * (c), xmax * (c + 1), ymax * (r), ymax * (r + 1), c + 1, r + 1);
                area.setRealLimits(c * sizeX, c * sizeX + sizeX, r * sizeY, r * sizeY + sizeY);
                areas.add(area);
                //Toast.makeText(context, "Area: " + xmax *(r) + ", " + xmax * (r+1), Toast.LENGTH_SHORT).show();
            }
        }


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();

        // Just to show the area
        if (clicked) {
            //canvas.drawRect(clickedArea.getXstart(), clickedArea.getYstart(), clickedArea.getXend(), clickedArea.getYend(), p);
            //Toast.makeText(context, clickedArea.getXstart() + " " + clickedArea.getYstart() + " " + clickedArea.getXend() + " " + clickedArea.getYend(), Toast.LENGTH_SHORT).show();

        }


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
                float px = (x / w) * 100;
                float py = (y / h) * 100;

                //depending on where the screen is touched
                for (int i = 0; i < areas.size(); i++) {
                    if (px > areas.get(i).getxmin() && px < areas.get(i).getxmax() && areas.get(i).getymin() < py && areas.get(i).getymax() > py) {
                        clickedArea = areas.get(i);
                        // Check if there is any object in the area clicked
                        checkObjectForArea();
                        // Toast.makeText(context, "Clicked Area: " + areas.get(i).getrow() + ", " + areas.get(i).getcollumn() + ", Coordinates: " + px + ", " + py, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, "Its object " + object.getObjectName(), Toast.LENGTH_SHORT).show();
                clickedObject = object;


            }
        }
    }
}

