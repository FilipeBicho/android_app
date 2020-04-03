package com.filipebicho.poker;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 *
 * Define a data structure to save and get the 52 cards of the deck
 * @author filipe bicho created 23.03.2020
 *
 */
public class Card {

    static int RANK_CARDS_SIZE = 13;
    static int SUIT_CARDS_SIZE = 4;

    static int ACE = 0;
    static int TWO = 1;
    static int THREE = 2;
    static int FOUR = 3;
    static int FIVE = 4;
    static int SIX = 5;
    static int SEVEN = 6;
    static int EIGHT = 7;
    static int NINE = 8;
    static int TEN = 9;
    static int JACK = 10;
    static int QUEEN = 11;
    static int KING = 12;

    static int SUIT_HEARTS = 0;
    static int SUIT_DIAMONDS = 1;
    static int SUIT_CLUBS = 2;
    static int SUIT_SPADES = 3;


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
     * initialize card with rank and suit
     *
     * @param rank given rank
     * @param suit given suit
     */
    Card(int rank, int suit)
    {
        this.cardRank = rank;
        this.cardSuit = suit;
    }

    /**
     * initialize card with card
     *
     * @param card give card
     */
    public Card(Card card)
    {
        this.cardRank = card.getRank();
        this.cardSuit = card.getSuit();
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

    /**
     * order cards by rank asc
     */
    static final Comparator<Card> sortRankAsc = new Comparator<Card>() {

        public int compare(Card card1, Card card2) {

            int rank1 = card1.getRank();
            int rank2 = card2.getRank();

            // sort asc
            return rank1 - rank2;
        }
    };

    /**
     * order cards by rank desc
     */
    static final Comparator<Card> sortRankDesc = new Comparator<Card>() {

        public int compare(Card card1, Card card2) {

            int rank1 = card1.getRank();
            int rank2 = card2.getRank();

            // sort asc
            return rank2 - rank1;
        }
    };

    /**
     *
     * @param card Card
     * @return true if Card objects are equal
     */
    public boolean equals(Object card)
    {
        return ((Card) card).getRank() == this.getRank() &&
                ((Card) card).getSuit() == this.getSuit();
    }
}
