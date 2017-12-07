package se.miun.android_app.MasterUnit;

import android.annotation.SuppressLint;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import se.miun.android_app.Adapter.ObjectAdapter;
import se.miun.android_app.Api.ApiClient;
import se.miun.android_app.Api.ApiInterface;
import se.miun.android_app.LoginActivity;
import se.miun.android_app.Model.Employee;
import se.miun.android_app.Model.FactoryObject;
import se.miun.android_app.Model.Message;
import se.miun.android_app.testing.Area;


@SuppressLint({"ViewConstructor", "AppCompatCustomView"})
public class FloorplanImageView extends ImageView implements View.OnTouchListener {
    private static final int RESPONSE_OK = 200;
    private String filePath;
    private String IP_ADDRESS = "http://193.10.119.34:8080";
    private Context context;
    private Bitmap bmp;
    private Boolean clicked = false;
    private Area clickedArea;
    private int buildingId;
    private Boolean drawing = false;
    private float clickedObjectxstart, clickedObjectystart, clickedObjectxend, clickedObjectyend;
    private ArrayList<Area> areas = new ArrayList<>();
    private ArrayList<FactoryObject> objects;
    private FactoryObject clickedObject;

    private enum MessageType {
        WARNING, REGULAR
    }


    // Imageview with no item selected
    public FloorplanImageView(final Context context, final String filePath, ArrayList<FactoryObject> objects, int buildingId) {
        super(context);
        this.filePath = filePath;
        this.context = context;
        this.objects = objects;
        this.buildingId = buildingId;
        getImage();
        setImageAreas();
    }

    // Imageview with item selected
    public FloorplanImageView(final Context context, final String filePath, ArrayList<FactoryObject> objects, int buildingId, FactoryObject clickedObject) {
        super(context);
        this.filePath = filePath;
        this.context = context;
        this.objects = objects;
        this.buildingId = buildingId;
        this.clickedObject = clickedObject;
        getImage();
        setImageAreas();
        dialogChooseMessageType();
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
                dialogChooseMessageType();

            }
        }
    }

    void dialogChooseMessageType() {
        // Different kind of message
        final CharSequence[] messageTypes = {"Warning message", "Regular Message"};

        // Display dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setTitle("Select type of message for " + clickedObject.getObjectName());

        dialog.setSingleChoiceItems(messageTypes, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int messageType) {
                        Toast.makeText(context, messageTypes[messageType],
                                Toast.LENGTH_SHORT).show();

                        // Check if the message should be warning or regular and call respective function
                        if (messageType == MessageType.WARNING.ordinal()) {
                            dialogSelectWarningMessage();
                            dialog.dismiss();


                        } else {
                            dialogCreateMessage("", MessageType.REGULAR);
                            dialog.dismiss();
                        }
                    }
                });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // Dialog to select warning message, create own or default messages
    void dialogSelectWarningMessage() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.setTitle("Select warning message for " + clickedObject.getObjectName());

        final CharSequence[] messageChoices = {"Exit the building! (Default message)", "Avoid this area! (Default message)", "Write own message"};
        dialog.setSingleChoiceItems(messageChoices, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, final int selectedIndex) {

                        // If default message 0 or 1 is choosen
                        if (selectedIndex == 0 || selectedIndex == 1) {

                            // Remove the (default message) part of string
                            String selectedMessage = (String) messageChoices[selectedIndex];
                            String toBeReplaced = selectedMessage.substring(selectedMessage.indexOf("("), selectedMessage.indexOf(")")+1);
                            selectedMessage = selectedMessage.replace(toBeReplaced, "");

                            // Create message
                            dialogCreateMessage(selectedMessage, MessageType.WARNING);
                            dialog.dismiss();

                        } else {
                            // Write own message
                            dialogCreateMessage("", MessageType.WARNING);
                            dialog.dismiss();
                        }


                    }
                });


        dialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialogChooseMessageType();

            }
        });

        dialog.show();
    }


    // Creates the message
    void dialogCreateMessage(final String message, final MessageType messageType) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);


        final EditText messageEditText = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        messageEditText.setLayoutParams(lp);
        dialog.setView(messageEditText);


        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        if(messageType == MessageType.REGULAR){
            dialog.setTitle("Enter a message");
        } else{
            dialog.setTitle("Enter a warning message");
        }


        messageEditText.setText(message);

        dialog.setPositiveButton("Send",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // check if the message is empty
                        if (!messageEditText.getText().toString().trim().equals("")) {
                            dialog.dismiss();
                            AlertDialog.Builder dialogCheck = new AlertDialog.Builder(context);

                            dialogCheck.setIcon(android.R.drawable.ic_dialog_alert);
                            dialogCheck.setTitle("Are you sure you want to send the message \"" + messageEditText.getText().toString() + "\"?");
                            dialogCheck.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Send default message
                                    sendMessage(messageEditText.getText().toString(), messageType);
                                }
                            });

                            dialogCheck.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogCreateMessage(message, messageType);
                                }
                            });

                            dialogCheck.show();
                        } else{
                            dialogCreateMessage(message, messageType);
                        }


                    }
                });

        dialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(messageType == MessageType.WARNING){
                    dialogSelectWarningMessage();
                } else{
                    dialogChooseMessageType();
                }

            }
        });

        dialog.show();




    }


    // Send the actual message
    void sendMessage(final String message, final MessageType messageType) {
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);



        Message m = new Message("(no subject)", message, messageType.name());
        Call<ResponseBody> call, floorMessageCall;
        if(messageType == MessageType.REGULAR) {
            call = apiInterface.insertRegularMessageFactoryObject(m, clickedObject.getObjectId());
        } else{
            call = apiInterface.addBuildingWarningMessage(buildingId, m);
        }




        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == RESPONSE_OK) {
                    Toast.makeText(context, "Sent " + message + ", " + messageType.name(), Toast.LENGTH_SHORT).show();

                } else {

                    try {
                        Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

}

