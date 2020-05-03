package com.filipebicho.poker;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Main activity class
 */
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
        ArrayList<Card> player1Hand;
        ArrayList<Card> player2Hand;

        OddsCalculator oddsCalculator;

        ArrayList<ImageView> tableImg = new ArrayList<>();
        ArrayList<ImageView> player1Img = new ArrayList<>();
        ArrayList<ImageView> player2Img = new ArrayList<>();

        final Button showTurnButton = findViewById(R.id.show_turn_button);
        final Button showRiverButton = findViewById(R.id.show_river_button);

        // Init cards combinations
        // needs to be initialized before the deck is changed
        CombinationsCalculator combinationsCalculator = new CombinationsCalculator(new ArrayList<>(deck.getDeck()), getApplicationContext());

        TextView player1HandEvaluationTextView = findViewById(R.id.player1_hand_evaluation);
        TextView player2HandEvaluationTextView = findViewById(R.id.player2_hand_evaluation);

        //--- set cards
        dealer.setPlayersCards(deck, player1, player2);
        oddsCalculator = new OddsCalculator(player1, null, combinationsCalculator);

        //----- player1

        player1Img.add((ImageView) findViewById(R.id.player1_1));
        player1Img.add((ImageView) findViewById(R.id.player1_2));

        player1Img.get(0).setImageResource(getResources().getIdentifier(player1.get(0).getCardDrawableName(), "drawable", getPackageName()));
        player1Img.get(1).setImageResource(getResources().getIdentifier(player1.get(1).getCardDrawableName(), "drawable", getPackageName()));

        //----- player2

        player2Img.add((ImageView) findViewById(R.id.player2_1));
        player2Img.add((ImageView) findViewById(R.id.player2_2));

        player2Img.get(0).setImageResource(getResources().getIdentifier(player2.get(0).getCardDrawableName(), "drawable", getPackageName()));
        player2Img.get(1).setImageResource(getResources().getIdentifier(player2.get(1).getCardDrawableName(), "drawable", getPackageName()));

        //--- Flop
        dealer.setFlop(deck, table);

        tableImg.add((ImageView) findViewById(R.id.flop_1));
        tableImg.add((ImageView) findViewById(R.id.flop_2));
        tableImg.add((ImageView) findViewById(R.id.flop_3));

        tableImg.get(0).setImageResource(getResources().getIdentifier(table.get(0).getCardDrawableName(), "drawable", getPackageName()));
        tableImg.get(1).setImageResource(getResources().getIdentifier(table.get(1).getCardDrawableName(), "drawable", getPackageName()));
        tableImg.get(2).setImageResource(getResources().getIdentifier(table.get(2).getCardDrawableName(), "drawable", getPackageName()));

        String player1EvaluationText = handEvaluator.getHandEvaluationTextByRanking(handEvaluator.evaluate(player1, table));
       // String player2EvaluationText = handEvaluator.getHandEvaluationTextByRanking(handEvaluator.evaluate(player2, table));

        ArrayList<String> flopOdds = oddsCalculator.flopOdds(table, 5000);

        player1HandEvaluationTextView.setText(player1EvaluationText + " - " + flopOdds.get(0));
       // player2HandEvaluationTextView.setText(player2EvaluationText + " - " + flopOdds.get(1));


        showTurnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // set turn
                dealer.setOneCard(deck, table);
                tableImg.add((ImageView) findViewById(R.id.turn));
                tableImg.get(3).setImageResource(getResources().getIdentifier(table.get(3).getCardDrawableName(), "drawable", getPackageName()));

                String player1EvaluationText = handEvaluator.getHandEvaluationTextByRanking(handEvaluator.evaluate(player1, table));
            //    String player2EvaluationText = handEvaluator.getHandEvaluationTextByRanking(handEvaluator.evaluate(player2, table));

                ArrayList<String> turnOdds = oddsCalculator.turnOdds(table, 5000);

                player1HandEvaluationTextView.setText(player1EvaluationText + " - " + turnOdds.get(0));
              //  player2HandEvaluationTextView.setText(player2EvaluationText + " - " + turnOdds.get(1));

                showTurnButton.setVisibility(View.INVISIBLE);
                showRiverButton.setVisibility(View.VISIBLE);
            }
        });


        showRiverButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // set river
                dealer.setOneCard(deck, table);
                tableImg.add((ImageView) findViewById(R.id.river));
                tableImg.get(4).setImageResource(getResources().getIdentifier(table.get(4).getCardDrawableName(), "drawable", getPackageName()));

                ArrayList<String> riverOdds = oddsCalculator.riverOdds(table);

                player1HandEvaluationTextView.setText(player1EvaluationText + " - " + riverOdds.get(0));

//                int player1HandEvaluation = handEvaluator.evaluate(player1, table);
//                ArrayList<Card> player1Hand = new ArrayList<>(handEvaluator.getHand());

//                int player2HandEvaluation = handEvaluator.evaluate(player2, table);
//                ArrayList<Card> player2Hand = new ArrayList<>(handEvaluator.getHand());
//
//               HandWinCalculator handWinCalculator = new HandWinCalculator(player1Hand, player2Hand);
//               int winnerResult = handWinCalculator.calculate(player1HandEvaluation, player2HandEvaluation);
//
//                String player1EvaluationText = handEvaluator.getHandEvaluationTextByRanking(player1HandEvaluation);
//                String player2EvaluationText = handEvaluator.getHandEvaluationTextByRanking(player2HandEvaluation);
//
//               if (winnerResult == Dealer.PLAYER_1)
//               {
//                   player1HandEvaluationTextView.setText(player1EvaluationText + " - Winner");
//                   player2HandEvaluationTextView.setText(player2EvaluationText);
//               }
//               else if (winnerResult == Dealer.PLAYER_2)
//               {
//                   player1HandEvaluationTextView.setText(player1EvaluationText);
//                   player2HandEvaluationTextView.setText(player2EvaluationText + " - Winner");
//               }


                showRiverButton.setVisibility(View.INVISIBLE);
            }
        });


//        player1.add(new Card(Card.ACE,Card.SUIT_HEARTS));
//        player1.add(new Card(Card.TWO,Card.SUIT_HEARTS));
//
//        player2.add(new Card(Card.EIGHT,Card.SUIT_HEARTS));
//        player2.add(new Card(Card.SEVEN,Card.SUIT_HEARTS));
//
//        table.add(new Card(Card.NINE,Card.SUIT_HEARTS));
//        table.add(new Card(Card.QUEEN,Card.SUIT_HEARTS));
//        table.add(new Card(Card.JACK,Card.SUIT_HEARTS));
//        table.add(new Card(Card.TEN,Card.SUIT_HEARTS));
//        table.add(new Card(Card.SIX,Card.SUIT_HEARTS));

        // get untouched deck
//        ArrayList<Card> untouchedDeck = new ArrayList<>(deck.getDeck());
//
//        // set player cards
//        dealer.setPlayersCards(deck, player1, player2);
//        oddsCalculator = new Odds(player1, null,untouchedDeck);
//
//        // set flop
//        dealer.setFlop(deck, table);
//        oddsCalculator.odds(table);
//
//         // set turn
//        dealer.setOneCard(deck, table);
//        oddsCalculator.odds(table);
//
//        // set river
//        dealer.setOneCard(deck, table);
//        oddsCalculator.odds(table);
//
//        //----- winning turn odds
//        oddsCalculator.turnWinningOdds(table);

        //--- evaluate players hands

//        int player1HandResult = handEvaluator.evaluate(player1, table);
//        player1Hand = new ArrayList<>(handEvaluator.getHand());
//        int player2HandResult = handEvaluator.evaluate(player2, table);
//        player2Hand = new ArrayList<>(handEvaluator.getHand());
//
//        //--- calculate winning player
//
//        handWinCalculator = new HandWinCalculator(player1Hand, player2Hand);
//        winnerHandResult = handWinCalculator.calculate(player1HandResult, player2HandResult);

//
//        // set turn
//        dealer.setOneCard(deck, table);
//
//        // set river
//        dealer.setOneCard(deck, table);
    }
}
