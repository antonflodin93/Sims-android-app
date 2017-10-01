package se.miun.android_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DatabaseTestActivity extends AppCompatActivity {
    private TextView databaseTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_databasetest);

        databaseTextView = (TextView) findViewById(R.id.databaseTextView);
    }
}
