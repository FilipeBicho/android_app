package com.example.poker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Deck deck = new Deck();

        for (int i = 0; i < Deck.DECK_SIZE; i++)
        {
            Card card = deck.getCard();

            int id = getResources().getIdentifier(card.getCardDrawableName(), "drawable", getPackageName());

            final ImageView cardView = findViewById(R.id.card);
            cardView.setImageResource(id);
        }





    }
}
