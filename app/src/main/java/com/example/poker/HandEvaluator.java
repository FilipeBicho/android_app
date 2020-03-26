package com.example.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Evaluator player poker hand on flop, turn and river
 * @author filipe bicho created 27.10.2017 improved 25.03.2020
 */
public class HandEvaluator {

    //----- private static variables

    static int IS_HIGH_CARD= 1;
    static int IS_PAIR = 2;
    static int IS_TWO_PAIR = 3;
    static int IS_THREE_OF_A_KIND = 4;
    static int IS_STRAIGHT = 5;
    static int IS_FLUSH = 6;
    static int IS_FULL_HOUSE = 7;
    static int IS_FOUR_OF_A_KIND = 8;
    static int IS_STRAIGHT_FLUSH = 9;
    static int IS_ROYAL_STRAIGHT_FLUSH = 10;

    private static String RANK_CARD_TYPE = "rank";
    private static String SUIT_CARD_TYPE = "suit";

    //----- private instance variables

    /**
     * stores all cards (Player cards + Table cards - 5 to 7 cards)
     */
    private ArrayList<Card> allCards = new ArrayList<>();

    /**
     * stores the best hand possible
     */
    private ArrayList<Card> hand = new ArrayList<>();

    /**
     * store count of a card rank
     */
    private HashMap<Integer, Integer> rankCount = new HashMap<>();

    /**
     * store count of a card suit
     */
    private HashMap<Integer, Integer> suitCount = new HashMap<>();


    //----- private instance methods

    /**
     * init allCards with player and table cards
     *
     * @param playerCards player cards
     * @param tableCards table cards
     */
    private void initAllCards(ArrayList<Card> playerCards, ArrayList<Card> tableCards)
    {
        allCards.addAll(0, playerCards);
        allCards.addAll(allCards.size(), tableCards);
    }

    /**
     * set rank and suit cards count
     * it will help calculate pair, 2 pair, 3 of a kind, 4 of a kind and flush hands
     */
    private void setRankAndSuitCardsCount()
    {
        ArrayList<Integer> cardsRank = new ArrayList<>();
        ArrayList<Integer> cardsSuit = new ArrayList<>();

        for(Card card : allCards)
        {
            cardsRank.add(card.getRank());
            cardsSuit.add(card.getSuit());
        }

        // set rank count
        for(Integer count : cardsRank)
            rankCount.put(count, Collections.frequency(cardsRank, count));

        // set suit count
        for(Integer count : cardsSuit)
            suitCount.put(count, Collections.frequency(cardsSuit, count));
    }

    /**
     *
     * @param value used to search a key
     * @param cardsTypeCount HashMap card type
     * @return keys that contain the given value
     */
    private ArrayList<Integer> getHashMapKeysFromValue(int value, HashMap<Integer, Integer> cardsTypeCount)
    {
        ArrayList<Integer> keys = new ArrayList<>();
        for (Map.Entry entry : cardsTypeCount.entrySet())
        {
            if (Integer.valueOf(value).equals(entry.getValue()))
                keys.add((Integer) entry.getKey());
        }

        return keys;
    }


    /**
     * set hand cards by cards type
     *
     * @param key of card to add to hand
     * @param cardType to add to hand
     */
    private void setHand(Integer key, String cardType)
    {
        if (cardType.equals(RANK_CARD_TYPE))
        {
            for (Card card : allCards)
            {
                if (key.equals(card.getRank()))
                    hand.add(card);
            }
        }
        else if (cardType.equals(SUIT_CARD_TYPE))
        {
            for (Card card : allCards)
            {
                if (key.equals(card.getSuit()))
                    hand.add(card);
            }
        }
        else
            throw new java.lang.Error("Error defining hand cards - setHandCards()");
    }

    /**
     * set hand kicker
     */
    private void setHandKicker()
    {
        ArrayList<Card> kicker = new ArrayList<>();

        for(Card card : allCards)
        {
            if(!hand.contains(card))
                kicker.add(card);
        }

        // Check if has an Ace
        // If not gets the card with highest value
        if(Integer.valueOf(kicker.get(0).getRank()).equals(0))
            hand.add(kicker.get(0));
        else
            hand.add(kicker.get(kicker.size()-1));
    }

    /**
     *
     * @return true if has a flush and set best hand
     */
    private Boolean isFlush()
    {
        // 7 cards flush
        if (suitCount.containsValue(7))
        {
            // Store all the keys that contains 7 cards flush
            int key = getHashMapKeysFromValue(7, suitCount).get(0);
            setHand(key, SUIT_CARD_TYPE);

            // flush with Ace
            if (Integer.valueOf(hand.get(0).getRank()).equals(Card.ACE))
            {
                // move Ace to last position
                hand.add(hand.size(), hand.get(0));
                hand.remove(0);
            }

            // remove the 2 lowest cards
            hand.remove(0);
            hand.remove(0);

        }
        // 6 cards flush
        else if (suitCount.containsValue(6))
        {
            // Store all the keys that contains 6 cards flush
            int key = getHashMapKeysFromValue(6, suitCount).get(0);
            setHand(key, SUIT_CARD_TYPE);

            // flush with Ace
            if (Integer.valueOf(hand.get(0).getRank()).equals(Card.ACE))
            {

                // move Ace to last position
                hand.add(hand.size(), hand.get(0));
                hand.remove(0);
            }

            // remove the lowest card
            hand.remove(0);
        }
        // 5 cards flush
        else if (suitCount.containsValue(5))
        {
            // Store all the keys that contains 5 cards flush
            int key = getHashMapKeysFromValue(5, suitCount).get(0);
            setHand(key, SUIT_CARD_TYPE);

            // flush with Ace
            if (Integer.valueOf(hand.get(0).getRank()).equals(Card.ACE))
            {
                // move Ace to last position
                hand.add(hand.size(), hand.get(0));
                hand.remove(0);
            }
        }
        else
        {
            return false;
        }

        return true;
    }

    /**
     *
     * @return true if has a straight and set best hand
     */
    private Boolean isStraight()
    {
        int straightCount = 0;
        int straight = 0;
        int straightStartIndex = 0;
        for(int i = 0; i < allCards.size() - 1; i++)
        {
            // If next card is the same as the actual card goes to the next iteration
            if(Integer.valueOf(allCards.get(i).getRank()).equals(allCards.get(i+1).getRank()))
                continue;

            if(Integer.valueOf(allCards.get(i).getRank()).equals(allCards.get(i+1).getRank()-1))
            {
                straightCount++;
                if(straight < straightCount)
                {
                    straight = straightCount;
                    straightStartIndex = i+1;
                }
            }
            else
            {
                straightCount = 0;
            }
        }

        if (straight >= 3 &&
                Integer.valueOf(allCards.get(straightStartIndex).getRank()).equals(Card.KING) &&
                Integer.valueOf(allCards.get(0).getRank()).equals(Card.ACE))
        {
            hand.add(allCards.get(0));
            hand.add(allCards.get(straightStartIndex));
            hand.add(allCards.get(straightStartIndex-1));
            hand.add(allCards.get(straightStartIndex-2));
            hand.add(allCards.get(straightStartIndex-3));
        }
        else if (straight >= 4)
        {
            hand.add(allCards.get(straightStartIndex));
            hand.add(allCards.get(straightStartIndex-1));
            hand.add(allCards.get(straightStartIndex-2));
            hand.add(allCards.get(straightStartIndex-3));
            hand.add(allCards.get(straightStartIndex-4));
        }
        else
        {
            return false;
        }

        return true;
    }

    /**
     *
     * @return true if has 3 of a kind and set best hand
     */
    private Boolean isThreeOfAKind()
    {
        if (rankCount.containsValue(3))
        {
            // Store all the keys that contains a three of a kind
            ArrayList<Integer> keys = getHashMapKeysFromValue(3, rankCount);

            // has more than 1 three of a kind
            if (keys.size() > 1)
            {
                // set hand with all three of a kinds
                for(Integer key : keys)
                    setHand(key, RANK_CARD_TYPE);

                // has a Three of a kind of Aces
                if(Integer.valueOf(hand.get(0).getRank()).equals(Card.ACE))
                {
                    // Remove the lowest three of a kind
                    hand.remove(3);
                    hand.remove(3);
                    hand.remove(3);
                    setHandKicker();
                    setHandKicker();
                }
                else
                {
                    // Remove the lowest three of a kind
                    hand.remove(0);
                    hand.remove(0);
                    hand.remove(0);
                    setHandKicker();
                    setHandKicker();
                }
            }
            else
            {
                int key = getHashMapKeysFromValue(3, rankCount).get(0);
                setHand(key, RANK_CARD_TYPE);
                setHandKicker();
                setHandKicker();
            }

            return true;
        }

        return false;
    }

    /**
     *
     * @return true if has 2 pairs and set best hand
     */
    private Boolean isTwoPair()
    {
        // Store all the keys that contains a pair
        ArrayList<Integer> keys = getHashMapKeysFromValue(2, rankCount);

        // Check if has more than a pair
        if(keys.size() > 1)
        {
            // Hand gets all the pairs
            for(Integer key : keys)
                setHand(key, RANK_CARD_TYPE);

            // 2 pairs
            if(Integer.valueOf(keys.size()).equals(2))
            {
                if(!Integer.valueOf(hand.get(0).getRank()).equals(Card.ACE))
                {
                    // move biggest pair to first position
                    hand.add(hand.size(), hand.get(0));
                    hand.remove(0);

                    hand.add(hand.size(), hand.get(0));
                    hand.remove(0);
                }

                setHandKicker();
            }
            // 3 pairs
            else
            {
                // Check if has a pair of Aces
                if(Integer.valueOf(hand.get(0).getRank()).equals(Card.ACE))
                {
                    // Remove the lowest pair
                    hand.remove(2);
                    hand.remove(2);
                    setHandKicker();
                }
                else
                {
                    // Remove the lowest pair
                    hand.remove(0);
                    hand.remove(0);

                    // move biggest pair to first position
                    hand.add(hand.size(), hand.get(0));
                    hand.remove(0);

                    hand.add(hand.size(), hand.get(0));
                    hand.remove(0);

                    setHandKicker();
                }
            }

            return true;
        }

        return false;
    }

    /**
     *
     * @return true true if has a pair and set best hand
     */
    private Boolean isPair()
    {
        if (rankCount.containsValue(2))
        {
            Integer key = getHashMapKeysFromValue(2, rankCount).get(0);
            setHand(key, RANK_CARD_TYPE);
            setHandKicker();
            setHandKicker();
            setHandKicker();

            return true;
        }

        return false;
    }

    /**
     *
     * @return always true and set highest hand
     */
    private Boolean isHighCard()
    {
        // Check if cards has an Ace
        if(Integer.valueOf(allCards.get(0).getRank()).equals(Card.ACE))
        {
            hand.add(allCards.get(0));
            hand.add(allCards.get(allCards.size()-1));
            hand.add(allCards.get(allCards.size()-2));
            hand.add(allCards.get(allCards.size()-3));
            hand.add(allCards.get(allCards.size()-4));
        }
        else
        {
            hand.add(allCards.get(allCards.size()-1));
            hand.add(allCards.get(allCards.size()-2));
            hand.add(allCards.get(allCards.size()-3));
            hand.add(allCards.get(allCards.size()-4));
            hand.add(allCards.get(allCards.size()-5));
        }

        return true;
    }


    //----- public instance methods

    int evaluate(ArrayList<Card> playerCards, ArrayList<Card> tableCards)
    {
        // merge player and table cards
        initAllCards(playerCards, tableCards);

        // Sort card by rank
        Collections.sort(allCards, Card.sortRank);

        // count rank and suit count
        setRankAndSuitCardsCount();

        if (isFlush())
            return IS_FLUSH;

        if (isStraight())
            return IS_STRAIGHT;

        if (isThreeOfAKind())
            return IS_THREE_OF_A_KIND;

        if (isTwoPair())
            return IS_TWO_PAIR;

        if (isPair())
            return IS_PAIR;

        if (isHighCard())
            return IS_HIGH_CARD;

        return 0;
    }

    /**
     *
     * @return final hand
     */
    ArrayList<Card> getHand()
    {
        return hand;
    }

}
