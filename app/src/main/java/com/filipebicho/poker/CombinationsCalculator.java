package com.filipebicho.poker;


import android.content.Context;
import android.util.JsonWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
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
     * Two cards combination Permutations
     */
    private LinkedHashSet<String> twoCardsCombinationPermutations;


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
        setTwoCardsCombinations();
    }

    //----- private instance methods

    /**
     *
     * @param combination ArrayList contains current combination
     * @param index start index to start calculate permutation
     */
    private void calculateCardsPermutations(ArrayList<String> combination, int index)
    {
        if (index >= combination.size() - 1)
        {
            StringBuilder permutation = new StringBuilder();
            for (String strCard : combination)
                permutation.append(strCard);

            twoCardsCombinationPermutations.add(permutation.toString());
        }

        for (int i = index; i < combination.size(); i++)
        {
            Collections.swap(combination, index, i);
            calculateCardsPermutations(combination,index + 1);
            Collections.swap(combination, index, i);
        }
    }

    /**
     *
     * @param combination ArrayList contains current combination
     * @return permutations of given combination
     */
    private LinkedHashSet<String> getCombinationPermutations(ArrayList<String> combination)
    {

        LinkedHashSet<String> permutations = new LinkedHashSet<>();
        twoCardsCombinationPermutations = new LinkedHashSet<>();

        calculateCardsPermutations(combination, 0);

        return twoCardsCombinationPermutations;
    }

    /**
     *
     * @param permutations ArrayList containing calculated permutations
     * @param combination string containing current cards combination
     * @return true if exists permutation for the current cards combination
     */
    private Boolean permutationOfCombinationExists(ArrayList<LinkedHashSet> permutations, String combination)
    {
        for (LinkedHashSet permutation : permutations)
        {
            if (permutation.contains(combination))
                return  true;
        }

        return false;
    }

    /**
     * insert to DB all two cards combinations
     */
    private void insertIntoDBTwoCardsCombinations()
    {
        ArrayList<String> cardsStr = new ArrayList<>();
        ArrayList<LinkedHashSet> permutations = new ArrayList<>();
        ArrayList<Card> currentCards;
        StringBuilder combination;

        // init current cards string combinations
        for (int i = 0; i < 2; i++)
            cardsStr.add("");

        for (Card card1 : deck)
        {
            cardsStr.set(0, card1.toString());

            for (Card card2 : deck)
            {
                if (!card2.equals(card1))
                {
                    cardsStr.set(1, card2.toString());

                    combination = new StringBuilder();
                    for (String cardStr : cardsStr)
                        combination.append(cardStr);

                    if (!permutationOfCombinationExists(permutations, combination.toString()))
                    {
                        permutations.add(getCombinationPermutations(cardsStr));

                        String convertedCombinationString = card1.getRank() + "-" +
                                card1.getSuit() + "_" +
                                card2.getRank() + "-" +
                                card2.getSuit();
                        twoCardsCombinationsDao.insert(new TwoCardsCombination(convertedCombinationString));
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
     *
     * @param encodedCard enconded card
     * @return card object
     */
    private Card getConvertedCard(String encodedCard)
    {
        Pattern cardTypePattern = Pattern.compile("([^-]+)");
        Matcher cardTypeMatcher = cardTypePattern.matcher(encodedCard);

        cardTypeMatcher.find();
        int rank = Integer.parseInt(cardTypeMatcher.group(0));

        cardTypeMatcher.find();
        int suit = Integer.parseInt(cardTypeMatcher.group(0));

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
    ArrayList<ArrayList<Card>> getTwoCardsCombinations()
    {
        ArrayList<ArrayList<Card>> twoCardsCombinations = new ArrayList<>();

        for (TwoCardsCombination combination : twoCardsCombinationsDao.getAllCombinations())
            twoCardsCombinations.add(getConvertedCardsCombination(combination.getCombination()));

        return twoCardsCombinations;
    }

    /**
     *
     * @return two cards combinations
     */
    ArrayList<ArrayList<Card>> getTwoCardsCombinationsWithoutGiveCards(ArrayList<Card> cards)
    {
        ArrayList<ArrayList<Card>> twoCardsCombinations = new ArrayList<>();

        for (TwoCardsCombination combination : twoCardsCombinationsDao.getAllCombinations())
        {
            if (!combinationContainsCard(combination.getCombination(), cards))
                twoCardsCombinations.add(getConvertedCardsCombination(combination.getCombination()));
        }

        return twoCardsCombinations;
    }

}
