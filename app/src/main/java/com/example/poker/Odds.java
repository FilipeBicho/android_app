package com.example.poker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * calculate winning ods on flop, turn and river
 * predict odds on flop, turn and river for poker player simulator
 * @author filipe bicho created 28.03.2020
 */
class Odds {

    //----- private instance variables

    /**
     * player 1 cards
     */
    ArrayList<Card> player1Cards;

    /**
     * player 2 cards
     */
    ArrayList<Card> player2Cards;

    /**
     * deck without players cards
     */
    ArrayList<Card> deck;

    /**
     * Hand evaluator calculator
     */
    private HandEvaluator handEvaluator = new HandEvaluator();

    /**
     * Hand win calculator
     */
    private HandWinCalculator handWinCalculator;

    //----- public constructor

    /**
     *
     * @param player1Cards cards
     * @param player2Cards cards
     * @param deck cards
     */
    Odds (ArrayList<Card> player1Cards, ArrayList<Card> player2Cards, ArrayList<Card> deck)
    {
        this.player1Cards = player1Cards;
        this.player2Cards = player2Cards;
        this.deck = deck;
    }

    //----- private instance methods

    /**
     *
     * @return cards combinations
     */
    private ArrayList<ArrayList<Card>> getCardsCombinations()
    {
        ArrayList<ArrayList<Card>> allTableCardsCombinations = new ArrayList<>();
        ArrayList<Card> cards;
        ArrayList<String> duplicateCardsCombinations = new ArrayList<>();

        for (Card card1 : deck)
        {
            for (Card card2 : deck)
            {
                if (!card2.equals(card1))
                {
                    // add cards into duplication in inverse order
                    duplicateCardsCombinations.add(card2.toString() + "_" + card1.toString());

                    if (!duplicateCardsCombinations.contains(card1.toString() + "_" + card2.toString()))
                    {
                        cards = new ArrayList<>();
                        cards.add(card1);
                        cards.add(card2);

                        allTableCardsCombinations.add(cards);
                    }
                }
            }
        }

        return allTableCardsCombinations;
    }

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
     * @return odds of each player to win the game with the flop cards
     */
    ArrayList<String> flopWinningOdds(ArrayList<Card> tableCards)
    {
        Integer[] winningHandResults = new Integer[3];
        Integer[] handEvaluationResult = new Integer[2];
        ArrayList<Card> player1Hand;
        ArrayList<Card> player2Hand;

        // remove table cards from the deck
        for (Card tableCard : tableCards)
            deck.remove(tableCard);

        // init table turn and river cards
        tableCards.add(null);
        tableCards.add(null);

        // init winning hand results
        Arrays.fill(winningHandResults, 0);

        ArrayList<ArrayList<Card>> cardsCombination = getCardsCombinations();

        for (ArrayList<Card> cards : cardsCombination)
        {
            tableCards.set(3, cards.get(0));
            tableCards.set(4, cards.get(1));

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

        return calculateOdds(winningHandResults, cardsCombination.size());
    }

}
