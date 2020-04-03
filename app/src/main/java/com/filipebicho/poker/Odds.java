package com.filipebicho.poker;

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
    CombinationsCalculator combinationsCalculator;

    //----- public constructor

    /**
     *
     * @param player1Cards cards
     * @param player2Cards cards
     * @param combinationsCalculator CombinationsCalculator
     */
    Odds (ArrayList<Card> player1Cards, ArrayList<Card> player2Cards, CombinationsCalculator combinationsCalculator)
    {
        this.player1Cards = player1Cards;
        this.player2Cards = player2Cards;
        this.combinationsCalculator = combinationsCalculator;
    }

    //----- private instance methods


//    private ArrayList<ArrayList<Card>> getFourCardsCombinations()
//    {
//        ArrayList<ArrayList<Card>> cardsCombinations = new ArrayList<>();
//        ArrayList<Card> currentCards;
//        ArrayList<String> currentStrCards = new ArrayList<>();
//        StringBuilder currentCombination = new StringBuilder();
//
//        ArrayList<String> getStrCardsPermutation = new ArrayList<>();
//        ArrayList<LinkedHashSet> permutations = new ArrayList<>();
//
//        // init current cards combinations
//        for (int i = 0; i < 4; i++)
//            currentStrCards.add("");
//
//        int i = 0;
//        for (Card card1 : deck)
//        {
//            String card1Str = card1.toString();
//            currentStrCards.set(0, card1Str);
//            for (Card card2 : deck)
//            {
//                if (!card2.equals(card1))
//                {
//                    String card2Str = card2.toString();
//                    currentStrCards.set(1, card2Str);
//                    for (Card card3 : deck)
//                    {
//                        if (!card3.equals(card2) && !card3.equals(card1))
//                        {
//                            String card3Str = card3.toString();
//                            currentStrCards.set(2, card3Str);
//                            for (Card card4 : deck)
//                            {
//                                if (!card4.equals(card3) && !card4.equals(card2) && !card4.equals(card1))
//                                {
//
//                                    String card4Str = card4.toString();
//                                    currentStrCards.set(3, card4Str);
//                                    String combinationCardsStr = card1Str + card2Str + card3Str + card4Str;
//                                    if (!hasCombination(permutations, combinationCardsStr))
//                                    {
//                                        permutations.add(getCardsPermutations(currentStrCards));
//                                        currentCards = new ArrayList<>();
//                                        currentCards.add(card1);
//                                        currentCards.add(card2);
//                                        currentCards.add(card3);
//                                        currentCards.add(card4);
//                                        cardsCombinations.add(currentCards);
//
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        return cardsCombinations;
//    }

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
    ArrayList<String> flopWinningOdds(ArrayList<Card> tableCards)
    {
        Integer[] winningHandResults = new Integer[3];
        Integer[] handEvaluationResult = new Integer[2];
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

        ArrayList<ArrayList<Card>> cardsCombinations = combinationsCalculator.getTwoCardsCombinationsWithoutGiveCards(usedCards);

        for (ArrayList<Card> cards : cardsCombinations)
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
    ArrayList<String> turnWinningOdds(ArrayList<Card> tableCards, ArrayList<Card> deck)
    {
        Integer[] winningHandResults = new Integer[3];
        Integer[] handEvaluationResult = new Integer[2];
        ArrayList<Card> player1Hand;
        ArrayList<Card> player2Hand;
        ArrayList<Card> usedCards = new ArrayList<>();

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

//    ArrayList<String> odds(ArrayList<Card> tableCards)
//    {
//        ArrayList<Card> playerHand;
//        ArrayList<Card> opponentCards;
//        ArrayList<Card> opponentHand;
//        Integer[] winningHandResults = new Integer[3];
//        Integer[] handEvaluationResult = new Integer[2];
//        ArrayList<String> flopOdds;
//
//        // remove player 1 cards from the deck
//        for (Card player1Card : player1Cards)
//            deck.remove(player1Card);
//
//
//        // remove table cards from the deck
//        for (Card tableCard : tableCards)
//            deck.remove(tableCard);
//
//        // init winning hand results
//        Arrays.fill(winningHandResults, 0);
//
//        // player hand
//        handEvaluationResult[Dealer.PLAYER_1] = handEvaluator.evaluate(player1Cards, tableCards);
//        playerHand = new ArrayList<>(handEvaluator.getHand());
//
//        ArrayList<ArrayList<Card>> combinations = getFourCardsCombinations();
//
//        for (ArrayList<Card> cards : combinations)
//        {
//            // opponent hand
//            opponentCards = new ArrayList<>(cards);
//            handEvaluationResult[Dealer.PLAYER_2] = handEvaluator.evaluate(opponentCards, tableCards);
//            opponentHand = new ArrayList<>(handEvaluator.getHand());
//
//            // calculate and store winner hand
//            handWinCalculator = new HandWinCalculator(playerHand, opponentHand);
//            winningHandResults[handWinCalculator.calculate(handEvaluationResult[Dealer.PLAYER_1], handEvaluationResult[Dealer.PLAYER_2])]++;
//
//
//        }
//
//        return calculateOdds(winningHandResults, combinations.size());
//    }
}
