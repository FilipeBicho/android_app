package com.example.poker;

import org.jetbrains.annotations.NotNull;

/**
 *
 * Define a data structure to save and get the 52 cards of the deck
 * @author Filipe Bicho 23.03.2020 improved
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
    Card(int rank, int suit) {
        this.cardRank = rank;
        this.cardSuit = suit;
    }

    //----- private instance methods

    /**
     *
     * @return card rank
     */
    private int getRank() {
        return cardRank;
    }

    /**
     *
     * @return card suit
     */
    private int getSuit() {
        return cardSuit;
    }

    //----- public instance methods

    /**
     * overrides toString method
     *
     * @return card in string format
     */
    @NotNull
    public String toString() {
        return ranks[cardRank] + suits[cardSuit];
    }

    /**
     *
     * @return card image name
     */
    String getCardDrawableName() {
        return "_" + this.getRank() + this.getSuit();
    }
}
