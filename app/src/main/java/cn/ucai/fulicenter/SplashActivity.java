package cn.ucai.fulicenter;

import android.content.Intent;
import android.support.v4.media.RatingCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {
    private long sleepTime=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new Runnable() {
                @Override
                public void run() {
                    long start=System.currentTimeMillis();
                    //creat db
                    long costTime=System.currentTimeMillis()-start;
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime-costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                }

            }).start();

        }
    }

