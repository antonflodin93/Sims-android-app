package se.miun.android_app.testing;


import android.os.Bundle;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import se.miun.android_app.R;
import se.miun.android_app.Service.LocalWordService;

public class ServiceTestActivity extends AppCompatActivity implements ServiceConnection {
    private LocalWordService s;
    private ListView displayDataListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_test);

        displayDataListView = (ListView) findViewById(R.id.displayDataListView);

        wordList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, wordList);

        displayDataListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent= new Intent(this, LocalWordService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    private ArrayAdapter<String> adapter;
    private List<String> wordList;

    public void onClick(View view) {
        Log.i("myapp", "in click");
        switch (view.getId()) {
            case R.id.updateList:
                if (s != null) {
                    wordList.clear();
                    wordList.addAll(s.getWordList());
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.triggerServiceUpdate:
                //Intent service = new Intent(getApplicationContext(), LocalWordService.class);
                //getApplicationContext().startService(service);
                break;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        LocalWordService.MyBinder b = (LocalWordService.MyBinder) binder;
        s = b.getService();
        Log.i("myapp", "server connected");
        if (s != null) {
            Toast.makeText(this, "Number of elements" + s.getWordListSize(),
                    Toast.LENGTH_SHORT).show();
            wordList.clear();
            wordList.addAll(s.getWordList());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        s = null;
    }
}