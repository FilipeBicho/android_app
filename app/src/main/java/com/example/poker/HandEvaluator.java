package com.example.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Evaluator player poker hand on flop, turn and river
 * @author filipe bicho created 27.10.2017 improved 25.03.2020
 */
public class HandEvaluator {

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


    //----- public instance methods

    int evaluate(ArrayList<Card> playerCards, ArrayList<Card> tableCards)
    {
        // merge player and table cards
        initAllCards(playerCards, tableCards);

        // count rank and suit count
        setRankAndSuitCardsCount();;

        return 0;
    }

}
