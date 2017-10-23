package se.miun.android_app.EmployeeUnit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import se.miun.android_app.R;
import se.miun.android_app.EmployeeUnit.EmployeeUnitActivity.MessageType;

public class ShowMessagesActivity extends AppCompatActivity {

    private MessageType messagetype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_messages);
        messagetype = (MessageType) getIntent().getSerializableExtra("MESSAGETYPE");

        if(messagetype == MessageType.REGULAR){
            Toast.makeText(this, "Regular", Toast.LENGTH_SHORT).show();
        } else if(messagetype == MessageType.WARNING){
            Toast.makeText(this, "Warning", Toast.LENGTH_SHORT).show();
        }
    }
}
