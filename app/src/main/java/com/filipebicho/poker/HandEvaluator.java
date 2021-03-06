package com.filipebicho.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Evaluator player poker hand on flop, turn and river
 * @author filipe bicho created 25.03.2020
 */
class HandEvaluator {

    //----- static const variables

    static final int IS_HIGH_CARD = 1;
    static final int IS_PAIR = 2;
    static final int IS_TWO_PAIR = 3;
    static final int IS_THREE_OF_A_KIND = 4;
    static final int IS_STRAIGHT = 5;
    static final int IS_FLUSH = 6;
    static final int IS_FULL_HOUSE = 7;
    static final int IS_FOUR_OF_A_KIND = 8;
    static final int IS_STRAIGHT_FLUSH = 9;
    static final int IS_ROYAL_STRAIGHT_FLUSH = 10;

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
     * reset values
     */
    private void reset()
    {
        hand.clear();
        allCards.clear();
        rankCount.clear();
        suitCount.clear();
    }

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
        if(Integer.valueOf(kicker.get(0).getRank()).equals(Card.ACE))
            hand.add(kicker.get(0));
        else
            hand.add(kicker.get(kicker.size()-1));
    }

    /**
     *
     * @return true if has a royal straight flush and set best hand
     */
    private Boolean isRoyalStraightFlush()
    {
        if (isStraightFlush())
        {
            if (Integer.valueOf(hand.get(hand.size() - 1).getRank()).equals(Card.ACE))
                return true;

            hand.clear();
        }
        return false;
    }

    /**
     *
     * @return true if has a straight flush and set best hand
     */
    private Boolean isStraightFlush()
    {
        // flush with 7 cards
        if (suitCount.containsValue(7))
        {
            return isStraight();
        }
        // flush with 5 or 6 cards
        else if (suitCount.containsValue(5) || suitCount.containsValue(6))
        {

            ArrayList<Card> backupAllCards = new ArrayList<>(allCards);
            int key = -1;

            if (suitCount.containsValue(5))
                key = getHashMapKeysFromValue(5, suitCount).get(0);
            else if (suitCount.containsValue(6))
                key = getHashMapKeysFromValue(6, suitCount).get(0);

            // remove card without the flush suit
            for (Card card : new ArrayList<Card>(allCards))
            {
                if (card.getSuit() != key)
                    allCards.remove(card);
            }

            // check if all cards with flush suit have a straight
            if (isStraight())
            {
                allCards = backupAllCards;
                return true;
            }
            else
            {
                allCards = backupAllCards;
                return false;
            }
        }
        return false;
    }

    /**
     *
     * @return true if has a Four of a Kind and set best hand
     */
    private Boolean isFourOfAKind()
    {
        if(rankCount.containsValue(4))
        {
            int key = getHashMapKeysFromValue(4, rankCount).get(0);

            setHand(key, RANK_CARD_TYPE);
            setHandKicker();

            return true;
        }
        return false;
    }

    /**
     *
     * @return true if has a FullHouse and set best hand
     */
    private Boolean isFullHouse()
    {
        ArrayList<Integer> threeOfKindKeys = getHashMapKeysFromValue(3, rankCount);

        if (threeOfKindKeys.size() >= 1)
        {
            // has a second Three of a kind
            if (threeOfKindKeys.size() == 2 )
            {
                // set hand with all three of a kinds
                for(Integer key : threeOfKindKeys)
                    setHand(key, RANK_CARD_TYPE);

                if (Integer.valueOf(hand.get(0).getRank()).equals(Card.ACE))
                {
                   hand.remove(hand.size()-1);
                }
                else
                {
                    // move biggest three of a kind to first position
                    hand.add(hand.size(), hand.get(0));
                    hand.remove(0);
                    hand.add(hand.size(), hand.get(0));
                    hand.remove(0);

                    // set lowest three of a kind as a pair
                    hand.remove(0);
                }

                return true;
            }
            else
            {
                ArrayList<Integer> pairKeys = getHashMapKeysFromValue(2, rankCount);

                // has 1 pair or more
                if (pairKeys.size() >= 1)
                {
                    // set hand with three of a kind
                    for(Integer key : threeOfKindKeys)
                        setHand(key, RANK_CARD_TYPE);

                    // set hand with pair(s)
                    for(Integer key : pairKeys)
                        setHand(key, RANK_CARD_TYPE);

                    // has a three of a kind and 2 pairs
                    if (hand.size() == 7)
                    {
                        // biggest pair is an Ace
                        if (Integer.valueOf(hand.get(3).getRank()).equals(Card.ACE))
                        {
                            // remove lowest pair
                            hand.remove(hand.size() - 1);
                            hand.remove(hand.size() - 1);
                        }
                        else
                        {
                            // remove lowest pair
                            hand.remove(3);
                            hand.remove(3);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
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

            // Sort desc card by rank
            Collections.sort(hand, Card.sortRankDesc);

            // flush with Ace
            if (Integer.valueOf(hand.get(hand.size() - 1).getRank()).equals(Card.ACE))
            {
                // move Ace to first position
                hand.add(0, hand.get(hand.size() - 1));
                hand.remove(hand.size() - 1);
            }

            // remove the 2 lowest cards
            hand.remove(hand.size() - 1);
            hand.remove(hand.size() - 1);
        }
        // 6 cards flush
        else if (suitCount.containsValue(6))
        {
            // Store all the keys that contains 6 cards flush
            int key = getHashMapKeysFromValue(6, suitCount).get(0);
            setHand(key, SUIT_CARD_TYPE);

            // Sort desc card by rank
            Collections.sort(hand, Card.sortRankDesc);

            // flush with Ace
            if (Integer.valueOf(hand.get(hand.size() - 1).getRank()).equals(Card.ACE))
            {
                // move Ace to first position
                hand.add(0, hand.get(hand.size() - 1));
                hand.remove(hand.size() - 1);
            }

            // remove the lowest card
            hand.remove(hand.size() - 1);
        }
        // 5 cards flush
        else if (suitCount.containsValue(5))
        {
            // Store all the keys that contains 5 cards flush
            int key = getHashMapKeysFromValue(5, suitCount).get(0);
            setHand(key, SUIT_CARD_TYPE);

            // Sort desc card by rank
            Collections.sort(hand, Card.sortRankDesc);

            // flush with Ace
            if (Integer.valueOf(hand.get(hand.size() - 1).getRank()).equals(Card.ACE))
            {
                // move Ace to first position
                hand.add(0, hand.get(hand.size() - 1));
                hand.remove(hand.size() - 1);
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

        ArrayList<Card> allCardsWithoutRepetition = new ArrayList<>(allCards);

        // remove cards rank repetitions
        for(int i = 0; i < allCardsWithoutRepetition.size() - 1; i++)
        {
            if(Integer.valueOf(allCardsWithoutRepetition.get(i).getRank()).equals(allCardsWithoutRepetition.get(i+1).getRank()))
                allCardsWithoutRepetition.remove(allCardsWithoutRepetition.get(i+1));
        }

        for(int i = 0; i < allCardsWithoutRepetition.size() - 1; i++)
        {
            if(Integer.valueOf(allCardsWithoutRepetition.get(i).getRank()).equals(allCardsWithoutRepetition.get(i+1).getRank()-1))
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
                Integer.valueOf(allCardsWithoutRepetition.get(straightStartIndex).getRank()).equals(Card.KING) &&
                Integer.valueOf(allCardsWithoutRepetition.get(0).getRank()).equals(Card.ACE))
        {
            hand.add(allCardsWithoutRepetition.get(straightStartIndex-3));
            hand.add(allCardsWithoutRepetition.get(straightStartIndex-2));
            hand.add(allCardsWithoutRepetition.get(straightStartIndex-1));
            hand.add(allCardsWithoutRepetition.get(straightStartIndex));
            hand.add(allCardsWithoutRepetition.get(0));
        }
        else if (straight >= 4)
        {
            hand.add(allCardsWithoutRepetition.get(straightStartIndex-4));
            hand.add(allCardsWithoutRepetition.get(straightStartIndex-3));
            hand.add(allCardsWithoutRepetition.get(straightStartIndex-2));
            hand.add(allCardsWithoutRepetition.get(straightStartIndex-1));
            hand.add(allCardsWithoutRepetition.get(straightStartIndex));
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

    /**
     *
     * @param playerCards player cards
     * @param tableCards table cards
     * @return hand evaluation result
     */
    int evaluate(ArrayList<Card> playerCards, ArrayList<Card> tableCards)
    {
        reset();

        // merge player and table cards
        initAllCards(playerCards, tableCards);

        // Sort card by rank
        Collections.sort(allCards, Card.sortRankAsc);

        // count rank and suit count
        setRankAndSuitCardsCount();

        if (isRoyalStraightFlush())
            return IS_ROYAL_STRAIGHT_FLUSH;

        if (isStraightFlush())
            return IS_STRAIGHT_FLUSH;

        if (isFourOfAKind())
            return IS_FOUR_OF_A_KIND;

        if (isFullHouse())
            return IS_FULL_HOUSE;

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

    /**
     *
     * @return hand string
     */
    String getHandString()
    {
        return hand.get(0) + " " + hand.get(1) + " "  + hand.get(2) + " " + hand.get(3) + " " + hand.get(4);
    }

    /**
     *
     * @param ranking int containing the ranking
     * @return text from given ranking
     */
    String getHandEvaluationTextByRanking(int ranking)
    {
        String handEvaluationText = "";

        switch (ranking)
        {
            case IS_ROYAL_STRAIGHT_FLUSH:
                handEvaluationText = "Royal Straight Flush";
                break;
            case IS_STRAIGHT_FLUSH:
                handEvaluationText = "Straight Flush";
                break;
            case IS_FOUR_OF_A_KIND:
                handEvaluationText = "Four of a Kind";
                break;
            case IS_FULL_HOUSE:
                handEvaluationText = "Full House";
                break;
            case IS_FLUSH:
                handEvaluationText = "Flush";
                break;
            case IS_STRAIGHT:
                handEvaluationText = "Straight";
                break;
            case IS_THREE_OF_A_KIND:
                handEvaluationText = "Three of a Kind";
                break;
            case IS_TWO_PAIR:
                handEvaluationText = "Two Pairs";
                break;
            case IS_PAIR:
                handEvaluationText = "One Pair";
                break;
            case IS_HIGH_CARD:
                handEvaluationText = "High Card";
                break;
            default:
                handEvaluationText = "Ranking unknown";
                break;
        }

        return handEvaluationText;
    }

}
