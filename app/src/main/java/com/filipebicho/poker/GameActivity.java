package com.filipebicho.poker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Game Activity class
 */
public class GameActivity extends AppCompatActivity {

    //----- Dealer

    private int DEALER = -1;
    private int BLIND = -1;

    //----- Big and small blind values

    private int BIG_BLIND_VALUE = 40;
    private int SMALL_BLIND_VALUE = 20;

    //----- Start money

    private int START_MONEY = 1500;

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

    //----- Bets

    private Float[] bet = new Float[2];
    private Float pot;

    //----- Game number

    int gameNumber = 0;

    //----- Money

    private Float[] money = new Float[2];

    //----- Names

    // player and opponent names
    private String playerName;
    private String opponentName;

    //----- Summary
    private String summaryText = "";

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

    // bet's and pot
    private TextView playerBetTextView;
    private TextView opponentBetTextView;
    private TextView potTextView;

    // summary
    private TextView summaryTextView;

    // game action
    private TextView gameActionTexView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullScreen();
        setContentView(R.layout.activity_game);

        // init labels
        initLabels();

        money[Dealer.PLAYER_1] = (float) 30;
        money[Dealer.PLAYER_2] = (float) START_MONEY;


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

        playerName = "Filipe";
        opponentName = "Rival";

        playerNameTextView.setText(playerName);
        opponentNameTextView.setText(opponentName);

        // init player and opponent dealer image
        playerDealerImg = findViewById(R.id.player_dealer);
        opponentDealerImg = findViewById(R.id.opponent_dealer);

        // init player and opponent bet labels
        playerBetTextView = findViewById(R.id.player_bet_value);
        opponentBetTextView = findViewById(R.id.opponent_bet_value);

        // init player and opponent money labels
        playerMoneyTextView = findViewById(R.id.player_money);
        opponentMoneyTextView = findViewById(R.id.opponent_money);

        // init pot label
        potTextView = findViewById(R.id.pot);

        // init summary
        summaryTextView = findViewById(R.id.summary);

        // init game action
        gameActionTexView = findViewById(R.id.game_action);
    }

    /**
     * update player bet, opponent bet and pot labels
     */
    private void updateMoneyAndPotLabels()
    {
        playerMoneyTextView.setText(String.format("%s €", money[Dealer.PLAYER_1]));
        opponentMoneyTextView.setText(String.format("%s €", money[Dealer.PLAYER_2]));
        potTextView.setText(String.format("%s €", pot));
    }

    /**
     * calculate pre flop odds
     */
    private void preFlopBets()
    {
        // Blind doesn't have enough money to pay big blind
        if (money[BLIND] <= BIG_BLIND_VALUE)
        {
            // Blind doesn't have enough money to pay small blind
            if (money[BLIND] <= SMALL_BLIND_VALUE)
            {
                // Blind makes all-in
                bet[BLIND] = money[BLIND];
                money[BLIND] -= bet[BLIND];

                // Dealer pays all-in
                bet[DEALER] = bet[BLIND];
                money[DEALER] -= bet[DEALER];

                // calculate pot
                pot = bet[BLIND] + bet[DEALER];

                updateMoneyAndPotLabels();

                // set game action labels
                if (BLIND == Dealer.PLAYER_1)
                    gameActionTexView.setText(String.format("%s bets %s €", playerName, bet[Dealer.PLAYER_1]));
                else
                    gameActionTexView.setText(String.format("%s bets %s €", opponentName, bet[Dealer.PLAYER_2]));

                // set bets labels
                playerBetTextView.setText(String.format("%s €", bet[Dealer.PLAYER_1]));
                opponentBetTextView.setText(String.format("%s €", bet[Dealer.PLAYER_2]));

                // set summary text
                summaryText += String.format("%s bets %s €\n", playerName, bet[Dealer.PLAYER_1]);
                summaryText += String.format("%s bets %s €\n", opponentName, bet[Dealer.PLAYER_2]);
                summaryTextView.setText(summaryText);

                //TODO five cards showdown
            }
            else
            {
                // Dealer pays small blind
                bet[DEALER] = (float) SMALL_BLIND_VALUE;
                money[DEALER] -= bet[DEALER];

                // calculate pot
                pot = bet[BLIND] + bet[DEALER];

                updateMoneyAndPotLabels();

                // set game action, bets and summary labels
                if (BLIND == Dealer.PLAYER_1)
                {
                    gameActionTexView.setText(String.format("%s pays small blind (%s €)\n\"", playerName, SMALL_BLIND_VALUE));
                    summaryText += String.format("%s pays small blind (%s €)\n\"", playerName, SMALL_BLIND_VALUE);
                }
                else
                {
                    gameActionTexView.setText(String.format("%s pays small blind (%s €)\n\"", opponentName, SMALL_BLIND_VALUE));
                    summaryText += String.format("%s pays small blind (%s €)\n\"", opponentName, SMALL_BLIND_VALUE);
                }

                playerBetTextView.setText(String.format("%s €", bet[Dealer.PLAYER_1]));
                opponentBetTextView.setText(String.format("%s €", bet[Dealer.PLAYER_2]));

                summaryTextView.setText(summaryText);

                // TODO Blind makes all-in
            }
        }
    }

    /**
     * init preFlop cards, odds and bets
     */
    @SuppressLint("DefaultLocale")
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

        // random select dealer or switch dealer
        if (DEALER == -1)
            DEALER = 1; //(int) ( Math.random() * 1 + 0);
        else
            DEALER = DEALER == Dealer.PLAYER_1 ? Dealer.PLAYER_2 : Dealer.PLAYER_1;

        // init big blind
        BLIND = DEALER == Dealer.PLAYER_1 ? Dealer.PLAYER_2 : Dealer.PLAYER_1;

        // init dealer label
        if (DEALER == Dealer.PLAYER_1)
        {
            playerDealerImg.setVisibility(View.VISIBLE);
            opponentDealerImg.setVisibility(View.INVISIBLE);
        }
        else
        {
            playerDealerImg.setVisibility(View.INVISIBLE);
            opponentDealerImg.setVisibility(View.VISIBLE);
        }

        //---- init bet's and bet's label

        // reset bet's and pot values
        bet[Dealer.PLAYER_1] = (float) 0;
        bet[Dealer.PLAYER_2] = (float) 0;
        pot = (float) 0;

        // reset bet's and pot labels
        playerBetTextView.setText(String.format("%s €", bet[Dealer.PLAYER_1]));
        opponentBetTextView.setText(String.format("%s €", bet[Dealer.PLAYER_2]));
        potTextView.setText(String.format("%s €", pot));

        // init summary new game
        gameNumber++;
        summaryText += String.format("\n------ Game %d-----\n\n", gameNumber);
        summaryTextView.setText(summaryText);

        // calculate pre flop bets
        preFlopBets();
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
