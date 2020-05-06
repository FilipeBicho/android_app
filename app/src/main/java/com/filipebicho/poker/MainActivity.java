package com.filipebicho.poker;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;


/**
 * Main activity class
 */
public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = (Button) findViewById(R.id.start_button);
        Animation startButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.start_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startButton.startAnimation(startButtonAnimation);
            }
        });

    }
}
