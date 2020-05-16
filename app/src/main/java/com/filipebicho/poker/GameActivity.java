package com.filipebicho.poker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
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

    //----- Animation time

    private int ANIMATION_TIME = 500;

    //----- Cards

    // player, opponent and table cards
    private ArrayList<Card> playerCards = new ArrayList<>();
    private ArrayList<Card> opponentCards = new ArrayList<>();
    private ArrayList<Card> tableCards = new ArrayList<>();

    // player and opponent hand
    private ArrayList<Card> playerHand = new ArrayList<>();
    private ArrayList<Card> opponentHand = new ArrayList<>();
    private int playerHandRanking = 0;
    private int opponentHandRanking = 0;

    //----- Odds

    // odds calculator
    private OddsCalculator oddsCalculator;

    // player and opponent pre-flop odds
    private Float playerOddsPreFlop;
    private Float opponentOddsPreFlop;

    // flop odds
    private ArrayList<Float> flopOdds;

    // turn odds
    private ArrayList<Float> turnOdds;

    // river odds
    private ArrayList<Float> riverOdds;

    //----- Dealer

    Dealer dealer;

    //----- Deck

    Deck deck;

    //----- Bets

    private Float[] bet = new Float[2];
    private Float pot = (float) 0;
    private Float savedPot = (float) 0;

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

    // player and opponent hand ranking text
    private TextView playerHandRankingTextView;
    private TextView opponentHandRankingTextView;

    //------ on create method

    /**
     *
     * @param savedInstanceState Bundle
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setFullScreen();
        setContentView(R.layout.activity_game);

        // init player and opponent money
        money[Dealer.PLAYER_1] = (float) START_MONEY;
        money[Dealer.PLAYER_2] = (float) START_MONEY;

        // init labels
        initLabels();

        // init buttons click listeners
        initButtonsAndSeekBarListeners();

        // init combination calculator
        try {
            combinationsCalculator = new CombinationsCalculator(getResources());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // start a new game
        newGame();
    }

    //----- private instance methods

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

        // init table cards images
        tableCardsImg = new ArrayList<>();
        tableCardsImg.add(findViewById(R.id.table_flop_1_card));
        tableCardsImg.add(findViewById(R.id.table_flop_2_card));
        tableCardsImg.add(findViewById(R.id.table_flop_3_card));
        tableCardsImg.add(findViewById(R.id.table_turn_card));
        tableCardsImg.add(findViewById(R.id.table_river_card));

        // init player and opponent cards
        playerCardsImg = new ArrayList<>();
        playerCardsImg.add(findViewById(R.id.player_card_1));
        playerCardsImg.add(findViewById(R.id.player_card_2));
        opponentCardsImg = new ArrayList<>();
        opponentCardsImg.add(findViewById(R.id.opponent_card_1));
        opponentCardsImg.add(findViewById(R.id.opponent_card_2));

        // init player and opponent odds labels
        playerOddsTextView = findViewById(R.id.player_odds);
        opponentOddsTextView = findViewById(R.id.opponent_odds);

        // init player and opponent hand ranking
        playerHandRankingTextView = findViewById(R.id.player_ranking);
        opponentHandRankingTextView = findViewById(R.id.opponent_ranking);
    }

    /**
     * update player and opponent bet, money, pot and summary text labels
     */
    private void updateLabels()
    {
        // money labels
        playerMoneyTextView.setText(String.format("%s €", money[Dealer.PLAYER_1]));
        opponentMoneyTextView.setText(String.format("%s €", money[Dealer.PLAYER_2]));

        // pot labels
        potTextView.setText(String.format("%s €", pot));

        // bet labels
        playerBetTextView.setText(String.format("%s €", bet[Dealer.PLAYER_1]));
        opponentBetTextView.setText(String.format("%s €", bet[Dealer.PLAYER_2]));

        // summary label
        summaryTextView.setText(summaryText);
    }

    /**
     * init new game
     */
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void newGame()
    {
        //--- init dealer
        dealer = new Dealer();

        //--- init deck
        deck = new Deck();

        // reset player, opponent and table cards
        playerCards.clear();
        opponentCards.clear();
        playerHand.clear();
        opponentHand.clear();
        tableCards.clear();

        // reset seek bar progress
        betSeekBar.setProgress(BIG_BLIND_VALUE);

        //--- init player, opponent and table cards

        // init cards
        dealer.setPlayersCards(deck, playerCards, opponentCards);

        // init player cards images
        playerCardsImg.get(0).setImageResource(getResources().getIdentifier(playerCards.get(0).getCardDrawableName(), "drawable", getPackageName()));
        playerCardsImg.get(1).setImageResource(getResources().getIdentifier(playerCards.get(1).getCardDrawableName(), "drawable", getPackageName()));

        // init opponent cards images with back card image
        opponentCardsImg.get(0).setImageResource(R.drawable.card_back);
        opponentCardsImg.get(1).setImageResource(R.drawable.card_back);

        // fade out table cards
        tableCardsImg.get(0).setAlpha(0.0f);
        tableCardsImg.get(1).setAlpha(0.0f);
        tableCardsImg.get(2).setAlpha(0.0f);
        tableCardsImg.get(3).setAlpha(0.0f);
        tableCardsImg.get(4).setAlpha(0.0f);

        // reset player and opponent hand ranking text
        playerHandRankingTextView.setText("");
        opponentHandRankingTextView.setText("");

        // hide odds labels
        playerOddsTextView.setVisibility(View.INVISIBLE);
        opponentOddsTextView.setVisibility(View.INVISIBLE);

        //--- init odds
        oddsCalculator = new OddsCalculator(playerCards, opponentCards, combinationsCalculator);

        // calculate player and opponent pre flop odds
        playerOddsPreFlop = (float) oddsCalculator.preFlopOdds(playerCards);
        opponentOddsPreFlop = (float) oddsCalculator.preFlopOdds(opponentCards);

        // set player and opponent odds labels
        playerOddsTextView.setText(playerOddsPreFlop.toString() + " %");
        opponentOddsTextView.setText(opponentOddsPreFlop.toString() + " %");

        //--- calculate and init dealer

        // random select dealer or switch dealer
        if (DEALER == -1)
            DEALER = (int) ( Math.random() * 1 + 0);
        else
            DEALER = DEALER == Dealer.PLAYER_1 ? Dealer.PLAYER_2 : Dealer.PLAYER_1;

        // set big blind
        BLIND = DEALER == Dealer.PLAYER_1 ? Dealer.PLAYER_2 : Dealer.PLAYER_1;

        // set dealer label
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
        savedPot = (float) 0;

        // set summary new game text
        gameNumber++;
        summaryText += String.format(getString(R.string.game_number), gameNumber);

        // update labels
        updateLabels();

        // update action
        gameActionTexView.setText(R.string.new_game);

        // set current round
        CURRENT_ROUND = PRE_FLOP;

        // check or bet is possible
        CHECK_BET = true;

        // set player turn
        PLAYER_TURN = BLIND;

        preFlop();
    }

    /**
     * pre flop
     */
    @SuppressLint("StringFormatMatches")
    private void preFlop()
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
                pot = savedPot + bet[BLIND] + bet[DEALER];

                // set summary text and game action label
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

                // update labels
                updateLabels();

                // show flop cards
                flop();
            }
            else
            {
                // Dealer pays small blind
                bet[DEALER] = (float) SMALL_BLIND_VALUE;
                money[DEALER] -= bet[DEALER];

                // calculate pot
                pot = savedPot + bet[BLIND] + bet[DEALER];

                // set summary text and game action label
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

                // update labels
                updateLabels();

                // blind makes all in
                allIn();
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
            pot = savedPot + bet[BLIND] + bet[DEALER];

            // set summary text and game action label
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

            // update labels
            updateLabels();

            // show flop
            flop();
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
            pot = savedPot + bet[BLIND] + bet[DEALER];

            // set summary text and game action label
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

            // update labels
            updateLabels();

            // change player turn
            PLAYER_TURN = DEALER;

            if (PLAYER_TURN == Dealer.PLAYER_2)
            {
                if (money[DEALER] + bet[DEALER] > BIG_BLIND_VALUE)
                    bet(); // TODO change to fold_call_bet method
                else
                    call(); // TODO change to fold_call method
            }
            else
            {
                // show fold and check button
                foldButton.setVisibility(View.VISIBLE);
                callButton.setVisibility(View.VISIBLE);

                // show bet button and seek bar if Dealer have enough money to bet
                if (money[DEALER] + bet[DEALER] > BIG_BLIND_VALUE)
                {
                    betButton.setVisibility(View.VISIBLE);
                    betSeekBar.setVisibility(View.VISIBLE);
                    inputBetTextView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * flop
     */
    @SuppressLint("SetTextI18n")
    private void flop()
    {
        // set current round
        CURRENT_ROUND = FLOP;

        // check is possible
        CHECK_BET = true;

        // reset bets
        bet[Dealer.PLAYER_1] = (float) 0;
        bet[Dealer.PLAYER_2] = (float) 0;

        // reset seek bar progress
        betSeekBar.setProgress(BIG_BLIND_VALUE);

        // save pot state
        savedPot = pot;

        // set flop cards
        dealer.setFlop(deck, tableCards);

        if (money[Dealer.PLAYER_1] > 0 && money[Dealer.PLAYER_2] > 0)
        {
            // calculate odds
            flopOdds = oddsCalculator.flopOdds(tableCards, 1000);
        }
        else
        {
            // show opponent cards
            opponentCardsImg.get(0).setImageResource(getResources().getIdentifier(opponentCards.get(0).getCardDrawableName(), "drawable", getPackageName()));
            opponentCardsImg.get(1).setImageResource(getResources().getIdentifier(opponentCards.get(1).getCardDrawableName(), "drawable", getPackageName()));

            // calculate odds
            flopOdds = oddsCalculator.playerVsPlayerFlopOdds(tableCards);

            // set odds text view
            playerOddsTextView.setText(flopOdds.get(Dealer.PLAYER_1).toString() + " %");
            playerOddsTextView.setVisibility(View.VISIBLE);
            opponentOddsTextView.setText(flopOdds.get(Dealer.PLAYER_2).toString() + " %");
            opponentOddsTextView.setVisibility(View.VISIBLE);
        }

        // set summary text
        summaryText += getString(R.string.flop_header);
        summaryText += tableCards.toString() + "\n";

        // update labels
        updateLabels();

        // make sure blind plays first
        PLAYER_TURN = BLIND;

        //----- display flop and buttons

        // init flop cards
        tableCardsImg.get(0).setImageResource(getResources().getIdentifier(tableCards.get(0).getCardDrawableName(), "drawable", getPackageName()));
        tableCardsImg.get(1).setImageResource(getResources().getIdentifier(tableCards.get(1).getCardDrawableName(), "drawable", getPackageName()));
        tableCardsImg.get(2).setImageResource(getResources().getIdentifier(tableCards.get(2).getCardDrawableName(), "drawable", getPackageName()));

        // display flop animation
        tableCardsImg.get(0).animate().alpha(1.0f).setDuration(ANIMATION_TIME).withEndAction(() -> {
            tableCardsImg.get(1).animate().alpha(1.0f).setDuration(ANIMATION_TIME).withEndAction(() -> {
                tableCardsImg.get(2).animate().alpha(1.0f).setDuration(ANIMATION_TIME).withEndAction(() -> {

                    if (money[Dealer.PLAYER_1] > 0 && money[Dealer.PLAYER_2] > 0)
                    {
                        if (PLAYER_TURN == Dealer.PLAYER_2)
                        {
                            bet(); //TODO check_bet
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
                        turn();
                    }
                });
            });
        });
    }

    /**
     * turn
     */
    @SuppressLint("SetTextI18n")
    private void turn()
    {
        // set current round
        CURRENT_ROUND = TURN;

        // check is possible
        CHECK_BET = true;

        // reset bets
        bet[Dealer.PLAYER_1] = (float) 0;
        bet[Dealer.PLAYER_2] = (float) 0;

        // reset seek bar progress
        betSeekBar.setProgress(BIG_BLIND_VALUE);

        // save pot state
        savedPot = pot;

        // set turn card
        dealer.setOneCard(deck, tableCards);

        if (money[Dealer.PLAYER_1] > 0 && money[Dealer.PLAYER_2] > 0)
        {
            // calculate odds
            turnOdds = oddsCalculator.turnOdds(tableCards, 1000);
        }
        else
        {
            // show opponent cards
            opponentCardsImg.get(0).setImageResource(getResources().getIdentifier(opponentCards.get(0).getCardDrawableName(), "drawable", getPackageName()));
            opponentCardsImg.get(1).setImageResource(getResources().getIdentifier(opponentCards.get(1).getCardDrawableName(), "drawable", getPackageName()));

            // calculate odds
            turnOdds = oddsCalculator.playerVsPlayerTurnOdds(tableCards, deck.getDeck());

            // set odds text view
            playerOddsTextView.setText(turnOdds.get(Dealer.PLAYER_1).toString() + " %");
            playerOddsTextView.setVisibility(View.VISIBLE);
            opponentOddsTextView.setText(turnOdds.get(Dealer.PLAYER_2).toString() + " %");
            opponentOddsTextView.setVisibility(View.VISIBLE);
        }

        // set summary text
        summaryText += getString(R.string.turn_header);
        summaryText += tableCards.toString() + "\n";

        // update labels
        updateLabels();

        // make sure blind plays first
        PLAYER_TURN = BLIND;

        //----- display flop and buttons

        // init turn card
        tableCardsImg.get(3).setImageResource(getResources().getIdentifier(tableCards.get(3).getCardDrawableName(), "drawable", getPackageName()));

        // display flop animation
        tableCardsImg.get(3).animate().alpha(1.0f).setDuration(ANIMATION_TIME).withEndAction(() -> {
            if (money[Dealer.PLAYER_1] > 0 && money[Dealer.PLAYER_2] > 0)
            {
                if (PLAYER_TURN == Dealer.PLAYER_2)
                {
                    bet(); //TODO check_bet
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
                river();
            }
        });
    }

    /**
     * River
     */
    @SuppressLint("SetTextI18n")
    private void river()
    {
        // set current round
        CURRENT_ROUND = RIVER;

        // check is possible
        CHECK_BET = true;

        // reset bets
        bet[Dealer.PLAYER_1] = (float) 0;
        bet[Dealer.PLAYER_2] = (float) 0;

        // reset seek bar
        betSeekBar.setProgress(BIG_BLIND_VALUE);

        // save pot state
        savedPot = pot;

        // set river card
        dealer.setOneCard(deck, tableCards);

        // calculate odds
        if (money[Dealer.PLAYER_1] > 0 && money[Dealer.PLAYER_2] > 0)
        {
            // calculate odds
            turnOdds = oddsCalculator.riverOdds(tableCards, 1000);
        }
        else
        {
            // show opponent cards
            opponentCardsImg.get(0).setImageResource(getResources().getIdentifier(opponentCards.get(0).getCardDrawableName(), "drawable", getPackageName()));
            opponentCardsImg.get(1).setImageResource(getResources().getIdentifier(opponentCards.get(1).getCardDrawableName(), "drawable", getPackageName()));

            // display odds
            playerOddsTextView.setVisibility(View.VISIBLE);
            opponentOddsTextView.setVisibility(View.VISIBLE);
        }

        // set summary text
        summaryText += getString(R.string.river_header);
        summaryText += tableCards.toString() + "\n";

        // update labels
        updateLabels();

        // make sure blind plays first
        PLAYER_TURN = BLIND;

        //----- display flop and buttons

        // init river card
        tableCardsImg.get(4).setImageResource(getResources().getIdentifier(tableCards.get(4).getCardDrawableName(), "drawable", getPackageName()));

        // display flop animation
        tableCardsImg.get(4).animate().alpha(1.0f).setDuration(ANIMATION_TIME).withEndAction(() -> {
            if (money[Dealer.PLAYER_1] > 0 && money[Dealer.PLAYER_2] > 0)
            {
                if (PLAYER_TURN == Dealer.PLAYER_2)
                {
                    bet(); //TODO check_bet
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
                showDown();
            }
        });
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

        // set action label
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

        newGame();
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

            // reset call amount
            callAmount = bet[player];

            // player makes allin
            bet[player] = money[player];
            money[player] -= bet[player];
            bet[player] += callAmount;

            // opponent money gets the difference
            money[opponent] += bet[opponent];
            bet[opponent] = bet[player];
            money[opponent] -= bet[opponent];

            // calculate pot
            pot = savedPot + bet[player] + bet[opponent];

            // set summary text and game action label
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

            // update labels
            updateLabels();

            if (CURRENT_ROUND.equals(PRE_FLOP))
                flop();
            else if (CURRENT_ROUND.equals(FLOP))
                turn();
            else if (CURRENT_ROUND.equals(TURN))
                river();
            else if (CURRENT_ROUND.equals(RIVER))
                showDown();
        }
        else
        {
            // player equals opponent bet
            bet[player] += callAmount;

            // update money
            money[player] -= callAmount;

            // calculate pot
            pot = savedPot + bet[player] + bet[opponent];

            // set summary text and game action label
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

            // update labels
            updateLabels();

            // change player turn
            PLAYER_TURN = opponent;

            // if is pre flop call then other player still has the possibility to check
            if (CHECK_BET && CURRENT_ROUND.equals(PRE_FLOP))
            {
                if (PLAYER_TURN == Dealer.PLAYER_2)
                {
                    bet(); //TODO implement check_bet
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
                if (CURRENT_ROUND.equals(PRE_FLOP))
                    flop();
                else if (CURRENT_ROUND.equals(FLOP))
                    turn();
                else if (CURRENT_ROUND.equals(TURN))
                    river();
                else if (CURRENT_ROUND.equals(RIVER))
                    showDown();
            }
        }
    }

    /**
     * Check
     */
    private void check()
    {
        // set action
        if (PLAYER_TURN == Dealer.PLAYER_1)
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
                bet(); //TODO implement check_bet
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
            if (CURRENT_ROUND.equals(PRE_FLOP))
                flop();
            else if (CURRENT_ROUND.equals(FLOP))
                turn();
            else if (CURRENT_ROUND.equals(TURN))
                river();
            else if (CURRENT_ROUND.equals(RIVER))
                showDown();
        }
    }

    /**
     * Bet
     */
    @SuppressLint("StringFormatMatches")
    private void bet()
    {
        // init player and opponent
        final int player = PLAYER_TURN;
        final int opponent = player == Dealer.PLAYER_1 ? Dealer.PLAYER_2 : Dealer.PLAYER_1;

        // it's not possible to check anymore
        CHECK_BET = false;

        // all-in if player don't have enough money to pay opponent bet
        if (bet[opponent] >= money[player] + bet[player])
        {
           allIn();
        }
        else
        {
            // TODO implement bot bet
            Float currentBet = player == Dealer.PLAYER_1 ? betSeekBar.getProgress() : (float) BIG_BLIND_VALUE;

            // player bet is all-in
            if (currentBet >= money[player])
            {
                allIn();
            }
            else
            {
                // remove old bet from money
                money[player] += bet[player];

                // update bet
                bet[player] = currentBet + bet[opponent];

                // all-in
                if (bet[player] >= money[player])
                    bet[player] = money[player];

                // update money
                money[player] -= bet[player];

                // update pot
                pot = savedPot + bet[player] + bet[opponent];

                // set action
                if (player == Dealer.PLAYER_1)
                {
                    gameActionTexView.setText(String.format(getString(R.string.player_bets), playerName, bet[Dealer.PLAYER_1]));
                    summaryText += String.format(getString(R.string.player_bets)  + "\n", playerName, bet[Dealer.PLAYER_1]);
                }
                else
                {
                    gameActionTexView.setText(String.format(getString(R.string.player_bets), opponentName, bet[Dealer.PLAYER_2]));
                    summaryText += String.format(getString(R.string.player_bets)  + "\n", opponentName, bet[Dealer.PLAYER_2]);
                }

                // update labels
                updateLabels();

                // change player turn
                PLAYER_TURN = opponent;

                if (PLAYER_TURN == Dealer.PLAYER_2)
                {
                    // opponent can still raise the bet
                    if (money[opponent] + bet[opponent] > bet[player])
                    {
                        bet(); // TODO fold_call_bet
                    }
                    else
                    {
                        allIn();// TODO fold_allin
                    }
                }
                else
                {
                    betSeekBar.setProgress(BIG_BLIND_VALUE);

                    // show buttons and seek bar
                    foldButton.setVisibility(View.VISIBLE);
                    CHECK_BUTTON = false;
                    callButton.setText(R.string.call_button);
                    callButton.setVisibility(View.VISIBLE);

                    // player has money and opponent can still raise the bet
                    if (money[player] > 0 && money[opponent] + bet[opponent] > bet[player])
                    {
                        betButton.setVisibility(View.VISIBLE);
                        betSeekBar.setVisibility(View.VISIBLE);
                        inputBetTextView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    /**
     * All In
     */
    private void allIn()
    {
        // init player and opponent
        final int player = PLAYER_TURN;
        final int opponent = player == Dealer.PLAYER_1 ? Dealer.PLAYER_2 : Dealer.PLAYER_1;

        // it's not possible to check anymore
        CHECK_BET = false;

        // bet all money
        bet[player] += money[player];
        money[player] = (float) 0;

        // update pot
        pot = savedPot + bet[player] + bet[opponent];

        // set game action and summary text labels
        if (player == Dealer.PLAYER_1)
        {
            gameActionTexView.setText(String.format(getString(R.string.player_makes_allin)  + "\n", playerName, bet[Dealer.PLAYER_1]));
            summaryText += String.format(getString(R.string.player_makes_allin)  + "\n", playerName, bet[Dealer.PLAYER_1]);

        }
        else
        {
            gameActionTexView.setText(String.format(getString(R.string.player_makes_allin)  + "\n", opponentName, bet[Dealer.PLAYER_2]));
            summaryText += String.format(getString(R.string.player_makes_allin)  + "\n", opponentName, bet[Dealer.PLAYER_2]);
        }

        // update labels
        updateLabels();

        // change player turn
        PLAYER_TURN = opponent;

        // opponent still needs to play
        if (money[opponent] > 0)
        {
            if (PLAYER_TURN == Dealer.PLAYER_2)
            {
                call(); // TODO fold_call method
            }
            else
            {
                // show buttons and seek bar
                foldButton.setVisibility(View.VISIBLE);
                CHECK_BUTTON = false;
                callButton.setText(R.string.call_button);
                callButton.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            if (CURRENT_ROUND.equals(PRE_FLOP))
                flop();
            else if (CURRENT_ROUND.equals(FLOP))
                turn();
            else if (CURRENT_ROUND.equals(TURN))
                river();
            else if (CURRENT_ROUND.equals(RIVER))
                showDown();
        }
    }

    /**
     * show opponent cards, hand and ranking
     * show player hand and ranking
     */
    @SuppressLint({"StringFormatMatches", "SetTextI18n"})
    private void showDown()
    {
        // calculate pot
        pot = savedPot + bet[Dealer.PLAYER_1] + bet[Dealer.PLAYER_2];

        // calculate player hand and hand ranking
        HandEvaluator handEvaluator = new HandEvaluator();
        playerHandRanking = handEvaluator.evaluate(playerCards, tableCards);
        String playerHandRankingText = handEvaluator.getHandEvaluationTextByRanking(playerHandRanking);
        playerHand = handEvaluator.getHand();
        String playerHandText = handEvaluator.getHandString();

        // calculate opponent hand and hand ranking
        handEvaluator = new HandEvaluator();
        opponentHandRanking = handEvaluator.evaluate(opponentCards, tableCards);
        String opponentHandRankingText = handEvaluator.getHandEvaluationTextByRanking(opponentHandRanking);
        opponentHand = handEvaluator.getHand();
        String opponentHandText = handEvaluator.getHandString();

        // show opponent cards
        opponentCardsImg.get(0).setImageResource(getResources().getIdentifier(opponentCards.get(0).getCardDrawableName(), "drawable", getPackageName()));
        opponentCardsImg.get(1).setImageResource(getResources().getIdentifier(opponentCards.get(1).getCardDrawableName(), "drawable", getPackageName()));

        // set hand ranking text view as visible
        playerHandRankingTextView.setVisibility(View.VISIBLE);
        opponentHandRankingTextView.setVisibility(View.VISIBLE);

        // set player and opponent hand ranking label
        playerHandRankingTextView.setText(playerHandRankingText + " - " + playerHandText);
        opponentHandRankingTextView.setText(opponentHandRankingText + " - " + opponentHandText);

        // set player and opponent hand on summary
        summaryText += String.format(getString(R.string.player_hand), playerName, playerHandText, playerHandRankingText) + "\n";
        summaryText += String.format(getString(R.string.opponent_hand), opponentName, opponentHandText, opponentHandRankingText) + "\n";
        summaryTextView.setText(summaryText);

        hideButtonsAndSeekBar();

        // calculate hand winner
        calculateWinner();

        // update labels
        updateLabels();

        if (money[Dealer.PLAYER_1] > 0 && money[Dealer.PLAYER_2] > 0)
        {
            new Handler().postDelayed(this::newGame, 5000);
        }
    }

    /**
     * calculate winner
     */
    @SuppressLint({"StringFormatMatches", "SetTextI18n"})
    private void calculateWinner()
    {
        HandWinnerCalculator handWinnerCalculator = new HandWinnerCalculator(playerHand, opponentHand);

        int winner = handWinnerCalculator.calculate(playerHandRanking, opponentHandRanking);

        if (winner == Dealer.PLAYER_1)
        {
            // set summary and game action labels
            gameActionTexView.setText(String.format(getString(R.string.player_wins_pot), playerName, pot));
            summaryText += String.format(getString(R.string.player_wins_pot), playerName, pot) + "\n";
            summaryTextView.setText(summaryText);

            // set odds labels
            playerOddsTextView.setText("100 %");
            opponentOddsTextView.setText("0 %");

            // update money
            money[Dealer.PLAYER_1] += pot;
        }
        else if (winner == Dealer.PLAYER_2)
        {
            // set summary and game action labels
            gameActionTexView.setText(String.format(getString(R.string.player_wins_pot), opponentName, pot));
            summaryText += String.format(getString(R.string.player_wins_pot), opponentName, pot) + "\n";
            summaryTextView.setText(summaryText);

            // set odds labels
            playerOddsTextView.setText("0 %");
            opponentOddsTextView.setText("100 %");

            // update money
            money[Dealer.PLAYER_2] += pot;
        }
        else if (winner == handWinnerCalculator.DRAW)
        {
            // split pot
            pot /= 2;

            // set summary and game action labels
            gameActionTexView.setText(String.format(getString(R.string.draw), pot));
            summaryText += String.format(getString(R.string.draw), pot) + "\n";
            summaryTextView.setText(summaryText);

            // update money
            money[Dealer.PLAYER_1] += pot;
            money[Dealer.PLAYER_2] += pot;
        }
    }

    /**
     * init buttons click listeners
     */
    @SuppressLint("SetTextI18n")
    private void initButtonsAndSeekBarListeners()
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

        betButton.setOnClickListener(v -> {
            hideButtonsAndSeekBar();
            bet();
        });

        // set min value
        betSeekBar.setProgress(BIG_BLIND_VALUE);

        // set max value
        betSeekBar.setMax(Math.round(money[Dealer.PLAYER_1]));

        // Set the amount of poker chips with the seek bar
        inputBetTextView.setText(betSeekBar.getProgress() + " €");

        betSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // set min value
                if (betSeekBar.getProgress() < BIG_BLIND_VALUE)
                    betSeekBar.setProgress(BIG_BLIND_VALUE);

                // min bet is 2 * big blind
                if (betSeekBar.getProgress() > BIG_BLIND_VALUE && betSeekBar.getProgress() < (2 * BIG_BLIND_VALUE))
                    betSeekBar.setProgress(BIG_BLIND_VALUE * 2);

                // set max value
                betSeekBar.setMax(Math.round(money[Dealer.PLAYER_1]));


                // Set the amount of poker chips with the seek bar
                inputBetTextView.setText(betSeekBar.getProgress() + " €");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
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
