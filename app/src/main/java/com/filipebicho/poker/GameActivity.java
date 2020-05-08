package com.filipebicho.poker;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Game Activity class
 */
public class GameActivity extends AppCompatActivity {

    //----- Cards

    // player, opponent and table cards
    private ArrayList<Card> playerCards = new ArrayList<>();
    private ArrayList<Card> opponentCards = new ArrayList<>();
    private ArrayList<Card> table = new ArrayList<>();

    // player and opponent hand
    private final ArrayList<Card> playerHand = new ArrayList<>();
    private final ArrayList<Card> opponentHand = new ArrayList<>();

    //----- Odds

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
    private ArrayList<Image> tableCardsImg = new ArrayList<>();

    // player and opponent odds text
    private TextView playerOddsTextView;
    private TextView opponentOddsTextView;

    // player and opponent money text
    private TextView playerMoneyTextView;
    private TextView opponentMoneyTextView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullScreen();
        setContentView(R.layout.activity_game);

        //init labels
        initLabels();

    }


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
