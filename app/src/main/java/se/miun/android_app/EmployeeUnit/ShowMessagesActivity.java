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
import se.miun.android_app.model.Message;

// Show messages, either warning or regular
public class ShowMessagesActivity extends AppCompatActivity {

    private ArrayList<Message> messages;
    private RecyclerView messageRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RegularMessageAdapter regularAdapter;
    private WarningMessageAdapter warningAdapter;
    private MessageType messageType;
    private Context context;


    public ShowMessagesActivity() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_show_messages);
        context = this;

        messageType = (MessageType) getIntent().getSerializableExtra("MESSAGETYPE");
        messages = (ArrayList<Message>) getIntent().getSerializableExtra("MESSAGES");

        // Init variables
        messageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        messageRecyclerView.setLayoutManager(layoutManager);
        messageRecyclerView.setHasFixedSize(true);

        updateList();
    }

    private void updateList() {
        if(messageType == MessageType.WARNING){
            warningAdapter = new WarningMessageAdapter(messages);
            messageRecyclerView.setAdapter(warningAdapter);
        } else if(messageType == MessageType.REGULAR){
            Toast.makeText(context, "Size " + messages.size(), Toast.LENGTH_SHORT).show();
            regularAdapter = new RegularMessageAdapter(messages);
            messageRecyclerView.setAdapter(regularAdapter);
        }

    }


}
