package com.filipebicho.poker;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

        Button startButton = findViewById(R.id.start_button);
        Animation startButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.start_button);

        startButton.setOnClickListener(v -> {
            startButton.startAnimation(startButtonAnimation);
            startActivity(new Intent(MainActivity.this, GameActivity.class));
        });

    }
}
