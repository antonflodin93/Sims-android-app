package se.miun.android_app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class WelcomeActivity extends Activity {
    private static int SPLASH_TIME = 3000;
    private ImageView welcomeImageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeImageview = (ImageView) findViewById(R.id.welcomeImageview);



        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            welcomeImageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else{
            welcomeImageview.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent startIntent = new Intent(WelcomeActivity.this, StartActivity.class);
                startActivity(startIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }, SPLASH_TIME);
    }
}
