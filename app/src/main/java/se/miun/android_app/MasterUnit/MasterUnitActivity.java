package se.miun.android_app.MasterUnit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import se.miun.android_app.EmployeeUnit.EmployeeUnitActivity;
import se.miun.android_app.R;

public class MasterUnitActivity extends AppCompatActivity implements View.OnClickListener{
    private Button sendMessagesBtn;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_unit);
        context = this;

        // Init the components
        sendMessagesBtn = (Button) findViewById(R.id.sendMessagesBtn);
        sendMessagesBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.sendMessagesBtn){
            Intent myIntent = new Intent(getApplicationContext(), SendMessageActivity.class);
            context.startActivity(myIntent);
        }

    }
}
