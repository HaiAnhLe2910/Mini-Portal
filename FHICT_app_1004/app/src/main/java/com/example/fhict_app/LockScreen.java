package com.example.fhict_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class LockScreen extends AppCompatActivity {

    private ImageView logo;
    private TextView welcomeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        logo=(ImageView)findViewById(R.id.imageFontys);
        logo.setImageResource(R.drawable.fontys);

        welcomeText=(TextView)findViewById(R.id.welcomeText);

        Animation animation= AnimationUtils.loadAnimation(this,R.anim.mytransition);
        welcomeText.startAnimation(animation);
        logo.startAnimation(animation);


        final Intent i=new Intent(this, MainActivity.class);
        Thread timer=new Thread(){
            public void run(){
                try {
                    sleep(5000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }




}
