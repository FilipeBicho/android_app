package com.filipebicho.poker;


import android.content.Context;
import android.content.res.Resources;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
     * total of three cards combinations C(52,4)
     */
    private static int THREE_CARDS_COMBINATIONS_TOTAL = 22100;

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
     * Three cards combinations Dao
     */
    private ThreeCardsCombinationDao threeCardsCombinationsDao;

    /**
     * Four cards combinations Dao
     */
    private FourCardsCombinationDao fourCardsCombinationsDao;

    /**
     * Two cards combinations ArrayList
     */
    private ArrayList<ArrayList<Card>> twoCardsCombinations = new ArrayList<>();

    /**
     * Three cards combinations ArrayList
     */
    private ArrayList<ArrayList<Card>> threeCardsCombinations = new ArrayList<>();

    /**
     * Four cards combinations ArrayList
     */
    private ArrayList<ArrayList<Card>> fourCardsCombinations = new ArrayList<>();

    /**
     * Two cards combinations string ArrayList
     */
    private ArrayList<String> twoCardsCombinationsString = new ArrayList<>();

    /**
     * Three cards combinations string ArrayList
     */
    private ArrayList<String> threeCardsCombinationsString = new ArrayList<>();

    /**
     * Four cards combinations string ArrayList
     */
    private ArrayList<String> fourCardsCombinationsString = new ArrayList<>();

    //----- public constructor

    /**
     *
     * @param deck cards ArrayList
     * @param resources Resources
     */
    CombinationsCalculator(ArrayList<Card> deck, Resources resources) throws IOException
    {
        this.deck = deck;
        initCardsCombinationsFromResources(resources);
    }

    //----- private instance methods

    /**
     * init two cards combinations string from file
     *
     * @param scanner two cards combinations file scanner
     */
    private void initTwoCardsCombinations(Scanner scanner)
    {
        while (scanner.hasNext())
            fourCardsCombinationsString.add(scanner.nextLine());
    }

    /**
     * init three cards combinations string from file
     *
     * @param scanner three cards combinations file scanner
     */
    private void initThreeCardsCombinations(Scanner scanner)
    {
        while (scanner.hasNext())
            threeCardsCombinationsString.add(scanner.nextLine());
    }

    /**
     * init four cards combinations string from file
     *
     * @param scanner four cards combinations file scanner
     */
    private void initFourCardsCombinations(@NotNull Scanner scanner)
    {
        while (scanner.hasNext())
            twoCardsCombinationsString.add(scanner.nextLine());
    }

    /**
     * init card combinations string combinations from resources
     * @param resources resources
     * @throws IOException
     */
    private void initCardsCombinationsFromResources(@NotNull Resources resources) throws IOException
    {
        InputStream inputStream = resources.openRawResource(R.raw.combinations);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry zipEntry;

        while ((zipEntry = zipInputStream.getNextEntry()) != null)
        {
            if (zipEntry.getName().equals("four_cards_combinations.txt"))
                initFourCardsCombinations(new Scanner(zipInputStream));
            if (zipEntry.getName().equals("three_cards_combinations.txt"))
                initThreeCardsCombinations(new Scanner(zipInputStream));
            if (zipEntry.getName().equals("two_cards_combinations.txt"))
                initTwoCardsCombinations(new Scanner(zipInputStream));
        }
        zipInputStream.close();
        inputStream.close();
    }


    /**
     * insert to database 2 cards combinations
     */
    private void insertIntoDBTwoCardsCombinations()
    {

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
     * insert to database 3 cards combinations
     */
    private void insertIntoDBThreeCardsCombinations()
    {
        for (int i = 0; i < deck.size() - 2 ; i++)
        {
            Card card1 = new Card(deck.get(i));
            for (int j = i + 1; j < deck.size() - 1; j++)
            {
                Card card2 = new Card(deck.get(j));
                for (int k = j + 1; k < deck.size(); k++)
                {
                    Card card3 = new Card(deck.get(k));

                    String convertedCombinationString = card1.getRank() + "-" +
                            card1.getSuit() + "_" +
                            card2.getRank() + "-" +
                            card2.getSuit() + "_" +
                            card3.getRank() + "-" +
                            card3.getSuit();

                    threeCardsCombinationsDao.insert(new ThreeCardsCombination(convertedCombinationString));
                }
            }
        }
    }

    /**
     * insert to database 4 cards combinations
     */
    private void insertIntoDBFourCardsCombinations()
    {

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
     * add three cards combinations into database
     */
    private void setThreeCardsCombinations()
    {
        if (threeCardsCombinationsDao.getCombinationsCount() < THREE_CARDS_COMBINATIONS_TOTAL)
        {
            //remove all combinations entries
            threeCardsCombinationsDao.deleteAll();
            insertIntoDBThreeCardsCombinations();
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

    //----- public instance methods

    /**
     *
     * @param usedCards ArrayList containing used cards
     * @param combination ArrayList containing current combination
     * @return true if combination contains any of the given cards
     */
    Boolean combinationContainsUsedCard(ArrayList<Card> usedCards, ArrayList<Card> combination)
    {
        for (Card card : usedCards)
        {
            if (combination.contains(card))
                return true;
        }

        return false;
    }

    /**
     *
     * @return two cards combinations
     */
    ArrayList<ArrayList<Card>> getTwoCardsCombinations()
    {
        if (!twoCardsCombinations.isEmpty())
            return twoCardsCombinations;

        for (TwoCardsCombination combination : twoCardsCombinationsDao.getAllCombinations())
            twoCardsCombinations.add(getConvertedCardsCombination(combination.getCombination()));

        return twoCardsCombinations;
    }

    /**
     *
     * @param limit limit combinations of four cards
     * @return three cards combinations
     */
    ArrayList<ArrayList<Card>> getThreeCardsCombinations(int limit)
    {
        if (!threeCardsCombinations.isEmpty())
            return threeCardsCombinations;

        for (ThreeCardsCombination combination : threeCardsCombinationsDao.getRandomCombinationsWithLimit(limit))
            threeCardsCombinations.add(getConvertedCardsCombination(combination.getCombination()));

        return threeCardsCombinations;
    }

    /**
     *
     * @param limit limit combinations of four cards
     * @return four cards combinations
     */
    ArrayList<ArrayList<Card>> getFourCardsCombinations(int limit)
    {
        if (!fourCardsCombinations.isEmpty())
            return fourCardsCombinations;

        for (FourCardsCombination combination : fourCardsCombinationsDao.getRandomCombinationsWithLimit(limit))
            fourCardsCombinations.add(getConvertedCardsCombination(combination.getCombination()));

        return fourCardsCombinations;
    }
}
