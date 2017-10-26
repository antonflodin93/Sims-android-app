package se.miun.android_app.EmployeeUnit;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

import se.miun.android_app.Adapter.RegularMessageAdapter;
import se.miun.android_app.R;
import se.miun.android_app.Service.RegularMessageService;
import se.miun.android_app.Service.WarningMessageService;
import se.miun.android_app.Model.Message;

public class EmployeeUnitActivity extends Activity implements View.OnClickListener {

    public enum MessageType {
        WARNING, REGULAR;
    }

    ;


    private ImageButton warningMessageBtn, regularMessageBtn;
    private Context context;
    private Intent regularMessageIntent, warningMessageIntent;
    private ArrayList<Message> regularMessages;
    private ArrayList<Message> warningMessages;
    private RecyclerView messageRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RegularMessageAdapter adapter;
    private int numOfReceievedRegularMessages, numOfReceievedWarningMessages;
    private boolean clickedButton = false;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            // When receiving regular messages
            if (intent.getAction().equals("RegularMessageService")) {
                regularMessages = (ArrayList<Message>) intent.getSerializableExtra("messages");
                if (regularMessages.size() > numOfReceievedRegularMessages) {
                    regularMessageBtn.setBackgroundColor(Color.RED);
                    numOfReceievedRegularMessages = regularMessages.size();
                }
                //ShowMessagesActivity.this.updateUIRegularMessages(intent);

                // When receiving warning messages
            } else if (intent.getAction().equals("WarningMessageService")) {
                warningMessages = (ArrayList<Message>) intent.getSerializableExtra("messages");
                if (warningMessages.size() > numOfReceievedWarningMessages) {
                    warningMessageBtn.setBackgroundColor(Color.RED);
                    numOfReceievedWarningMessages = warningMessages.size();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_unit);
        context = this;

        // Init components
        warningMessageBtn = (ImageButton) findViewById(R.id.warningMessageBtn);
        warningMessageBtn.setOnClickListener(this);
        regularMessageBtn = (ImageButton) findViewById(R.id.regularMessageBtn);
        regularMessageBtn.setOnClickListener(this);
        this.regularMessageIntent = new Intent(this, RegularMessageService.class);
        this.warningMessageIntent = new Intent(this, WarningMessageService.class);


    }


    public void onResume() {
        super.onResume();

        this.startService(this.warningMessageIntent);
        this.startService(this.regularMessageIntent);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("RegularMessageService");
        intentFilter.addAction("WarningMessageService");
        this.registerReceiver(this.broadcastReceiver, intentFilter);
    }

    public void onPause() {
        super.onPause();
        if(!clickedButton){
            this.unregisterReceiver(this.broadcastReceiver);
            this.stopService(this.regularMessageIntent);
            this.stopService(this.warningMessageIntent);
        }

        // Maybee needed
        //this.unregisterReceiver(this.broadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        clickedButton = true;
        if (v.getId() == R.id.warningMessageBtn) {
            Intent myIntent = new Intent(getApplicationContext(), ShowMessagesActivity.class);
            myIntent.putExtra("MESSAGETYPE", MessageType.WARNING);
           myIntent.putExtra("WARNINGMESSAGES", warningMessages);
            warningMessageBtn.setBackgroundColor(Color.BLACK);
            context.startActivity(myIntent);

        } else if (v.getId() == R.id.regularMessageBtn) {
            Intent myIntent = new Intent(getApplicationContext(), ShowMessagesActivity.class);
            myIntent.putExtra("MESSAGETYPE", MessageType.REGULAR);
            myIntent.putExtra("REGULARMESSAGES", regularMessages);
            regularMessageBtn.setBackgroundColor(Color.BLACK);
            context.startActivity(myIntent);
        }
    }
}
