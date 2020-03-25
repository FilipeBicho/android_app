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
    static int IS_THREE_OF_A_KING = 4;
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
     * @return true if has a pair and set best hand
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
        if(Integer.valueOf(allCards.get(0).getRank()).equals(0))
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
        setRankAndSuitCardsCount();;

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
    public ArrayList<Card> getHand()
    {
        return hand;
    }

}
