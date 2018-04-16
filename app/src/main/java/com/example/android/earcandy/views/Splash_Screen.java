package com.example.android.earcandy.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.earcandy.R;

/**
 * Created by markmamdouh on 8/23/2017.
 */


public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        ImageView img = (ImageView)findViewById(R.id.splashscreen);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_2);
        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_2);
        if (img != null) {
            img.setAnimation(animation);
        }

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //When you want delay some time
                try {
                    Thread.sleep(2500);
                    //When animate finish, load main activity
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } catch (Exception e){

                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
