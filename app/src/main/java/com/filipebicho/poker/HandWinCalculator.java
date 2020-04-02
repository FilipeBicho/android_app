package com.filipebicho.poker;

import java.util.ArrayList;

/**
 * calculate winning hand
 * @author filipe bicho created 27.03.2020
 */
class HandWinCalculator {

    //----- static const variables
    
    static final int DRAW = 2;

    //----- private instance variables

    /**
     * player 1 hand
     */
    private ArrayList<Card> player1Hand;

    /**
     * player 2 hand
     */
    private ArrayList<Card> player2Hand;

    //----- public constructor

    /**
     *
     * @param player1Hand player 1 hand
     * @param player2Hand player 2 hand
     */
    HandWinCalculator (ArrayList<Card> player1Hand, ArrayList<Card> player2Hand)
    {
        this.player1Hand = new ArrayList<>(player1Hand);
        this.player2Hand = new ArrayList<>(player2Hand);
    }

    //----- private instance methods

    /**
     *
     * @param index start index
     * @return string containing the winner or draw
     */
    private int calculateKickerWinner(int index)
    {
        if (index >= 5)
            return -1;

        for (; index < 5; index++)
            if (player1Hand.get(index).getRank() > player2Hand.get(index).getRank())
                return Dealer.PLAYER_1;
            else if (player1Hand.get(index).getRank() < player2Hand.get(index).getRank())
                return Dealer.PLAYER_2;

        return DRAW;
    }

    /**
     *
     * @return player with straight flush winning hand
     */
    private int calculateStraightFlushWinner()
    {
        Integer player1StraightFlushRank = player1Hand.get(0).getRank();
        Integer player2StraightFlushRank = player2Hand.get(0).getRank();

        if (player1StraightFlushRank.equals(player2StraightFlushRank))
            return DRAW;
        else
            return player1StraightFlushRank > player2StraightFlushRank ? Dealer.PLAYER_1 : Dealer.PLAYER_2;
    }

    /**
     *
     * @return player with 4 of a kind winning hand
     */
    private int calculateFourOfAKindWinner()
    {
        Integer player1FourOfAKindRank = player1Hand.get(0).getRank();
        Integer player2FourOfAKindRank = player2Hand.get(0).getRank();

        if (player1FourOfAKindRank.equals(player2FourOfAKindRank))
        {
            Integer player1KickerRank = player1Hand.get(4).getRank();
            Integer player2KickerRank = player2Hand.get(4).getRank();

            if (player1KickerRank.equals(player2KickerRank))
                return DRAW;
            else if (player1KickerRank.equals(Card.ACE))
                return Dealer.PLAYER_1;
            else if (player2KickerRank.equals(Card.ACE))
                return Dealer.PLAYER_2;
            else
                return player1KickerRank > player2KickerRank ? Dealer.PLAYER_1 : Dealer.PLAYER_2;
        }
        else if (player1FourOfAKindRank.equals(Card.ACE))
        {
            return Dealer.PLAYER_1;
        }
        else if (player2FourOfAKindRank.equals(Card.ACE))
        {
            return Dealer.PLAYER_2;
        }
        else
        {
            return player1FourOfAKindRank > player2FourOfAKindRank ? Dealer.PLAYER_1 : Dealer.PLAYER_2;
        }
    }

    /**
     *
     * @return player with full house winning hand
     */
    private int calculateFullHouseWinner()
    {
        Integer player1ThreeOfAKindRank = player1Hand.get(0).getRank();
        Integer player2ThreeOfAKindRank = player2Hand.get(0).getRank();
        Integer player1PairRank = player1Hand.get(3).getRank();
        Integer player2PairRank = player2Hand.get(3).getRank();

        if (player1ThreeOfAKindRank.equals(player2ThreeOfAKindRank))
        {
            if (player1PairRank.equals(player2PairRank))
                return DRAW;
            else if (player1PairRank.equals(Card.ACE))
                return Dealer.PLAYER_1;
            else if (player2PairRank.equals(Card.ACE))
                return Dealer.PLAYER_2;
            else
                return player1PairRank > player2PairRank ? Dealer.PLAYER_1 : Dealer.PLAYER_2;
        }
        else if (player1ThreeOfAKindRank.equals(Card.ACE))
        {
            return Dealer.PLAYER_1;
        }
        else if (player2ThreeOfAKindRank.equals(Card.ACE))
        {
            return Dealer.PLAYER_2;
        }
        else
        {
            return player1ThreeOfAKindRank > player2ThreeOfAKindRank ? Dealer.PLAYER_1 : Dealer.PLAYER_2;
        }
    }

    /**
     *
     * @return player with flush winning hand
     */
    private int calculateFlushWinner()
    {
        Integer player1FlushRank = player1Hand.get(0).getRank();
        Integer player2FlushRank = player2Hand.get(0).getRank();

        if (player1FlushRank.equals(player2FlushRank))
            return calculateKickerWinner(1);
        else if (player1FlushRank.equals(Card.ACE))
            return Dealer.PLAYER_1;
        else if (player2FlushRank.equals(Card.ACE))
            return Dealer.PLAYER_2;
        else
            return player1FlushRank > player2FlushRank ? Dealer.PLAYER_1 : Dealer.PLAYER_2;
    }

    /**
     *
     * @return player with straight winning hand
     */
    private int calculateStraightWinner()
    {
        Integer player1StraightRank = player1Hand.get(0).getRank();
        Integer player2StraightRank = player2Hand.get(0).getRank();

        if (player1StraightRank.equals(player2StraightRank))
            return DRAW;
        else
            return player1StraightRank > player2StraightRank ? Dealer.PLAYER_1 : Dealer.PLAYER_2;
    }

    /**
     *
     * @return player with 3 of kind winning hand
     */
    private int calculateThreeOfAKindWinner()
    {
        Integer player1ThreeOfAKindRank = player1Hand.get(0).getRank();
        Integer player2ThreeOfAKindRank = player2Hand.get(0).getRank();

        if (player1ThreeOfAKindRank.equals(player2ThreeOfAKindRank))
        {
            Boolean player1HasAce = Integer.valueOf(player1Hand.get(3).getRank()).equals(Card.ACE);
            Boolean player2HasAce = Integer.valueOf(player2Hand.get(3).getRank()).equals(Card.ACE);

            if (player1HasAce && player2HasAce)
                return calculateKickerWinner(4);
            else if (player1HasAce)
                return Dealer.PLAYER_1;
            else if (player2HasAce)
                return Dealer.PLAYER_2;
            else
                return calculateKickerWinner(3);
        }
        else if (player1ThreeOfAKindRank.equals(Card.ACE))
            return Dealer.PLAYER_1;
        else if (player2ThreeOfAKindRank.equals(Card.ACE))
            return Dealer.PLAYER_2;

        return player1ThreeOfAKindRank > player2ThreeOfAKindRank ? Dealer.PLAYER_1 : Dealer.PLAYER_2;
    }

    /**
     *
     * @return player with 2 pairs winning hand
     */
    private int calculateTwoPairWinner()
    {
        Integer player1Pair1Rank = player1Hand.get(0).getRank();
        Integer player2Pair1Rank = player2Hand.get(0).getRank();
        Integer player1Pair2Rank = player1Hand.get(2).getRank();
        Integer player2Pair2Rank = player2Hand.get(2).getRank();

        // same first pair
        if (player1Pair1Rank.equals(player2Pair1Rank))
        {
            // same second pair
            if (player1Pair2Rank.equals(player2Pair2Rank))
            {
                Integer player1KickerRank = player1Hand.get(4).getRank();
                Integer player2KickerRank = player2Hand.get(4).getRank();

                if (player1KickerRank.equals(player2KickerRank))
                    return DRAW;
                else if (player1KickerRank.equals(Card.ACE))
                    return Dealer.PLAYER_1;
                else if (player2KickerRank.equals(Card.ACE))
                    return Dealer.PLAYER_2;
                else
                    return player1KickerRank > player2KickerRank ? Dealer.PLAYER_1 : Dealer.PLAYER_2;
            }
            else
            {
                return player1Pair2Rank > player2Pair2Rank ? Dealer.PLAYER_1 : Dealer.PLAYER_2;
            }
        }
        else if (player1Pair1Rank.equals(Card.ACE))
        {
            return Dealer.PLAYER_1;
        }
        else if (player2Pair1Rank.equals(Card.ACE))
        {
            return Dealer.PLAYER_2;
        }

        return player1Pair1Rank > player2Pair1Rank ? Dealer.PLAYER_1 : Dealer.PLAYER_2;
    }

    /**
     *
     * @return player with par winning hand
     */
    private int calculateOnePairWinner()
    {
        Integer player1PairRank = player1Hand.get(0).getRank();
        Integer player2PairRank = player2Hand.get(0).getRank();

        if (player1PairRank.equals(player2PairRank))
        {
            Boolean player1HasAce = Integer.valueOf(player1Hand.get(2).getRank()).equals(Card.ACE);
            Boolean player2HasAce = Integer.valueOf(player2Hand.get(2).getRank()).equals(Card.ACE);

            if (player1HasAce && player2HasAce)
                return calculateKickerWinner(3);
            else if (player1HasAce)
                return Dealer.PLAYER_1;
            else if (player2HasAce)
                return Dealer.PLAYER_2;
            else
                return calculateKickerWinner(2);
        }
        else if (player1PairRank.equals(Card.ACE))
            return Dealer.PLAYER_1;
        else if (player2PairRank.equals(Card.ACE))
            return Dealer.PLAYER_2;

        return player1PairRank > player2PairRank ? Dealer.PLAYER_1 : Dealer.PLAYER_2;
    }

    /**
     *
     * @return player with high card winning hand
     */
    private int calculateHighCardWinner()
    {
        Boolean player1HasAce = Integer.valueOf(player1Hand.get(0).getRank()).equals(Card.ACE);
        Boolean player2HasAce = Integer.valueOf(player2Hand.get(0).getRank()).equals(Card.ACE);

        if (player1HasAce && player2HasAce)
            return calculateKickerWinner(1);
        else if (player1HasAce)
            return Dealer.PLAYER_1;
        else if (player2HasAce)
            return Dealer.PLAYER_2;
        else
            return calculateKickerWinner(0);
    }

    //----- public instance methods

    /**
     *
     * @param player1HandResult player 1 hand result
     * @param player2HandResult player 2 hand result
     * @return int with winning player
     */
    int calculate(int player1HandResult, int player2HandResult)
    {
        // draw
        if (player1HandResult == player2HandResult)
        {
            switch (player1HandResult)
            {
                case HandEvaluator.IS_HIGH_CARD:
                    return calculateHighCardWinner();
                case HandEvaluator.IS_PAIR:
                    return calculateOnePairWinner();
                case HandEvaluator.IS_TWO_PAIR:
                    return calculateTwoPairWinner();
                case HandEvaluator.IS_THREE_OF_A_KIND:
                    return calculateThreeOfAKindWinner();
                case HandEvaluator.IS_STRAIGHT:
                    return calculateStraightWinner();
                case HandEvaluator.IS_FLUSH:
                    return calculateFlushWinner();
                case HandEvaluator.IS_FULL_HOUSE:
                    return calculateFullHouseWinner();
                case HandEvaluator.IS_FOUR_OF_A_KIND:
                    return calculateFourOfAKindWinner();
                case HandEvaluator.IS_STRAIGHT_FLUSH:
                    return calculateStraightFlushWinner();
            }
        }

        return player1HandResult > player2HandResult ? Dealer.PLAYER_1 : Dealer.PLAYER_2;
    }

}
