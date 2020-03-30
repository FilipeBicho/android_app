package com.example.poker;


import java.util.ArrayList;
import java.util.Random;

/**
 * Class to create and shuffle a deck of cards
 *
 * @author filipe bicho created 23.03.2020
 */
class Deck {

    //----- private instance variable

    /**
     * ArrayList cards
     */
    private ArrayList<Card> deck = new ArrayList<>();

    //----- public constructor

    Deck()
    {

        Random random = new Random();

        // initialize deck
        for (int rank = 0; rank < Card.RANK_CARDS_SIZE; rank++)
        {
            for (int suit = 0; suit < Card.SUIT_CARDS_SIZE; suit++)
                deck.add(new Card(rank, suit));
        }

        // shuffle cards with Fisher-Yates algorithm
        for (int i = deck.size() - 1; i > 0; i--)
        {
            // Random a number between 0 and i+1
            int index = random.nextInt(i+1);

            // Temp gets the card that is at index
            Card temp = deck.get(index);

            // Change card from index with card at i
            deck.set(index, deck.get(i));

            // Change card at i with temp card
            deck.set(i, temp);
        }
    }

    //----- public instance method

    /**
     *
     * @return a card from the top of the deck and delete it
     */
    Card getCard()
    {
        return deck.remove(deck.size()-1);
    }

    /**
     *
     * @return current deck
     */
    ArrayList<Card> getDeck()
    {
        return deck;
    }
}
