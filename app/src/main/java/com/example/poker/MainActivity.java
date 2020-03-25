package com.example.poker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Deck deck = new Deck();
        Dealer dealer = new Dealer();
        HandEvaluator handEvaluator = new HandEvaluator();

        ArrayList<Card> table = new ArrayList<>();
        ArrayList<Card> player1 = new ArrayList<>();
        ArrayList<Card> player2 = new ArrayList<>();

        ArrayList<ImageView> tableImg = new ArrayList<>();
        ArrayList<ImageView> player1Img = new ArrayList<>();
        ArrayList<ImageView> player2Img = new ArrayList<>();

        // set player cards
        dealer.setPlayersCards(deck, player1, player2);

        // set flop
        dealer.setFlop(deck, table);

        // set turn
        dealer.setOneCard(deck, table);

        // set river
        dealer.setOneCard(deck, table);

        //----- player1
        int player1Card1Id = getResources().getIdentifier(player1.get(0).getCardDrawableName(), "drawable", getPackageName());
        int player1Card2Id = getResources().getIdentifier(player1.get(1).getCardDrawableName(), "drawable", getPackageName());

        player1Img.add((ImageView) findViewById(R.id.player1_1));
        player1Img.add((ImageView) findViewById(R.id.player1_2));

        player1Img.get(0).setImageResource(player1Card1Id);
        player1Img.get(1).setImageResource(player1Card2Id);

        //----- player2
        int player2Card1Id = getResources().getIdentifier(player2.get(0).getCardDrawableName(), "drawable", getPackageName());
        int player2Card2Id = getResources().getIdentifier(player2.get(1).getCardDrawableName(), "drawable", getPackageName());

        player2Img.add((ImageView) findViewById(R.id.player2_1));
        player2Img.add((ImageView) findViewById(R.id.player2_2));

        player2Img.get(0).setImageResource(player2Card1Id);
        player2Img.get(1).setImageResource(player2Card2Id);

        //----- table
        int flop1 = getResources().getIdentifier(table.get(0).getCardDrawableName(), "drawable", getPackageName());
        int flop2 = getResources().getIdentifier(table.get(1).getCardDrawableName(), "drawable", getPackageName());
        int flop3 = getResources().getIdentifier(table.get(2).getCardDrawableName(), "drawable", getPackageName());
        int turn = getResources().getIdentifier(table.get(3).getCardDrawableName(), "drawable", getPackageName());
        int river = getResources().getIdentifier(table.get(4).getCardDrawableName(), "drawable", getPackageName());

        tableImg.add((ImageView) findViewById(R.id.flop_1));
        tableImg.add((ImageView) findViewById(R.id.flop_2));
        tableImg.add((ImageView) findViewById(R.id.flop_3));
        tableImg.add((ImageView) findViewById(R.id.turn));
        tableImg.add((ImageView) findViewById(R.id.river));

        tableImg.get(0).setImageResource(flop1);
        tableImg.get(1).setImageResource(flop2);
        tableImg.get(2).setImageResource(flop3);
        tableImg.get(3).setImageResource(turn);
        tableImg.get(4).setImageResource(river);

        handEvaluator.evaluate(player1, table);

    }
}
