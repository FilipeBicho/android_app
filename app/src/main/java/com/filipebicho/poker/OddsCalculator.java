package com.filipebicho.poker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

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

    /**
     * three cards permutations indexes
     */
    private Integer[][] threeCardsPermutationsIndexes = {
            {0,1,2},{0,2,1},{1,2,0}
    };

    /**
     * four cards permutations indexes
     */
    private Integer[][] fourCardsPermutationsIndexes = {
            {0,1,2,3}, {0,2,1,3}, {0,3,1,2},
            {1,2,0,3}, {1,3,0,2},
            {2,3,0,1},
    };

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
    private ArrayList<Float> calculateOdds(Integer[] winningHandResults, int totalCombinations)
    {
        ArrayList<Float> odds = new ArrayList<>();
        DecimalFormat twoDecimalFormat = new DecimalFormat("##.00");

        for (Integer result : winningHandResults)
            odds.add(Float.parseFloat(twoDecimalFormat.format((((float )result / totalCombinations) * 100))));

        return odds;
    }

    //----- public instance methods

    /**
     *
     * @param card Card
     * @return hardcoded pre flop odds
     */
    public double preFlopOdds(ArrayList<Card> card)
    {
        /* A A */
        if(card.get(0).getRank()==0 && card.get(1).getRank()==0)
            return 85.3;

        /* A K suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==12 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==12 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 67;

        /* A K out suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==12 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==12 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 65.4;

        /* A Q suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==11 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==11 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 66.1;

        /* A Q out suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==11 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==11 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 66.1;

        /* A J suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==10 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==10 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 65.4;

        /* A J out suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==10 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==10 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 63.6;


        /* A 10 suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==9 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==9 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 64.7;

        /* A 10 out suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==9 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==9 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 62.9;

        /* A 9 suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==8 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==8 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 63;

        /* A 9 out suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==8 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==8 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 60.9;

        /* A 8 suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==7 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==7 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 62.1;

        /* A 8 out suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==7 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==7 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 60.1;

        /* A 7 suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==6 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==6 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 61.1;

        /* A 7 out suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==6 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==6 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 59.1;

        /* A 6 suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==5 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==5 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 60;

        /* A 6 out suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==5 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==5 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 57.8;

        /* A 5 suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==4 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==4 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 59.9;

        /* A 5 out suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==4 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==4 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 57.7;

        /* A 4 suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 58.9;

        /* A 4 out suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 56.4;

        /* A 3 suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 58;

        /* A 3 out suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 55.6;

        /* A 2 suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 57;

        /* A 2 out suit */
        if((card.get(0).getRank()==0 && card.get(1).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==0 && card.get(0).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 54.6;

        /*--------------------------------------------------------------------------------------------*/

        /* K K */
        if(card.get(0).getRank()==12 && card.get(1).getRank()==12)
            return 82.4;

        /* K Q suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==11 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==11 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 63.4;

        /* K Q out suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==11 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==11 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 61.4;

        /* K J suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==10 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==10 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 62.6;

        /* K J out suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==10 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==10 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 60.6;

        /* K 10 suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==9 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==9 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 61.9;

        /* K 10 out suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==9 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==9 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 59.9;

        /* K 9 suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==8 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==8 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 60;

        /* K 9 out suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==8 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==8 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 58;

        /* K 8 suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==7 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==7 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 58.5;

        /* K 8 out suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==7 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==7 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 56.3;

        /* K 7 suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==6 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==6 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 57.8;

        /* K 7 out suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==6 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==6 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 55.4;

        /* K 6 suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==5 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==5 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 56.8;

        /* K 6 out suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==5 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==5 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 54.3;

        /* K 5 suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==4 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==4 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 55.8;

        /* K 5 out suit */
        if((card.get(0).getRank()==12&& card.get(1).getRank()==4 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==4 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 53.3;

        /* K 4 suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 54.7;

        /* K 4 out suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 52.1;

        /* K 3 suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 53.8;

        /* K 3 out suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 51.2;

        /* K 2 suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 52.9;

        /* K 2 out suit */
        if((card.get(0).getRank()==12 && card.get(1).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==12 && card.get(0).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 50.2;

        /*-----------------------------------------------------------------------------------------*/

        /* Q Q */
        if(card.get(0).getRank()==11 && card.get(1).getRank()==11)
            return 79.9;

        /* Q J suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==10 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==10 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 60.3;

        /* Q J out suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==10 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==10 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 58.2;

        /* Q 10 suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==9 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==9 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 59.5;

        /* Q 10 out suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==9 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==9 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 57.4;

        /* Q 9 suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==8 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==8 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 57.9;

        /* Q 9 out suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==8 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==8 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 55.5;

        /* Q 8 suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==7 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==7 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 56.2;

        /* Q 8 out suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==7 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==7 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 53.8;

        /* Q 7 suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==6 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==6 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 54.5;

        /* Q 7 out suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==6 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==6 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 51.9;

        /* Q 6 suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==5 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==5 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 53.8;

        /* Q 6 out suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==5 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==5 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 51.1;

        /* Q 5 suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==4 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==4 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 52.9;

        /* Q 5 out suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==4 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==4 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 50.2;

        /* Q 4 suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 51.7;

        /* Q 4 out suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 49;

        /* Q 3 suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 50.7;

        /* Q 3 out suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 47.9;

        /* Q 2 suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 49.9;

        /* Q 2 out suit */
        if((card.get(0).getRank()==11 && card.get(1).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==11 && card.get(0).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 47;

        /*-------------------------------------------------------------------------------------*/

        /* J J */
        if(card.get(0).getRank()==10 && card.get(1).getRank()==10)
            return 77.5;

        /* J 10 suit */
        if((card.get(0).getRank()==10 && card.get(1).getRank()==9 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==10 && card.get(0).getRank()==9 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 57.5;

        /* J 10 out suit */
        if((card.get(0).getRank()==10 && card.get(1).getRank()==9 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==10 && card.get(0).getRank()==9 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 55.4;

        /* J 9 suit */
        if((card.get(0).getRank()==10 && card.get(1).getRank()==8 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==10 && card.get(0).getRank()==8 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 55.8;

        /* J 9 out suit */
        if((card.get(0).getRank()==10 && card.get(1).getRank()==8 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==10 && card.get(0).getRank()==8 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 53.4;

        /* J 8 suit */
        if((card.get(0).getRank()==10 && card.get(1).getRank()==7 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==10 && card.get(0).getRank()==7 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 54.2;

        /* J 8 out suit */
        if((card.get(0).getRank()==10 && card.get(1).getRank()==7 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==10 && card.get(0).getRank()==7 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 51.7;

        /* J 7 suit */
        if((card.get(0).getRank()==10 && card.get(1).getRank()==6 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==10 && card.get(0).getRank()==6 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 52.4;

        /* J 7 out suit */
        if((card.get(0).getRank()==10 && card.get(1).getRank()==6 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==10 && card.get(0).getRank()==6 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 49.9;

        /* J 6 suit */
        if((card.get(0).getRank()==10 && card.get(1).getRank()==5 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==10 && card.get(0).getRank()==5 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 50.8;

        /* J 6 out suit */
        if((card.get(0).getRank()==10 && card.get(1).getRank()==5 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==10 && card.get(0).getRank()==5 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 47.9;

        /* J 5 suit */
        if((card.get(0).getRank()==10 && card.get(1).getRank()==4 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==10 && card.get(0).getRank()==4 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 50;

        /* J 5 out suit */
        if((card.get(0).getRank()==10 && card.get(1).getRank()==4 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==10 && card.get(0).getRank()==4 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 47.1;

        /* J 4 suit */
        if((card.get(0).getRank()==10 && card.get(1).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==10 && card.get(0).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 49;

        /* J 4 out suit */
        if((card.get(0).getRank()==10 && card.get(1).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==10 && card.get(0).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 46.1;

        /* J 3 suit */
        if((card.get(0).getRank()==10 && card.get(1).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==10 && card.get(0).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 47.9;

        /* J 3 out suit */
        if((card.get(0).getRank()==10 && card.get(1).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==10 && card.get(0).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 45;

        /* J 2 suit */
        if((card.get(0).getRank()==10 && card.get(1).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==10 && card.get(0).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 47.1;

        /* J 2 out suit */
        if((card.get(0).getRank()==10 && card.get(1).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==10 && card.get(0).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 44;

        /*-------------------------------------------------------------------------------------*/

        /* 10 10 */
        if(card.get(0).getRank()==9 && card.get(1).getRank()==9)
            return 75.1;

        /* 10 9 suit */
        if((card.get(0).getRank()==9 && card.get(1).getRank()==8 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==9 && card.get(0).getRank()==8 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 54.3;

        /* 10 9 out suit */
        if((card.get(0).getRank()==9 && card.get(1).getRank()==8 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==9 && card.get(0).getRank()==8 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 51.7;

        /* 10 8 suit */
        if((card.get(0).getRank()==9 && card.get(1).getRank()==7 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==9 && card.get(0).getRank()==7 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 52.6;

        /* 10 8 out suit */
        if((card.get(0).getRank()==9 && card.get(1).getRank()==7 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==9 && card.get(0).getRank()==7 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 50;

        /* 10 7 suit */
        if((card.get(0).getRank()==9 && card.get(1).getRank()==6 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==9 && card.get(0).getRank()==6 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 51;

        /* 10 7 out suit */
        if((card.get(0).getRank()==9 && card.get(1).getRank()==6 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==9 && card.get(0).getRank()==6 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 48.2;

        /* 10 6 suit */
        if((card.get(0).getRank()==9 && card.get(1).getRank()==5 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==9 && card.get(0).getRank()==5 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 49.2;

        /* 10 6 out suit */
        if((card.get(0).getRank()==9 && card.get(1).getRank()==5 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==9 && card.get(0).getRank()==5 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 46.3;

        /* 10 5 suit */
        if((card.get(0).getRank()==9 && card.get(1).getRank()==4 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==9 && card.get(0).getRank()==4 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 47.2;

        /* 10 5 out suit */
        if((card.get(0).getRank()==9 && card.get(1).getRank()==4 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==9 && card.get(0).getRank()==4 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 44.2;

        /* 10 4 suit */
        if((card.get(0).getRank()==9 && card.get(1).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==9 && card.get(0).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 46.4;

        /* 10 4 out suit */
        if((card.get(0).getRank()==9 && card.get(1).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==9 && card.get(0).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 43.4;

        /* 10 3 suit */
        if((card.get(0).getRank()==9 && card.get(1).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==9 && card.get(0).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 45.5;

        /* 10 3 out suit */
        if((card.get(0).getRank()==9 && card.get(1).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==9 && card.get(0).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 42.4;

        /* 10 2 suit */
        if((card.get(0).getRank()==9 && card.get(1).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==9 && card.get(0).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 44.7;

        /* 10 2 out suit */
        if((card.get(0).getRank()==9 && card.get(1).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==9 && card.get(0).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 41.5;

        /*-----------------------------------------------------------------------------------*/

        /* 9 9 */
        if(card.get(0).getRank()==8 && card.get(1).getRank()==8)
            return 72.1;

        /* 9 8 suit */
        if((card.get(0).getRank()==8 && card.get(1).getRank()==7 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==8 && card.get(0).getRank()==7 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 51.1;

        /* 9 8 out suit */
        if((card.get(0).getRank()==8 && card.get(1).getRank()==7 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==8 && card.get(0).getRank()==7 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 48.4;

        /* 9 7 suit */
        if((card.get(0).getRank()==8 && card.get(1).getRank()==6 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==8 && card.get(0).getRank()==6 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 49.5;

        /* 9 7 out suit */
        if((card.get(0).getRank()==8 && card.get(1).getRank()==6 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==8 && card.get(0).getRank()==6 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 46.7;

        /* 9 6 suit */
        if((card.get(0).getRank()==8 && card.get(1).getRank()==5 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==8 && card.get(0).getRank()==5 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 47.7;

        /* 9 6 out suit */
        if((card.get(0).getRank()==8 && card.get(1).getRank()==5 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==8 && card.get(0).getRank()==5 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 44.9;

        /* 9 5 suit */
        if((card.get(0).getRank()==8 && card.get(1).getRank()==4 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==8 && card.get(0).getRank()==4 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 45.9;

        /* 9 5 out suit */
        if((card.get(0).getRank()==8 && card.get(1).getRank()==4 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==8 && card.get(0).getRank()==4 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 42.9;

        /* 9 4 suit */
        if((card.get(0).getRank()==8 && card.get(1).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==8 && card.get(0).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 43.8;

        /* 9 4 out suit */
        if((card.get(0).getRank()==8 && card.get(1).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==8 && card.get(0).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 40.7;

        /* 9 3 suit */
        if((card.get(0).getRank()==8 && card.get(1).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==8 && card.get(0).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 43.2;

        /* 9 3 out suit */
        if((card.get(0).getRank()==8 && card.get(1).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==8 && card.get(0).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 39.9;

        /* 9 2 suit */
        if((card.get(0).getRank()==8 && card.get(1).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==8 && card.get(0).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 42.3;

        /* 9 2 out suit */
        if((card.get(0).getRank()==8 && card.get(1).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==8 && card.get(0).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 38.9;

        /*-------------------------------------------------------------------------------------*/

        /* 8 8 */
        if(card.get(0).getRank()==7 && card.get(1).getRank()==7)
            return 69.1;

        /* 8 7 suit */
        if((card.get(0).getRank()==7 && card.get(1).getRank()==6 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==7 && card.get(0).getRank()==6 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 48.2;

        /* 8 7 out suit */
        if((card.get(0).getRank()==7 && card.get(1).getRank()==6 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==7 && card.get(0).getRank()==6 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 45.5;

        /* 8 6 suit */
        if((card.get(0).getRank()==7 && card.get(1).getRank()==5 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==7 && card.get(0).getRank()==5 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 46.5;

        /* 8 6 out suit */
        if((card.get(0).getRank()==7 && card.get(1).getRank()==5 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==7 && card.get(0).getRank()==5 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 43.6;

        /* 8 5 suit */
        if((card.get(0).getRank()==7 && card.get(1).getRank()==4 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==7 && card.get(0).getRank()==4 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 44.8;

        /* 8 5 out suit */
        if((card.get(0).getRank()==7 && card.get(1).getRank()==4 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==7 && card.get(0).getRank()==4 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 41.7;

        /* 8 4 suit */
        if((card.get(0).getRank()==7 && card.get(1).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==7 && card.get(0).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 42.7;

        /* 8 4 out suit */
        if((card.get(0).getRank()==7 && card.get(1).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==7 && card.get(0).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 39.6;

        /* 8 3 suit */
        if((card.get(0).getRank()==7 && card.get(1).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==7 && card.get(0).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 40.8;

        /* 8 3 out suit */
        if((card.get(0).getRank()==7 && card.get(1).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==7 && card.get(0).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 37.5;

        /* 8 2 suit */
        if((card.get(0).getRank()==7 && card.get(1).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==7 && card.get(0).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 40.3;

        /* 8 2 out suit */
        if((card.get(0).getRank()==7 && card.get(1).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==7 && card.get(0).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 36.8;

        /*-------------------------------------------------------------------------------------*/

        /* 7 7 */
        if(card.get(0).getRank()==6 && card.get(1).getRank()==6)
            return 66.2;

        /* 7 6 suit */
        if((card.get(0).getRank()==6 && card.get(1).getRank()==5 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==6 && card.get(0).getRank()==5 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 45.7;

        /* 7 6 out suit */
        if((card.get(0).getRank()==6 && card.get(1).getRank()==5 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==6 && card.get(0).getRank()==5 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 42.7;

        /* 7 5 suit */
        if((card.get(0).getRank()==6 && card.get(1).getRank()==4 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==6 && card.get(0).getRank()==4 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 43.8;

        /* 7 5 out suit */
        if((card.get(0).getRank()==6 && card.get(1).getRank()==4 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==6 && card.get(0).getRank()==4 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 40.8;

        /* 7 4 suit */
        if((card.get(0).getRank()==6 && card.get(1).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==6 && card.get(0).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 41.8;

        /* 7 4 out suit */
        if((card.get(0).getRank()==6 && card.get(1).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==6 && card.get(0).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 38.6;

        /* 7 3 suit */
        if((card.get(0).getRank()==6 && card.get(1).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==6 && card.get(0).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 40;

        /* 7 3 out suit */
        if((card.get(0).getRank()==6 && card.get(1).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==6 && card.get(0).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 36.6;

        /* 7 2 suit */
        if((card.get(0).getRank()==6 && card.get(1).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==6 && card.get(0).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 38.1;

        /* 7 2 out suit */
        if((card.get(0).getRank()==6 && card.get(1).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==6 && card.get(0).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 34.6;

        /*-------------------------------------------------------------------------------------*/

        /* 6 6 */
        if(card.get(0).getRank()==5 && card.get(1).getRank()==5)
            return 63.3;

        /* 6 5 suit */
        if((card.get(0).getRank()==5 && card.get(1).getRank()==4 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==5 && card.get(0).getRank()==4 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 43.2;

        /* 6 5 out suit */
        if((card.get(0).getRank()==5 && card.get(1).getRank()==4 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==5 && card.get(0).getRank()==4 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 40.1;

        /* 6 4 suit */
        if((card.get(0).getRank()==5 && card.get(1).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==5 && card.get(0).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 41.4;

        /* 6 4 out suit */
        if((card.get(0).getRank()==5 && card.get(1).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==5 && card.get(0).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 38;

        /* 6 3 suit */
        if((card.get(0).getRank()==5 && card.get(1).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==5 && card.get(0).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 39.4;

        /* 6 3 out suit */
        if((card.get(0).getRank()==5 && card.get(1).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==5 && card.get(0).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 35.9;

        /* 6 2 suit */
        if((card.get(0).getRank()==5 && card.get(1).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==5 && card.get(0).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 37.5;

        /* 6 2 out suit */
        if((card.get(0).getRank()==5 && card.get(1).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==5 && card.get(0).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 34;

        /*-------------------------------------------------------------------------------------*/

        /* 5 5 */
        if(card.get(0).getRank()==4 && card.get(1).getRank()==4)
            return 60.3;

        /* 5 4 suit */
        if((card.get(0).getRank()==4 && card.get(1).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==4 && card.get(0).getRank()==3 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 41.1;

        /* 5 4 out suit */
        if((card.get(0).getRank()==4 && card.get(1).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==4 && card.get(0).getRank()==3 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 37.9;

        /* 5 3 suit */
        if((card.get(0).getRank()==4 && card.get(1).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==4 && card.get(0).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 39.3;

        /* 5 3 out suit */
        if((card.get(0).getRank()==4 && card.get(1).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==4 && card.get(0).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 35.8;

        /* 5 2 suit */
        if((card.get(0).getRank()==4 && card.get(1).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==4 && card.get(0).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 37.5;

        /* 5 2 out suit */
        if((card.get(0).getRank()==4 && card.get(1).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==4 && card.get(0).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 33.9;

        /*-------------------------------------------------------------------------------------*/

        /* 4 4 */
        if(card.get(0).getRank()==3 && card.get(1).getRank()==3)
            return 57;

        /* 4 3 suit */
        if((card.get(0).getRank()==3 && card.get(1).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==3 && card.get(0).getRank()==2 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 38;

        /* 4 3 out suit */
        if((card.get(0).getRank()==3 && card.get(1).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==3 && card.get(0).getRank()==2 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 34.4;

        /* 4 2 suit */
        if((card.get(0).getRank()==3 && card.get(1).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==3 && card.get(0).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 36.3;

        /* 4 2 out suit */
        if((card.get(0).getRank()==3 && card.get(1).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==3 && card.get(0).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 32.5;

        /*-------------------------------------------------------------------------------------*/

        /* 3 3 */
        if(card.get(0).getRank()==2 && card.get(1).getRank()==2)
            return 53.7;

        /* 3 2 suit */
        if((card.get(0).getRank()==2 && card.get(1).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()) ||
                (card.get(1).getRank()==2 && card.get(0).getRank()==1 && card.get(0).getSuit() == card.get(1).getSuit()))
            return 35.1;

        /* 3 2 out suit */
        if((card.get(0).getRank()==2 && card.get(1).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()) ||
                (card.get(1).getRank()==2 && card.get(0).getRank()==1 && card.get(0).getSuit() != card.get(1).getSuit()))
            return 31.2;

        /*-------------------------------------------------------------------------------------*/

        /* 2 2 */
        if(card.get(0).getRank()==1 && card.get(1).getRank()==1)
            return 50.3;

        return 0;
    }

    /**
     *
     * @param tableCards table cards
     * @return odds of each player to win the game on flop
     */
    ArrayList<Float> playerVsPlayerFlopOdds(ArrayList<Card> tableCards)
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

        ArrayList<ArrayList<Card>> cardsCombinations = combinationsCalculator.getTwoCardsCombinations(CombinationsCalculator.TWO_CARDS_COMBINATIONS_TOTAL);

        int count = 0;
        for (ArrayList<Card> combination : cardsCombinations)
        {

            if (combinationsCalculator.combinationContainsUsedCard(usedCards, combination))
                continue;

            tableCards.set(3, combination.get(0));
            tableCards.set(4, combination.get(1));

            // evaluate player 1 hand
            int player1HandResult = handEvaluator.evaluate(player1Cards, tableCards);
            player1Hand = new ArrayList<>(handEvaluator.getHand());

            // evaluate player 2 hand
            int player2HandResult = handEvaluator.evaluate(player2Cards, tableCards);
            player2Hand = new ArrayList<>(handEvaluator.getHand());

            // calculate and store winner hand
            handWinCalculator = new HandWinCalculator(player1Hand, player2Hand);
            winningHandResults[handWinCalculator.calculate(player1HandResult, player2HandResult)]++;
            count++;
        }

        // remove added cards to calculate odds
        tableCards.remove(3);
        tableCards.remove(3);

        return calculateOdds(winningHandResults, count);
    }

    /**
     *
     * @param tableCards table cards
     * @return odds of each player to win the game on turn
     */
    ArrayList<Float> playerVsPlayerTurnOdds(ArrayList<Card> tableCards, ArrayList<Card> deck)
    {
        Integer[] winningHandResults = new Integer[3];
        Integer[] handEvaluationResult = new Integer[2];
        ArrayList<Card> player1Hand;
        ArrayList<Card> player2Hand;

        // init table river card
        tableCards.add(null);

        // init winning hand results
        Arrays.fill(winningHandResults, 0);

        int count = 0;
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
            count++;
        }

        // remove added card to calculate odds
        tableCards.remove(4);

        return calculateOdds(winningHandResults, count);
    }

    /**
     *
     * @param tableCards Arraylist containing flop
     * @param combinationsLimit limit of returned combinations cards
     * @return odds knowing flop and player cards
     */
    ArrayList<Float> flopOdds(ArrayList<Card> tableCards, int combinationsLimit)
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

        ArrayList<ArrayList<Card>> combinations = combinationsCalculator.getFourCardsCombinations(combinationsLimit);

        opponentCards.add(null);
        opponentCards.add(null);
        tableCards.add(null);
        tableCards.add(null);

        int count = 0;
        for (ArrayList<Card> combination : combinations)
        {

            if (combinationsCalculator.combinationContainsUsedCard(usedCards, combination))
                continue;

            for (Integer[] permutation : fourCardsPermutationsIndexes)
            {

                // opponent cards
                opponentCards.set(0, combination.get(permutation[0]));
                opponentCards.set(1, combination.get(permutation[1]));

                // table cards (turn & river)
                tableCards.set(3, combination.get(permutation[2]));
                tableCards.set(4, combination.get(permutation[3]));

                // player hand
                handEvaluationResult[Dealer.PLAYER_1] = handEvaluator.evaluate(player1Cards, tableCards);
                playerHand = new ArrayList<>(handEvaluator.getHand());

                // opponent hand
                handEvaluationResult[Dealer.PLAYER_2] = handEvaluator.evaluate(opponentCards, tableCards);
                opponentHand = new ArrayList<>(handEvaluator.getHand());

                // calculate and store winner hand
                handWinCalculator = new HandWinCalculator(playerHand, opponentHand);
                winningHandResults[handWinCalculator.calculate(handEvaluationResult[Dealer.PLAYER_1], handEvaluationResult[Dealer.PLAYER_2])]++;
                count++;
            }
        }

        tableCards.remove(3);
        tableCards.remove(3);

        return calculateOdds(winningHandResults, count);
    }

    /**
     *
     * @param tableCards Arraylist containing flop & turn
     * @param combinationsLimit limit of returned combinations cards
     * @return odds knowing flop, turn and player cards
     */
    ArrayList<Float> turnOdds(ArrayList<Card> tableCards, int combinationsLimit)
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

        ArrayList<ArrayList<Card>> combinations = combinationsCalculator.getThreeCardsCombinations(combinationsLimit);

        opponentCards.add(null);
        opponentCards.add(null);
        tableCards.add(null);

        int count = 0;
        for (ArrayList<Card> combination : combinations)
        {
            if (combinationsCalculator.combinationContainsUsedCard(usedCards, combination))
                continue;

            for (Integer[] permutation : threeCardsPermutationsIndexes)
            {
                // opponent cards
                opponentCards.set(0, combination.get(permutation[0]));
                opponentCards.set(1, combination.get(permutation[1]));

                // table card (river)
                tableCards.set(4, combination.get(permutation[2]));

                // player hand
                handEvaluationResult[Dealer.PLAYER_1] = handEvaluator.evaluate(player1Cards, tableCards);
                playerHand = new ArrayList<>(handEvaluator.getHand());

                // opponent hand
                handEvaluationResult[Dealer.PLAYER_2] = handEvaluator.evaluate(opponentCards, tableCards);
                opponentHand = new ArrayList<>(handEvaluator.getHand());

                // calculate and store winner hand
                handWinCalculator = new HandWinCalculator(playerHand, opponentHand);
                winningHandResults[handWinCalculator.calculate(handEvaluationResult[Dealer.PLAYER_1], handEvaluationResult[Dealer.PLAYER_2])]++;
                count++;
            }

        }

        tableCards.remove(4);

        return calculateOdds(winningHandResults, count);
    }

    /**
     *
     * @param tableCards Arraylist containing flop, turn & river
     * @param combinationsLimit limit of returned combinations cards
     * @return odds knowing flop, turn and player cards
     */
    ArrayList<Float> riverOdds(ArrayList<Card> tableCards, int combinationsLimit)
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

        ArrayList<ArrayList<Card>> combinations = combinationsCalculator.getTwoCardsCombinations(combinationsLimit);

        opponentCards.add(null);
        opponentCards.add(null);

        int count = 0;
        for (ArrayList<Card> combination : combinations)
        {

            if (combinationsCalculator.combinationContainsUsedCard(usedCards, combination))
                continue;

            // opponent cards
            opponentCards.set(0, combination.get(0));
            opponentCards.set(1, combination.get(1));

            // player hand
            handEvaluationResult[Dealer.PLAYER_1] = handEvaluator.evaluate(player1Cards, tableCards);
            playerHand = new ArrayList<>(handEvaluator.getHand());

            // opponent hand
            handEvaluationResult[Dealer.PLAYER_2] = handEvaluator.evaluate(opponentCards, tableCards);
            opponentHand = new ArrayList<>(handEvaluator.getHand());

            // calculate and store winner hand
            handWinCalculator = new HandWinCalculator(playerHand, opponentHand);
            winningHandResults[handWinCalculator.calculate(handEvaluationResult[Dealer.PLAYER_1], handEvaluationResult[Dealer.PLAYER_2])]++;
            count++;
        }

        return calculateOdds(winningHandResults, count);
    }
}
