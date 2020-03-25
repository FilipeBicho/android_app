package com.example.poker;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 *
 * Define a data structure to save and get the 52 cards of the deck
 * @author filipe bicho created 27.10.2017 improved 23.03.2020
 *
 */
public class Card {

    static int RANK_CARDS_SIZE = 13;
    static int SUIT_CARDS_SIZE = 4;

    //----- private instance variables

    /**
     * card rank
     */
    private final int cardRank;

    /**
     * card suit
     */
    private final int cardSuit;

    /**
     * card ranks
     */
    private final String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    /**
     *
     * card suits
     * order: Hearts, Diamonds, Clubs, Spades
     */
    private final String[] suits = {"\u2665", "\u2666", "\u2663", "\u2660"};

    //----- public constructor

    /**
     * initialize cards
     *
     * @param rank given rank
     * @param suit given suit
     */
    Card(int rank, int suit)
    {
        this.cardRank = rank;
        this.cardSuit = suit;
    }

    //----- public instance methods

    /**
     *
     * @return card rank
     */
    public int getRank()
    {
        return cardRank;
    }

    /**
     *
     * @return card suit
     */
    public int getSuit()
    {
        return cardSuit;
    }

    /**
     * overrides toString method
     *
     * @return card in string format
     */
    @NotNull
    public String toString()
    {
        return ranks[cardRank] + suits[cardSuit];
    }

    /**
     *
     * @return card image name
     */
    String getCardDrawableName()
    {
        return "_" + this.getRank() + this.getSuit();
    }

    static final Comparator<Card> sortRank = new Comparator<Card>() {

        public int compare(Card card1, Card card2) {

            int rank1 = card1.getRank();
            int rank2 = card2.getRank();

            // sort asc
            return rank1 - rank2;

            // sort desc
            //rank2-rank1;
        }
    };
}
