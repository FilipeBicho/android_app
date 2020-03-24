package com.example.poker;

import java.util.ArrayList;

/**
 * Distribute cards to players and table
 *
 * @author filipe bicho created 27.10.2017 improved 24.03.2020
 */
public class Dealer {

    //----- public instance methods

    /**
     * each player gets 2 cards
     *
     * @param deck of cards
     * @param player1 cards
     * @param player2 cards
     */
    public void setPlayersCards(Deck deck, ArrayList<Card> player1, ArrayList<Card> player2)
    {
        player1.add(deck.getCard());
        player2.add(deck.getCard());
        player1.add(deck.getCard());
        player2.add(deck.getCard());
    }

    /**
     * table get 3 cards
     *
     * @param deck of cards
     * @param table cards
     */
    public void setFlop(Deck deck, ArrayList<Card> table)
    {
        //burn card
        deck.getCard();

        //add 3 cards
        for(int i=0; i < 3; i++)
            table.add(deck.getCard());
    }

    public void setOneCard(Deck deck, ArrayList<Card> table)
    {
        //burn card
        deck.getCard();

        //add 1 card
        table.add(deck.getCard());
    }
}
