package se.miun.android_app.MasterUnit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import se.miun.android_app.Adapter.SentWarningMessageAdapter;
import se.miun.android_app.Api.ApiClient;
import se.miun.android_app.Api.ApiInterface;
import se.miun.android_app.EmployeeUnit.EmployeeUnitActivity;
import se.miun.android_app.Model.Message;
import se.miun.android_app.R;

public class BuildingFloorMessagesList extends AppCompatActivity {
    private int HTTP_RESPONSE_OK = 200;
    private SentWarningMessageAdapter warningAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Message> buildingMessages;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_floor_messages_list);

        // Init variables
        recyclerView = (RecyclerView) findViewById(R.id.sentWarningMessageRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        context = this;

        getBuildingMessages();
    }



    public void getBuildingMessages() {
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ArrayList<Message>> call;
        call = apiInterface.getAllBuildingWarningMessages();
        call.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                if (response.code() == HTTP_RESPONSE_OK) {
                    buildingMessages = response.body();
                    // Set adapter
                    warningAdapter = new SentWarningMessageAdapter(buildingMessages, context);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(warningAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setHasFixedSize(true);
                    setupSwipeFeatures();
                } else {
                    try {
                        Toast.makeText(context, "Error: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSwipeFeatures() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(context);

                builder.setTitle("Remove message")
                        .setMessage("Do you want to remove the message?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int position = viewHolder.getAdapterPosition();
                                String messageId = warningAdapter.getMessageId(position);
                                removeMessageFromDB(messageId);
                                warningAdapter.remove(position);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                recyclerView.setAdapter(warningAdapter);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();


            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    // When a message is clicked in adapter
    public void messageClicked(Message message){
        Intent myIntent = new Intent(getApplicationContext(), ListEmployeeInfo.class);
        myIntent.putExtra("message", message);
        this.startActivity(myIntent);
    }

    // When message is swiped
    private void removeMessageFromDB(String messageId) {
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call;
        call = apiInterface.deleteBuildingMessage(messageId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == HTTP_RESPONSE_OK) {
                    Toast.makeText(BuildingFloorMessagesList.this, "Message removed", Toast.LENGTH_SHORT).show();
                } else{
                    try {
                        Toast.makeText(context, "Error: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
