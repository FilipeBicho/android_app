package com.filipebicho.poker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**
 * Two cards combinations table entity class
 * @author filipe bicho created 02.04.2020
 */
@Entity(tableName = "two_cards_combinations")
public class TwoCardsCombinations {

    //----- private instance variables

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "combination")
    private String combination;

    //----- public constructor

    /**
     *
     * @param combination string
     */
    public TwoCardsCombinations(int id, String combination)
    {
        this.id = id;
        this.combination = combination;
    }

    //----- public instance methods

    /**
     *
     * @return id
     */
    public int getId()
    {
        return this.id;
    }

    /**
     *
     * @return combination
     */
    public String getCombination()
    {
        return this.combination;
    }
}
