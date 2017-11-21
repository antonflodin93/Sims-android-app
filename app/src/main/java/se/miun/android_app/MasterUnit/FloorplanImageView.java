package se.miun.android_app.MasterUnit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import se.miun.android_app.Adapter.ObjectAdapter;
import se.miun.android_app.Model.FactoryObject;
import se.miun.android_app.testing.Area;


public class FloorplanImageView extends ImageView implements View.OnTouchListener {
    private String filePath;
    private String IP_ADDRESS = "http://193.10.119.34:8080";
    private Context context;
    private Bitmap bmp;
    private Boolean clicked = false;
    private Area clickedArea;
    private Boolean drawing = false;
    private float clickedObjectxstart, clickedObjectystart, clickedObjectxend, clickedObjectyend;
    private ArrayList<Area> areas = new ArrayList<>();
    private ArrayList<FactoryObject> objects;
    private FactoryObject clickedObject;

    private enum MessageType {
        WARNING, REGULAR
    }


    // Imageview with no item selected
    public FloorplanImageView(final Context context, final String filePath, ArrayList<FactoryObject> objects) {
        super(context);
        this.filePath = filePath;
        this.context = context;
        this.objects = objects;
        getImage();
        setImageAreas();
    }

    // Imageview with item selected
    public FloorplanImageView(final Context context, final String filePath, ArrayList<FactoryObject> objects, FactoryObject clickedObject) {
        super(context);
        this.filePath = filePath;
        this.context = context;
        this.objects = objects;
        this.clickedObject = clickedObject;
        getImage();
        setImageAreas();
        openDialogForItem();
    }


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
                    setOnTouchListener(FloorplanImageView.this);
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

        if (clicked) {
            canvas.drawRect(clickedArea.getXstart(), clickedArea.getYstart(), clickedArea.getXend(), clickedArea.getYend(), p);
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

//18 20 24 26
                //depending on where the screen is touched, write which area that was touched
                for (int i = 0; i < areas.size(); i++) {
                    if (px > areas.get(i).getxmin() && px < areas.get(i).getxmax() && areas.get(i).getymin() < py && areas.get(i).getymax() > py) {
                        clickedArea = areas.get(i);

                        checkObjectForArea();
                        // Toast.makeText(context, "Clicked Area: " + areas.get(i).getrow() + ", " + areas.get(i).getcollumn() + ", Coordinates: " + px + ", " + py, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context, "Clicked Area: " + areas.get(i).getxmin() + "-" + areas.get(i).getxmax() + ", " + areas.get(i).getymin() + "-" + areas.get(i).getymax(), Toast.LENGTH_SHORT).show();
                    }
                }


                invalidate();

                break;

            case MotionEvent.ACTION_MOVE:


                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }


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
                openDialogForItem();

            }
        }
    }

    void openDialogForItem() {
        // First, select what kind of message
        final CharSequence[] messageTypes = {"Warning message", "Regular Message"};
        // Display dialog
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setIcon(android.R.drawable.ic_dialog_alert);
        builderSingle.setTitle("Select type of message for " + clickedObject.getObjectName());

        builderSingle.setSingleChoiceItems(messageTypes, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int messageType) {
                        Toast.makeText(context, messageTypes[messageType],
                                Toast.LENGTH_SHORT).show();

                        // 0 is warning and 1 is regular
                        if (messageType == MessageType.WARNING.ordinal()) {
                            Toast.makeText(context, "warning", Toast.LENGTH_SHORT).show();
                            createMessage(messageType);
                            dialog.dismiss();


                        } else {
                            createMessage(messageType);
                            dialog.dismiss();
                        }
                    }
                });

        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.show();
    }

    void createMessage(int messageType) {
        if (messageType == MessageType.WARNING.ordinal()) {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

            builderSingle.setIcon(android.R.drawable.ic_dialog_alert);
            builderSingle.setTitle("Select warning message for " + clickedObject.getObjectName());

            final CharSequence[] messageChoices = {"Default message: Exit the building", "Default message: Avoid this area", "Write own message"};
            builderSingle.setSingleChoiceItems(messageChoices, -1,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, final int selectedIndex) {
                            if (selectedIndex == 0 || selectedIndex == 1) {
                                dialog.dismiss();
                                AlertDialog.Builder dialogselection = new AlertDialog.Builder(context);

                                dialogselection.setIcon(android.R.drawable.ic_dialog_alert);
                                dialogselection.setTitle("Are you sure you want to send the message?");
                                dialogselection.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Send default message
                                        sendMessage((String) messageChoices[selectedIndex]);
                                    }
                                });

                                dialogselection.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        createMessage(MessageType.WARNING.ordinal());
                                    }
                                });

                                dialogselection.show();

                            } else {
                                // Write own message
                                dialog.dismiss();
                                AlertDialog.Builder dialogselection = new AlertDialog.Builder(context);


                                final EditText messageEditText = new EditText(context);
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT);
                                messageEditText.setLayoutParams(lp);
                                dialogselection.setView(messageEditText);


                                dialogselection.setIcon(android.R.drawable.ic_dialog_alert);
                                dialogselection.setTitle("Enter a warning message");


                                dialogselection.setPositiveButton("Send",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (!messageEditText.getText().toString().trim().equals("")) {
                                                    AlertDialog.Builder dialogselection = new AlertDialog.Builder(context);

                                                    dialogselection.setIcon(android.R.drawable.ic_dialog_alert);
                                                    dialogselection.setTitle("Are you sure you want to send the message?");
                                                    dialogselection.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            // Send default message
                                                            sendMessage(messageEditText.getText().toString());
                                                        }
                                                    });

                                                    dialogselection.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            createMessage(MessageType.WARNING.ordinal());
                                                        }
                                                    });

                                                    dialogselection.show();
                                                } else {
                                                    createMessage(MessageType.WARNING.ordinal());
                                                }


                                            }
                                        });

                                dialogselection.show();

                            }


                        }
                    });



/*

            final EditText messageEditText = new EditText(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            messageEditText.setLayoutParams(lp);
            builderSingle.setView(messageEditText);


            builderSingle.setIcon(android.R.drawable.ic_dialog_alert);
            builderSingle.setTitle("Enter a warning message");


            builderSingle.setPositiveButton("Send",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(!messageEditText.getText().toString().trim().equals("")){
                                Toast.makeText(context, "EMPTY", Toast.LENGTH_SHORT).show();
                            } else{
                                createMessage(MessageType.WARNING.ordinal());
                            }


                        }
                    });

*/
            builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builderSingle.show();
        }


    }

    void sendMessage(String messageChoice) {
        Toast.makeText(context, "Sent " + messageChoice, Toast.LENGTH_SHORT).show();
    }

}



/*




 */