package com.filipebicho.poker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * calculate winning ods on flop, turn and river
 * predict odds on flop, turn and river for poker player simulator
 * @author filipe bicho created 28.03.2020
 */
class OddsCalculator {

    //----- private instance variables

    /**
     * player 1 cards
     */
    private ArrayList<Card> player1Cards;

    /**
     * player 2 cards
     */
    private ArrayList<Card> player2Cards;

    /**
     * Hand evaluator calculator
     */
    private HandEvaluator handEvaluator = new HandEvaluator();

    /**
     * Hand win calculator
     */
    private HandWinCalculator handWinCalculator;

    /**
     * combinations calculator
     */
    private CombinationsCalculator combinationsCalculator;

    //----- public constructor

    /**
     *
     * @param player1Cards cards
     * @param player2Cards cards
     * @param combinationsCalculator CombinationsCalculator
     */
    OddsCalculator(ArrayList<Card> player1Cards, ArrayList<Card> player2Cards, CombinationsCalculator combinationsCalculator)
    {
        this.player1Cards = player1Cards;
        this.player2Cards = player2Cards;
        this.combinationsCalculator = combinationsCalculator;
    }

    //----- private instance methods

    /**
     *
     * @param winningHandResults total wins by player
     * @param totalCombinations total calculated combinations number
     * @return odds of each player winning
     */
    private ArrayList<String> calculateOdds(Integer[] winningHandResults, int totalCombinations)
    {
        ArrayList<String> odds = new ArrayList<>();
        DecimalFormat twoDecimalFormat = new DecimalFormat("##.00");

        for (Integer result : winningHandResults)
            odds.add(twoDecimalFormat.format((((float) result / totalCombinations) * 100)));

        return odds;
    }

    //----- public instance methods

    /**
     *
     * @param tableCards table cards
     * @return odds of each player to win the game on flop
     */
    ArrayList<String> playerVsPlayerFlopOdds(ArrayList<Card> tableCards)
    {
        Integer[] winningHandResults = new Integer[3];
        ArrayList<Card> player1Hand;
        ArrayList<Card> player2Hand;
        ArrayList<Card> usedCards = new ArrayList<>();

        usedCards.addAll(player1Cards);
        usedCards.addAll(player2Cards);
        usedCards.addAll(tableCards);

        // init table turn and river cards
        tableCards.add(null);
        tableCards.add(null);

        // init winning hand results
        Arrays.fill(winningHandResults, 0);

        ArrayList<ArrayList<Card>> cardsCombinations = combinationsCalculator.getTwoCardsCombinationsWithoutGivenCards(usedCards);

        for (ArrayList<Card> cards : cardsCombinations)
        {
            tableCards.set(3, cards.get(0));
            tableCards.set(4, cards.get(1));

            // evaluate player 1 hand
            int player1HandResult = handEvaluator.evaluate(player1Cards, tableCards);
            player1Hand = new ArrayList<>(handEvaluator.getHand());

            // evaluate player 2 hand
            int player2HandResult = handEvaluator.evaluate(player2Cards, tableCards);
            player2Hand = new ArrayList<>(handEvaluator.getHand());

            // calculate and store winner hand
            handWinCalculator = new HandWinCalculator(player1Hand, player2Hand);
            winningHandResults[handWinCalculator.calculate(player1HandResult, player2HandResult)]++;
        }

        // remove added cards to calculate odds
        tableCards.remove(3);
        tableCards.remove(3);

        return calculateOdds(winningHandResults, cardsCombinations.size());
    }

    /**
     *
     * @param tableCards table cards
     * @return odds of each player to win the game on turn
     */
    ArrayList<String> playerVsPlayerTurnOdds(ArrayList<Card> tableCards, ArrayList<Card> deck)
    {
        Integer[] winningHandResults = new Integer[3];
        Integer[] handEvaluationResult = new Integer[2];
        ArrayList<Card> player1Hand;
        ArrayList<Card> player2Hand;

        // init table river card
        tableCards.add(null);

        // init winning hand results
        Arrays.fill(winningHandResults, 0);

        for (Card card : deck)
        {
            tableCards.set(4, card);

            // evaluate player 1 hand
            handEvaluationResult[Dealer.PLAYER_1] = handEvaluator.evaluate(player1Cards, tableCards);
            player1Hand = new ArrayList<>(handEvaluator.getHand());

            // evaluate player 2 hand
            handEvaluationResult[Dealer.PLAYER_2] = handEvaluator.evaluate(player2Cards, tableCards);
            player2Hand = new ArrayList<>(handEvaluator.getHand());

            // calculate and store winner hand
            handWinCalculator = new HandWinCalculator(player1Hand, player2Hand);
            winningHandResults[handWinCalculator.calculate(handEvaluationResult[Dealer.PLAYER_1], handEvaluationResult[Dealer.PLAYER_2])]++;
        }

        // remove added card to calculate odds
        tableCards.remove(4);

        return calculateOdds(winningHandResults, deck.size());
    }

    /**
     *
     * @param tableCards Arraylist containing flop
     * @param combinationsLimit limit of returned combinations cards
     * @return odds knowing flop and player cards
     */
    ArrayList<String> flopOdds(ArrayList<Card> tableCards, int combinationsLimit)
    {
        ArrayList<Card> playerHand;
        ArrayList<Card> opponentCards = new ArrayList<>();
        ArrayList<Card> opponentHand;
        Integer[] winningHandResults = new Integer[3];
        Integer[] handEvaluationResult = new Integer[2];

        // init winning hand results
        Arrays.fill(winningHandResults, 0);

        ArrayList<Card> usedCards = new ArrayList<>();
        usedCards.addAll(player1Cards);
        usedCards.addAll(tableCards);

        ArrayList<ArrayList<Card>> combinations = combinationsCalculator.getFourCardsCombinationsWithoutGivenCards(usedCards, combinationsLimit);

        opponentCards.add(null);
        opponentCards.add(null);
        tableCards.add(null);
        tableCards.add(null);

        ArrayList<Integer> randomIndices = new ArrayList<>(Arrays.asList(0,1,2,3));
        Collections.shuffle(randomIndices);

        for (ArrayList<Card> cards : combinations)
        {
            // opponent cards
            opponentCards.set(0, cards.get(randomIndices.get(0)));
            opponentCards.set(1, cards.get(randomIndices.get(1)));

            // table cards (turn & river)
            tableCards.set(3, cards.get(randomIndices.get(2)));
            tableCards.set(4, cards.get(randomIndices.get(3)));

            // player hand
            handEvaluationResult[Dealer.PLAYER_1] = handEvaluator.evaluate(player1Cards, tableCards);
            playerHand = new ArrayList<>(handEvaluator.getHand());

            // opponent hand
            handEvaluationResult[Dealer.PLAYER_2] = handEvaluator.evaluate(opponentCards, tableCards);
            opponentHand = new ArrayList<>(handEvaluator.getHand());

            // calculate and store winner hand
            handWinCalculator = new HandWinCalculator(playerHand, opponentHand);
            winningHandResults[handWinCalculator.calculate(handEvaluationResult[Dealer.PLAYER_1], handEvaluationResult[Dealer.PLAYER_2])]++;
        }

        tableCards.remove(3);
        tableCards.remove(3);

        return calculateOdds(winningHandResults, combinations.size());
    }

    /**
     *
     * @param tableCards Arraylist containing flop & turn
     * @param combinationsLimit limit of returned combinations cards
     * @return odds knowing flop, turn and player cards
     */
    ArrayList<String> turnOdds(ArrayList<Card> tableCards, int combinationsLimit)
    {
        ArrayList<Card> playerHand;
        ArrayList<Card> opponentCards = new ArrayList<>();
        ArrayList<Card> opponentHand;
        Integer[] winningHandResults = new Integer[3];
        Integer[] handEvaluationResult = new Integer[2];

        // init winning hand results
        Arrays.fill(winningHandResults, 0);

        ArrayList<Card> usedCards = new ArrayList<>();
        usedCards.addAll(player1Cards);
        usedCards.addAll(tableCards);

        ArrayList<ArrayList<Card>> combinations = combinationsCalculator.getThreeCardsCombinationsWithoutGivenCards(usedCards, combinationsLimit);

        opponentCards.add(null);
        opponentCards.add(null);
        tableCards.add(null);

        ArrayList<Integer> randomIndices = new ArrayList<>(Arrays.asList(0,1,2));
        Collections.shuffle(randomIndices);

        for (ArrayList<Card> cards : combinations)
        {
            // opponent cards
            opponentCards.set(0, cards.get(randomIndices.get(0)));
            opponentCards.set(1, cards.get(randomIndices.get(1)));

            // table card (river)
            tableCards.set(4, cards.get(randomIndices.get(2)));

            // player hand
            handEvaluationResult[Dealer.PLAYER_1] = handEvaluator.evaluate(player1Cards, tableCards);
            playerHand = new ArrayList<>(handEvaluator.getHand());

            // opponent hand
            handEvaluationResult[Dealer.PLAYER_2] = handEvaluator.evaluate(opponentCards, tableCards);
            opponentHand = new ArrayList<>(handEvaluator.getHand());

            // calculate and store winner hand
            handWinCalculator = new HandWinCalculator(playerHand, opponentHand);
            winningHandResults[handWinCalculator.calculate(handEvaluationResult[Dealer.PLAYER_1], handEvaluationResult[Dealer.PLAYER_2])]++;
        }

        tableCards.remove(4);

        return calculateOdds(winningHandResults, combinations.size());
    }

    /**
     *
     * @param tableCards Arraylist containing flop, turn & river
     * @return odds knowing flop, turn and player cards
     */
    ArrayList<String> riverOdds(ArrayList<Card> tableCards)
    {
        ArrayList<Card> playerHand;
        ArrayList<Card> opponentCards = new ArrayList<>();
        ArrayList<Card> opponentHand;
        Integer[] winningHandResults = new Integer[3];
        Integer[] handEvaluationResult = new Integer[2];

        // init winning hand results
        Arrays.fill(winningHandResults, 0);

        ArrayList<Card> usedCards = new ArrayList<>();
        usedCards.addAll(player1Cards);
        usedCards.addAll(tableCards);

        ArrayList<ArrayList<Card>> combinations = combinationsCalculator.getTwoCardsCombinationsWithoutGivenCards(usedCards);

        opponentCards.add(null);
        opponentCards.add(null);

        ArrayList<Integer> randomIndices = new ArrayList<>(Arrays.asList(0,1));
        Collections.shuffle(randomIndices);

        for (ArrayList<Card> cards : combinations)
        {
            // opponent cards
            opponentCards.set(0, cards.get(randomIndices.get(0)));
            opponentCards.set(1, cards.get(randomIndices.get(1)));

            // player hand
            handEvaluationResult[Dealer.PLAYER_1] = handEvaluator.evaluate(player1Cards, tableCards);
            playerHand = new ArrayList<>(handEvaluator.getHand());

            // opponent hand
            handEvaluationResult[Dealer.PLAYER_2] = handEvaluator.evaluate(opponentCards, tableCards);
            opponentHand = new ArrayList<>(handEvaluator.getHand());

            // calculate and store winner hand
            handWinCalculator = new HandWinCalculator(playerHand, opponentHand);
            winningHandResults[handWinCalculator.calculate(handEvaluationResult[Dealer.PLAYER_1], handEvaluationResult[Dealer.PLAYER_2])]++;
        }

        return calculateOdds(winningHandResults, combinations.size());
    }
}
