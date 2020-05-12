package com.filipebicho.poker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Game Activity class
 */
public class GameActivity extends AppCompatActivity {

    //----- Combination Calculator

    CombinationsCalculator combinationsCalculator;

    //----- Rounds

    private String PRE_FLOP = "pre_flop";
    private String FLOP = "flop";
    private String TURN = "turn";
    private String RIVER = "river";
    private String CURRENT_ROUND = "";

    //----- Check flag

    // true if check or bet is still possible
    private Boolean CHECK_BET = true;

    // display check button instead call button on true
    private Boolean CHECK_BUTTON = false;

    //----- Dealer, Blind and player turn

    private int DEALER = -1;
    private int BLIND = -1;
    private int PLAYER_TURN = -1;

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

    // buttons and seekBar
    private Button foldButton;
    private Button callButton;
    private Button betButton;
    private SeekBar betSeekBar;
    private TextView inputBetTextView;

    /**
     *
     * @param savedInstanceState Bundle
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setFullScreen();
        setContentView(R.layout.activity_game);

        // init labels
        initLabels();

        // init buttons click listeners
        initButtonsClickListeners();

        // init player and opponent money
        money[Dealer.PLAYER_1] = (float) START_MONEY;
        money[Dealer.PLAYER_2] = (float) START_MONEY;

        // init combination calculator
        try {
            combinationsCalculator = new CombinationsCalculator(getResources());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // pre flop
        preFlop();
    }

    /**
     * hide buttons and seek bar
     */
    private void hideButtonsAndSeekBar()
    {
        foldButton.setVisibility(View.INVISIBLE);
        callButton.setVisibility(View.INVISIBLE);
        betButton.setVisibility(View.INVISIBLE);
        betSeekBar.setVisibility(View.INVISIBLE);
        inputBetTextView.setVisibility(View.INVISIBLE);
    }

    /**
     * init labels
     */
    private void initLabels()
    {
        playerName = "Filipe";
        opponentName = "Rival";

        // init game title text
        TextView gameTitleTextView = findViewById(R.id.game_title);
        gameTitleTextView.setText(String.format("%s VS %s", playerName, opponentName));

        // init player and opponent names
        TextView playerNameTextView = findViewById(R.id.player_name);
        TextView opponentNameTextView = findViewById(R.id.opponent_name);
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

        // init buttons and seek bar
        foldButton = findViewById(R.id.fold);
        callButton = findViewById(R.id.call);
        betButton = findViewById(R.id.bet);
        betSeekBar = findViewById(R.id.seekBar);
        inputBetTextView = findViewById(R.id.input_bet);
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
     * update player and opponent bet label
     */
    private void updateBetLabels()
    {
        playerBetTextView.setText(String.format("%s €", bet[Dealer.PLAYER_1]));
        opponentBetTextView.setText(String.format("%s €", bet[Dealer.PLAYER_2]));
    }

    /**
     * calculate pre flop odds
     */
    @SuppressLint("StringFormatMatches")
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

                // set game action labels
                if (BLIND == Dealer.PLAYER_1)
                {
                    gameActionTexView.setText(String.format(getString(R.string.player_makes_allin), playerName, bet[Dealer.PLAYER_1]));
                    summaryText += String.format(getString(R.string.player_makes_allin) + "\n", playerName, bet[Dealer.PLAYER_1]);
                    summaryText += String.format(getString(R.string.player_pays_allin) + "\n", opponentName, bet[Dealer.PLAYER_2]);
                }
                else
                {
                    gameActionTexView.setText(String.format(getString(R.string.player_makes_allin), opponentName, bet[Dealer.PLAYER_2]));
                    summaryText += String.format(getString(R.string.player_makes_allin) + "\n", opponentName, bet[Dealer.PLAYER_2]);
                    summaryText += String.format(getString(R.string.player_pays_allin) + "\n", playerName, bet[Dealer.PLAYER_1]);
                }

                // set summary text
                summaryTextView.setText(summaryText);

                // update bet labels
                updateBetLabels();

                // update money and pot labels
                updateMoneyAndPotLabels();

                //TODO five cards showdown
            }
            else
            {
                // Dealer pays small blind
                bet[DEALER] = (float) SMALL_BLIND_VALUE;
                money[DEALER] -= bet[DEALER];

                // calculate pot
                pot = bet[BLIND] + bet[DEALER];

                // set game action, bets and summary labels
                if (DEALER == Dealer.PLAYER_1)
                {
                    gameActionTexView.setText(String.format(getString(R.string.player_pays_small_blind), playerName, SMALL_BLIND_VALUE));
                    summaryText += String.format(getString(R.string.player_pays_small_blind) + "\n", playerName, SMALL_BLIND_VALUE);
                }
                else
                {
                    gameActionTexView.setText(String.format(getString(R.string.player_pays_small_blind), opponentName, SMALL_BLIND_VALUE));
                    summaryText += String.format(getString(R.string.player_pays_small_blind) + "\n", opponentName, SMALL_BLIND_VALUE);
                }

                // set summary text
                summaryTextView.setText(summaryText);

                // update bet labels
                updateBetLabels();

                // update money and pot labels
                updateMoneyAndPotLabels();

                // TODO Blind makes all-in
            }
        }
        // Dealer doesn't have enough money to pay small blind
        else if (money[DEALER] <= SMALL_BLIND_VALUE)
        {
            // Dealer makes all-in
            bet[DEALER] = money[DEALER];
            money[DEALER] -= bet[DEALER];

            // Blind pays all-in
            bet[BLIND] = bet[DEALER];
            money[BLIND] -= bet[BLIND];

            // calculate pot
            pot = bet[BLIND] + bet[DEALER];

            if (DEALER == Dealer.PLAYER_1)
            {
                gameActionTexView.setText(String.format(getString(R.string.player_makes_allin)  + "\n", playerName, bet[Dealer.PLAYER_1]));
                summaryText += String.format(getString(R.string.player_makes_allin)  + "\n", playerName, bet[Dealer.PLAYER_1]);
                summaryText += String.format(getString(R.string.player_pays_allin) + "\n", opponentName, bet[Dealer.PLAYER_2]);

            }
            else
            {
                gameActionTexView.setText(String.format(getString(R.string.player_makes_allin)  + "\n", opponentName, bet[Dealer.PLAYER_2]));
                summaryText += String.format(getString(R.string.player_makes_allin)  + "\n", opponentName, bet[Dealer.PLAYER_2]);
                summaryText += String.format(getString(R.string.player_pays_allin)  + "\n", playerName, bet[Dealer.PLAYER_1]);
            }

            // set summary text
            summaryTextView.setText(summaryText);

            // update bet labels
            updateBetLabels();

            // update money and pot labels
            updateMoneyAndPotLabels();

            //TODO five cards showdown
        }
        // Bind and Dealer have enough money
        else
        {
            // Dealer pays small blind
            bet[DEALER] = (float) SMALL_BLIND_VALUE;
            money[DEALER] -= bet[DEALER];

            // Blind pays big blind
            bet[BLIND] = (float) BIG_BLIND_VALUE;
            money[BLIND] -= bet[BLIND];

            // calculate pot
            pot = bet[BLIND] + bet[DEALER];

            if (BLIND == Dealer.PLAYER_1)
            {
                gameActionTexView.setText(String.format(getString(R.string.player_pays_big_blind), playerName, bet[Dealer.PLAYER_1]));
                summaryText += String.format(getString(R.string.player_pays_small_blind) + "\n", opponentName, bet[Dealer.PLAYER_2]);
                summaryText += String.format(getString(R.string.player_pays_big_blind) + "\n", playerName, bet[Dealer.PLAYER_1]);

            }
            else
            {
                gameActionTexView.setText(String.format(getString(R.string.player_pays_big_blind) + "\n", opponentName, bet[Dealer.PLAYER_2]));
                summaryText += String.format(getString(R.string.player_pays_small_blind) + "\n", playerName, bet[Dealer.PLAYER_1]);
                summaryText += String.format(getString(R.string.player_pays_big_blind) + "\n", opponentName, bet[Dealer.PLAYER_2]);
            }

            // set summary text
            summaryTextView.setText(summaryText);

            // update bet labels
            updateBetLabels();

            // update money and pot labels
            updateMoneyAndPotLabels();

            // set current player
            PLAYER_TURN = DEALER;

            if (PLAYER_TURN == Dealer.PLAYER_2)
            {
                // TODO change to fold_call_bet method
                call();
            }
            else
            {
                // show buttons and seek bar
                foldButton.setVisibility(View.VISIBLE);
                callButton.setVisibility(View.VISIBLE);
                betButton.setVisibility(View.VISIBLE);
                betSeekBar.setVisibility(View.VISIBLE);
                inputBetTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * init preFlop cards, odds and bets
     */
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
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

        //--- init odds
        oddsCalculator = new OddsCalculator(playerCards, opponentCards, combinationsCalculator);


        // init player and opponent pre flop odds
        playerOddsPreFlop = (float) oddsCalculator.preFlopOdds(playerCards);
        playerOddsTextView = findViewById(R.id.player_odds);
        playerOddsTextView.setText(playerOddsPreFlop.toString() + " %");
        playerOddsTextView.setVisibility(View.VISIBLE); // TODO remove
        opponentOddsPreFlop = (float) oddsCalculator.preFlopOdds(opponentCards);
        opponentOddsTextView = findViewById(R.id.opponent_odds);
        opponentOddsTextView.setText(opponentOddsPreFlop.toString() + " %");
        opponentOddsTextView.setVisibility(View.VISIBLE); // TODO remove

        //--- calculate and init dealer

        // random select dealer or switch dealer
        if (DEALER == -1)
            DEALER = (int) ( Math.random() * 1 + 0);
        else
            DEALER = DEALER == Dealer.PLAYER_1 ? Dealer.PLAYER_2 : Dealer.PLAYER_1;

        DEALER = 0;

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

        // init summary new game
        gameNumber++;
        summaryText += String.format(getString(R.string.game_number), gameNumber);
        summaryTextView.setText(summaryText);

        // init current round
        CURRENT_ROUND = PRE_FLOP;

        // check or bet is still possible
        CHECK_BET = true;

        // calculate pre flop bets
        preFlopBets();
    }

    /**
     * restart game
     */
    private void restartGame()
    {
        // reset pot
        pot = (float) 0;

        // reset bets
        bet[Dealer.PLAYER_1] = (float) 0;
        bet[Dealer.PLAYER_2] = (float) 0;

        // update bet labels
        updateBetLabels();

        // update money and pot labels
        updateMoneyAndPotLabels();

        // update action
        gameActionTexView.setText(R.string.new_game);

        // reset opponent cards image
        opponentCardsImg.get(0).setImageResource(R.drawable.card_back);
        opponentCardsImg.get(1).setImageResource(R.drawable.card_back);

        // hide table cards image
        for (ImageView tableCard : tableCardsImg)
            tableCard.setVisibility(View.INVISIBLE);

        // reset player, opponent and table cards
        playerCards.clear();
        opponentCards.clear();
        playerHand.clear();
        opponentHand.clear();
        tableCards.clear();

        // both player have money to play
        if (money[Dealer.PLAYER_1] > 0 && money[Dealer.PLAYER_2] > 0)
            preFlop();
    }

    /**
     * Fold
     */
    @SuppressLint("StringFormatMatches")
    private void fold()
    {
        // init player and opponent
        final int player = PLAYER_TURN;
        final int opponent = player == Dealer.PLAYER_1 ? Dealer.PLAYER_2 : Dealer.PLAYER_1;

        // set action
        if (player == Dealer.PLAYER_1)
            gameActionTexView.setText(String.format(getString(R.string.player_folds), playerName));
        else
            gameActionTexView.setText(String.format(getString(R.string.player_folds), opponentName));

        // opponent wins the pot
        money[opponent] += pot;

        // set summary text
        summaryText += gameActionTexView.getText() + "\n";
        summaryText += player == Dealer.PLAYER_1 ? String.format(getString(R.string.player_wins_pot), opponentName, pot) : String.format(getString(R.string.player_wins_pot), playerName, pot);

        // set summary text
        summaryTextView.setText(summaryText);

        restartGame();
    }

    /**
     * Call
     */
    @SuppressLint("StringFormatMatches")
    private void call()
    {
        // init player and opponent
        final int player = PLAYER_TURN;
        final int opponent = player == Dealer.PLAYER_1 ? Dealer.PLAYER_2 : Dealer.PLAYER_1;

        // init call amount
        float callAmount = bet[opponent] - bet[player];

        // all-in if player don't have enough money to make a call
        if (money[player] <= callAmount)
        {
            // set new call amount
            callAmount = callAmount - money[player];

            // allin
            bet[player] += callAmount;

            //update money
            money[player] -= callAmount;
            money[opponent] += callAmount;

            // opponent equals player bet
            bet[opponent] =  bet[player];

            // calculate pot
            pot = bet[player] + bet[opponent];

            // set action
            if (player == Dealer.PLAYER_1)
            {
                gameActionTexView.setText(String.format(getString(R.string.player_makes_allin), playerName, bet[Dealer.PLAYER_1]));
                summaryText += String.format(getString(R.string.player_makes_allin)  + "\n", playerName, bet[Dealer.PLAYER_1]);
            }
            else
            {
                gameActionTexView.setText(String.format(getString(R.string.player_makes_allin), opponentName, bet[Dealer.PLAYER_2]));
                summaryText += String.format(getString(R.string.player_makes_allin)  + "\n", opponentName, bet[Dealer.PLAYER_2]);
            }

            // set summary text
            summaryTextView.setText(summaryText);

            // update bet labels
            updateBetLabels();

            // update money and pot labels
            updateMoneyAndPotLabels();

            // TODO show down
        }
        else
        {
            // player equals opponent bet
            bet[player] += callAmount;

            // update money
            money[player] -= callAmount;

            // calculate pot
            pot = bet[player] + bet[opponent];

            if (player == Dealer.PLAYER_1)
            {
                gameActionTexView.setText(String.format(getString(R.string.player_calls), playerName, callAmount));
                summaryText += String.format(getString(R.string.player_calls) + "\n", playerName, callAmount);
            }
            else
            {
                gameActionTexView.setText(String.format(getString(R.string.player_calls), opponentName, callAmount));
                summaryText += String.format(getString(R.string.player_calls) + "\n", opponentName, callAmount);
            }

            // set summary text
            summaryTextView.setText(summaryText);

            // update bet labels
            updateBetLabels();

            // update money and pot labels
            updateMoneyAndPotLabels();

            PLAYER_TURN = opponent;

            // if is pre flop call then other player still has to play
            if (CHECK_BET && CURRENT_ROUND.equals(PRE_FLOP))
            {
                if (PLAYER_TURN == Dealer.PLAYER_2)
                {
                    check();
                }
                else
                {
                    CHECK_BUTTON = true;
                    callButton.setText(R.string.check_button);
                    callButton.setVisibility(View.VISIBLE);
                    betButton.setVisibility(View.VISIBLE);
                    betSeekBar.setVisibility(View.VISIBLE);
                    inputBetTextView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void check()
    {
        // init player and opponent
        final int player = PLAYER_TURN;
        final int opponent = player == Dealer.PLAYER_1 ? Dealer.PLAYER_2 : Dealer.PLAYER_1;

        // set action
        if (player == Dealer.PLAYER_1)
        {
            gameActionTexView.setText(String.format(getString(R.string.player_checks), playerName));
            summaryText += String.format(getString(R.string.player_checks)  + "\n", playerName);
        }
        else
        {
            gameActionTexView.setText(String.format(getString(R.string.player_checks), opponentName));
            summaryText += String.format(getString(R.string.player_checks)  + "\n", opponentName);
        }

        // set summary text
        summaryTextView.setText(summaryText);

        // opponent still has to play if is not pre flop
        if (CHECK_BET && !CURRENT_ROUND.equals(PRE_FLOP))
        {
            // it's not possible to check again
            CHECK_BET = false;

            if (PLAYER_TURN == Dealer.PLAYER_2)
            {
                check();
            }
            else
            {
                CHECK_BUTTON = true;
                callButton.setText(R.string.check_button);
                callButton.setVisibility(View.VISIBLE);
                betButton.setVisibility(View.VISIBLE);
                betSeekBar.setVisibility(View.VISIBLE);
                inputBetTextView.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            // TODO show flop, turn or river
        }

    }

    /**
     * init buttons click listeners
     */
    private void initButtonsClickListeners()
    {
        foldButton.setOnClickListener(v -> {
            hideButtonsAndSeekBar();
            fold();
        });

        callButton.setOnClickListener(v -> {
            hideButtonsAndSeekBar();

            if (CHECK_BUTTON)
                check();
            else
                call();
        });
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
