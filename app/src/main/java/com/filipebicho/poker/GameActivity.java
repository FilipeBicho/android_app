package com.filipebicho.poker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Game Activity class
 */
public class GameActivity extends AppCompatActivity {

    //----- Dealer

    private static final int PLAYER_DEALER = 1;
    private static final int OPPONENT_DEALER = 2;
    private int DEALER = 0;

    //----- Cards

    // player, opponent and table cards
    private ArrayList<Card> playerCards = new ArrayList<>();
    private ArrayList<Card> opponentCards = new ArrayList<>();
    private ArrayList<Card> tableCards = new ArrayList<>();

    // player and opponent hand
    private final ArrayList<Card> playerHand = new ArrayList<>();
    private final ArrayList<Card> opponentHand = new ArrayList<>();

    //----- Odds

    // odds calculator
    private OddsCalculator oddsCalculator;

    // player and opponent pre-flop odds
    private Float playerOddsPreFlop;
    private Float opponentOddsPreFlop;

    // player and opponent flop odds
    private Float playerOddsFlop;
    private Float opponentOddsFlop;

    // player and opponent turn odds
    private Float playerOddsTurn;
    private Float opponentOddsTurn;

    // player and opponent river odds
    private Float playerOddsRiver;
    private Float opponentOddsRiver;

    //----- Labels

    // player, opponent and table cards images
    private ArrayList<ImageView> playerCardsImg = new ArrayList<>();
    private ArrayList<ImageView> opponentCardsImg = new ArrayList<>();
    private ArrayList<ImageView> tableCardsImg = new ArrayList<>();

    // player and opponent odds text
    private TextView playerOddsTextView;
    private TextView opponentOddsTextView;

    // player and opponent money text
    private TextView playerMoneyTextView;
    private TextView opponentMoneyTextView;

    // player and opponent dealer
    private ImageView playerDealerImg;
    private ImageView opponentDealerImg;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullScreen();
        setContentView(R.layout.activity_game);

        // init labels
        initLabels();

        // pre flop
        preFlop();
    }

    /**
     * init labels
     */
    private void initLabels()
    {
        // init game title text
        TextView gameTitleTextView = findViewById(R.id.game_title);
        gameTitleTextView.setText("Filipe VS Joao");

        // init player and opponent names
        TextView playerNameTextView = findViewById(R.id.player_name);
        TextView opponentNameTextView = findViewById(R.id.opponent_name);
        playerNameTextView.setText("Filipe");
        opponentNameTextView.setText("Joao");

        // init player and opponent dealer image
        playerDealerImg = findViewById(R.id.player_dealer);
        opponentDealerImg = findViewById(R.id.opponent_dealer);
    }

    @SuppressLint("SetTextI18n")
    private void preFlop()
    {

        //--- init player, opponent and table cards

        //init cards
        Dealer dealer = new Dealer();
        dealer.setPlayersCards(new Deck(), playerCards, opponentCards);

        // init player cards images
        playerCardsImg = new ArrayList<>();
        playerCardsImg.add(findViewById(R.id.player_card_1));
        playerCardsImg.add(findViewById(R.id.player_card_2));
        playerCardsImg.get(0).setImageResource(getResources().getIdentifier(playerCards.get(0).getCardDrawableName(), "drawable", getPackageName()));
        playerCardsImg.get(1).setImageResource(getResources().getIdentifier(playerCards.get(1).getCardDrawableName(), "drawable", getPackageName()));

        // init opponent cards images
        opponentCardsImg = new ArrayList<>();
        opponentCardsImg.add(findViewById(R.id.opponent_card_1));
        opponentCardsImg.add(findViewById(R.id.opponent_card_2));
        // TODO remove to avoid show opponent cards
        opponentCardsImg.get(0).setImageResource(getResources().getIdentifier(opponentCards.get(0).getCardDrawableName(), "drawable", getPackageName()));
        opponentCardsImg.get(1).setImageResource(getResources().getIdentifier(opponentCards.get(1).getCardDrawableName(), "drawable", getPackageName()));

//        // init table cards images
//        tableCardsImg = new ArrayList<>();
//        tableCardsImg.add(findViewById(R.id.table_flop_1_card));
//        tableCardsImg.add(findViewById(R.id.table_flop_2_card));
//        tableCardsImg.add(findViewById(R.id.table_flop_3_card));
//        tableCardsImg.add(findViewById(R.id.table_turn_card));
//        tableCardsImg.add(findViewById(R.id.table_river_card));
//        tableCardsImg.get(0).setImageResource(getResources().getIdentifier(tableCards.get(0).getCardDrawableName(), "drawable", getPackageName()));
//        tableCardsImg.get(1).setImageResource(getResources().getIdentifier(tableCards.get(1).getCardDrawableName(), "drawable", getPackageName()));
//        tableCardsImg.get(2).setImageResource(getResources().getIdentifier(tableCards.get(2).getCardDrawableName(), "drawable", getPackageName()));
//        tableCardsImg.get(3).setImageResource(getResources().getIdentifier(tableCards.get(3).getCardDrawableName(), "drawable", getPackageName()));
//        tableCardsImg.get(4).setImageResource(getResources().getIdentifier(tableCards.get(4).getCardDrawableName(), "drawable", getPackageName()));

        //--- pre flop odds
        try {
            oddsCalculator = new OddsCalculator(playerCards, opponentCards, new CombinationsCalculator(getResources()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // init player and opponent pre flop odds
        playerOddsPreFlop = (float) oddsCalculator.preFlopOdds(playerCards);
        playerOddsTextView = findViewById(R.id.player_odds);
        playerOddsTextView.setText(playerOddsPreFlop.toString());
        playerOddsTextView.setVisibility(View.VISIBLE); // TODO remove
        opponentOddsPreFlop = (float) oddsCalculator.preFlopOdds(opponentCards);
        opponentOddsTextView = findViewById(R.id.opponent_odds);
        opponentOddsTextView.setText(opponentOddsPreFlop.toString());
        opponentOddsTextView.setVisibility(View.VISIBLE); // TODO remove

        //--- calculate and init dealer

        // calculate
        if (DEALER == 0)
            DEALER = (int) ( Math.random() * 2 + 1);
        else
            DEALER = DEALER == PLAYER_DEALER ? OPPONENT_DEALER : PLAYER_DEALER;

        // init dealer label
        if (DEALER == PLAYER_DEALER)
        {
            playerDealerImg.setVisibility(View.VISIBLE);
            opponentDealerImg.setVisibility(View.INVISIBLE);
        }
        else
        {
            playerDealerImg.setVisibility(View.INVISIBLE);
            opponentDealerImg.setVisibility(View.VISIBLE);
        }

    }

    /**
     * Set Activity with fullscreen
     */
    private void setFullScreen()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
