package com.filipebicho.poker;


import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Set combinations, save into DB and get from DB
 * @author filipe bicho created 03.04.2020
 */
class CombinationsCalculator {

    //----- static instance variables

    /**
     * total of two cards combinations C(52,2)
     */
    private static int TWO_CARDS_COMBINATIONS_TOTAL = 1326;

    /**
     * total of four cards combinations C(52,4)
     */
    private static int FOUR_CARDS_COMBINATIONS_TOTAL = 270725;

    //----- private instance variables

    /**
     * Deck cards
     */
    private ArrayList<Card> deck;

    /**
     * Two cards combinations Dao
     */
    private TwoCardsCombinationDao twoCardsCombinationsDao;

    /**
     * Four cards combinations Dao
     */
    private FourCardsCombinationDao fourCardsCombinationsDao;



    //----- public constructor

    /**
     *
     * @param deck cards ArrayList
     * @param applicationContext Application Context
     */
    CombinationsCalculator(ArrayList<Card> deck, Context applicationContext)
    {
        this.deck = deck;
        twoCardsCombinationsDao = CombinationsRoomDatabase.getInstance(applicationContext).twoCardsCombinationsDao();
        fourCardsCombinationsDao = CombinationsRoomDatabase.getInstance(applicationContext).fourCardsCombinationsDao();
        setTwoCardsCombinations();
        setFourCardsCombinations();
    }

    //----- private instance methods


    /**
     * insert to database 2 cards combinations
     */
    private void insertIntoDBTwoCardsCombinations()
    {
        ArrayList<Card> deck = new ArrayList<>(this.deck);

        for (int i = 0; i < deck.size() -1 ; i++)
        {
            Card card1 = new Card(deck.get(i));

            for (int j = i + 1; j < deck.size(); j++)
            {
                Card card2 = new Card(deck.get(j));

                String convertedCombinationString = card1.getRank() + "-" +
                                card1.getSuit() + "_" +
                                card2.getRank() + "-" +
                                card2.getSuit();
                twoCardsCombinationsDao.insert(new TwoCardsCombination(convertedCombinationString));

            }
        }
    }

    /**
     * insert to database 4 cards combinations
     */
    private void insertIntoDBFourCardsCombinations()
    {
        ArrayList<Card> deck = new ArrayList<>(this.deck);

        int count = 0;
        for (int i = 0; i < deck.size() - 3 ; i++)
        {
            Card card1 = new Card(deck.get(i));

            for (int j = i + 1; j < deck.size() - 2; j++)
            {
                Card card2 = new Card(deck.get(j));

                for (int k = j + 1; k < deck.size() - 1; k++)
                {
                    Card card3 = new Card(deck.get(k));

                    for (int l = k + 1; l < deck.size(); l++)
                    {
                        Card card4 = new Card(deck.get(l));
                        String convertedCombinationString = card1.getRank() + "-" +
                                card1.getSuit() + "_" +
                                card2.getRank() + "-" +
                                card2.getSuit() + "_" +
                                card3.getRank() + "-" +
                                card3.getSuit() + "_" +
                                card4.getRank() + "-" +
                                card4.getSuit();
                        fourCardsCombinationsDao.insert(new FourCardsCombination(convertedCombinationString));
                        System.out.println(count);
                        count++;
                    }
                }
            }
        }
    }

    /**
     * add two cards combinations into database
     */
    private void setTwoCardsCombinations()
    {
        if (twoCardsCombinationsDao.getCombinationsCount() < TWO_CARDS_COMBINATIONS_TOTAL)
        {
            //remove all combinations entries
            twoCardsCombinationsDao.deleteAll();
            insertIntoDBTwoCardsCombinations();
        }
    }

    /**
     * add four cards combinations into database
     */
    private void setFourCardsCombinations()
    {
        if (fourCardsCombinationsDao.getCombinationsCount() < FOUR_CARDS_COMBINATIONS_TOTAL)
        {
            //remove all combinations entries
            fourCardsCombinationsDao.deleteAll();
            insertIntoDBFourCardsCombinations();
        }
    }

    /**
     *
     * @param encodedCard enconded card
     * @return card object
     */
    private Card getConvertedCard(String encodedCard)
    {
        Pattern cardTypePattern = Pattern.compile("([^-]+)");
        Matcher cardTypeMatcher = cardTypePattern.matcher(encodedCard);

        int rank = cardTypeMatcher.find() ? Integer.parseInt(cardTypeMatcher.group(0)) : null;

        int suit = cardTypeMatcher.find() ? Integer.parseInt(cardTypeMatcher.group(0)) : null;

        return new Card(rank, suit);
    }

    /**
     *
     * @param combination string
     * @return ArrayList cards containing the given combination
     */
    private ArrayList<Card> getConvertedCardsCombination(String combination)
    {
        ArrayList<Card> cards = new ArrayList<>();

        Pattern cardsPattern = Pattern.compile("([^_]+)");
        Matcher cardsMatcher = cardsPattern.matcher(combination);

        while (cardsMatcher.find())
            cards.add(getConvertedCard(cardsMatcher.group(0)));

        return cards;
    }

    /**
     *
     * @param combination String containing combination
     * @param cards ArrayList containing cards to compare
     * @return true if combination contains any of the given cards
     */
    private Boolean combinationContainsCard(String combination, ArrayList<Card> cards)
    {
        ArrayList<Card> cardsCombination = getConvertedCardsCombination(combination);

        for (Card card : cardsCombination)
        {
            if (cards.contains(card))
                return true;
        }

        return false;
    }

    //----- public instance methods

    /**
     *
     * @return two cards combinations
     */
    ArrayList<ArrayList<Card>> getTwoCardsCombinationsWithoutGivenCards(ArrayList<Card> cards)
    {
        ArrayList<ArrayList<Card>> twoCardsCombinations = new ArrayList<>();

        for (TwoCardsCombination combination : twoCardsCombinationsDao.getAllCombinations())
        {
            if (!combinationContainsCard(combination.getCombination(), cards))
                twoCardsCombinations.add(getConvertedCardsCombination(combination.getCombination()));
        }

        return twoCardsCombinations;
    }

    /**
     *
     * @param cards ArrayList containing used cards
     * @param limit limit combinations of four cards
     * @return four cards combinations
     */
    ArrayList<ArrayList<Card>> getFourCardsCombinationsWithoutGivenCards(ArrayList<Card> cards, int limit)
    {
        ArrayList<ArrayList<Card>> fourCardsCombinations = new ArrayList<>();

        for (FourCardsCombination combination : fourCardsCombinationsDao.getRandomCombinationsWithLimit(limit))
        {
            if (!combinationContainsCard(combination.getCombination(), cards))
                fourCardsCombinations.add(getConvertedCardsCombination(combination.getCombination()));
        }

        return fourCardsCombinations;
    }

}
