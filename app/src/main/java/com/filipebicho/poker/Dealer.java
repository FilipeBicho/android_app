package com.filipebicho.poker;

import java.util.ArrayList;

/**
 * Distribute cards to players and table
 *
 * @author filipe bicho created 24.03.2020
 */
class Dealer {

    static final int PLAYER_1 = 0;
    static final int PLAYER_2 = 1;

    //----- public instance methods

    /**
     * each player gets 2 cards
     *
     * @param deck of cards
     * @param player1 cards
     * @param player2 cards
     */
    void setPlayersCards(Deck deck, ArrayList<Card> player1, ArrayList<Card> player2)
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
    void setFlop(Deck deck, ArrayList<Card> table)
    {
        //burn card
        deck.getCard();

        //add 3 cards
        for(int i=0; i < 3; i++)
            table.add(deck.getCard());
    }

    void setOneCard(Deck deck, ArrayList<Card> table)
    {
        //burn card
        deck.getCard();

        //add 1 card
        table.add(deck.getCard());
    }
}
