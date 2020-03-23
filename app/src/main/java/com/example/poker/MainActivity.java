package com.example.poker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Cards card = new Cards(0,1);

        final ImageView cardView = findViewById(R.id.card);

        int id = getResources().getIdentifier(Cards.getCardDrawableName(card), "drawable", getPackageName());


        cardView.setImageResource(id);


    }
}
