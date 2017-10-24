package se.miun.android_app.EmployeeUnit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

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

    private ArrayList<Message> regularMessages;
    private ArrayList<Message> warningMessages;
    private RecyclerView messageRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RegularMessageAdapter regularAdapter;
    private WarningMessageAdapter warningAdapter;
    private MessageType messageType;
    private Context context;
    private Intent regularMessageIntent, warningMessageIntent;
    private int numOfReceievedRegularMessages, numOfReceievedWarningMessages;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            // When receiving regular messages
            if (intent.getAction().equals("RegularMessageService")) {
                regularMessages.clear();
                regularMessages = (ArrayList<Message>) intent.getSerializableExtra("messages");
                if (regularMessages.size() > numOfReceievedRegularMessages) {
                    Toast.makeText(context, "Regular receivied " + regularMessages.size(), Toast.LENGTH_SHORT).show();
                    numOfReceievedRegularMessages = regularMessages.size();
                    updateList();
                }
                //ShowMessagesActivity.this.updateUIRegularMessages(intent);

                // When receiving warning messages
            } else if (intent.getAction().equals("WarningMessageService")) {
                warningMessages = (ArrayList<Message>) intent.getSerializableExtra("messages");
                if (warningMessages.size() > numOfReceievedWarningMessages) {
                    Toast.makeText(context, "Warning receivied " + warningMessages.size(), Toast.LENGTH_SHORT).show();
                    numOfReceievedWarningMessages = warningMessages.size();
                    updateList();
                }
                //ShowMessagesActivity.this.updateUIWarningMessages(intent);
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

        messageType = (MessageType) getIntent().getSerializableExtra("MESSAGETYPE");
        if(messageType == MessageType.REGULAR){
            regularMessages = (ArrayList<Message>) getIntent().getSerializableExtra("MESSAGES");
            regularAdapter = new RegularMessageAdapter(regularMessages);
            messageRecyclerView.setAdapter(regularAdapter);
        } else if(messageType == MessageType.WARNING){
            warningMessages = (ArrayList<Message>) getIntent().getSerializableExtra("MESSAGES");
            warningAdapter = new WarningMessageAdapter(warningMessages);
            messageRecyclerView.setAdapter(warningAdapter);
        }


    }


    private void updateList() {



    }


}
