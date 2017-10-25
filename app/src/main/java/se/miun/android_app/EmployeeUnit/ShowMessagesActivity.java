package se.miun.android_app.EmployeeUnit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import se.miun.android_app.Adapter.RegularMessageAdapter;
import se.miun.android_app.Adapter.WarningMessageAdapter;
import se.miun.android_app.EmployeeUnit.EmployeeUnitActivity.MessageType;
import se.miun.android_app.R;
import se.miun.android_app.Service.RegularMessageService;
import se.miun.android_app.Service.WarningMessageService;
import se.miun.android_app.model.Message;

// Show messages, either warning or regular
public class ShowMessagesActivity extends AppCompatActivity {

    private ArrayList<Message> regularMessages = new ArrayList<>();
    private ArrayList<Message> warningMessages = new ArrayList<>();
    private RecyclerView messageRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RegularMessageAdapter regularAdapter;
    private WarningMessageAdapter warningAdapter;
    private MessageType messageType;
    private Context context;
    private int numOfReceievedRegularMessages, numOfReceievedWarningMessages;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            // When receiving regular messages
            if (intent.getAction().equals("RegularMessageService")) {
                regularMessages.clear();
                regularMessages = (ArrayList<Message>) intent.getSerializableExtra("messages");

                if (regularMessages.size() > numOfReceievedRegularMessages) {
                    numOfReceievedRegularMessages = regularMessages.size();
                    regularAdapter = new RegularMessageAdapter(regularMessages);
                    messageRecyclerView.setAdapter(regularAdapter);
                }


                // When receiving warning messages
            } else if (intent.getAction().equals("WarningMessageService")) {
                warningMessages.clear();
                warningMessages = (ArrayList<Message>) intent.getSerializableExtra("messages");
                if (warningMessages.size() > numOfReceievedWarningMessages) {
                    numOfReceievedWarningMessages = warningMessages.size();
                    warningAdapter = new WarningMessageAdapter(warningMessages);
                    messageRecyclerView.setAdapter(warningAdapter);
                }

            }
        }
    };


    public ShowMessagesActivity() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_show_messages);
        context = this;

        // Init variables
        messageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        messageRecyclerView.setLayoutManager(layoutManager);
        messageRecyclerView.setHasFixedSize(true);

        // Check if the user wants to display warning or regular messages
        messageType = (MessageType) getIntent().getSerializableExtra("MESSAGETYPE");
        if (messageType == MessageType.REGULAR) {
            // Define the adapter and set adapter
            regularAdapter = new RegularMessageAdapter(regularMessages);
            layoutManager = new LinearLayoutManager(getApplicationContext());
            messageRecyclerView.setLayoutManager(layoutManager);
            messageRecyclerView.setItemAnimator(new DefaultItemAnimator());
            messageRecyclerView.setAdapter(regularAdapter);

        } else if (messageType == MessageType.WARNING) {
            // Define the adapter and set adapter
            warningAdapter = new WarningMessageAdapter(warningMessages);
            layoutManager = new LinearLayoutManager(getApplicationContext());
            messageRecyclerView.setLayoutManager(layoutManager);
            messageRecyclerView.setItemAnimator(new DefaultItemAnimator());
            messageRecyclerView.setAdapter(warningAdapter);
        }
    }

    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        // Check which kind of messages that user wants to list
        // Add filter for those
        if (messageType == MessageType.REGULAR) {
            intentFilter.addAction("RegularMessageService");

        } else if (messageType == MessageType.WARNING) {
            intentFilter.addAction("WarningMessageService");
        }

        this.registerReceiver(this.broadcastReceiver, intentFilter);
    }

    public void onPause() {
        super.onPause();
        this.unregisterReceiver(this.broadcastReceiver);
    }


}
