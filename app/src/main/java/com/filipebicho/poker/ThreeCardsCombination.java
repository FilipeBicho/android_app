package com.filipebicho.poker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * Two cards combinations table entity class
 * @author filipe bicho created 02.04.2020
 */
@Entity(tableName = "three_cards_combination")
class ThreeCardsCombination {

    //----- private instance variables

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "combination")
    private String combination;

    //----- public constructor

    /**
     *
     * @param combination string
     */
    ThreeCardsCombination(@NotNull String combination)
    {
        this.combination = combination;
    }

    //----- public instance methods

    /**
     *
     * @return id
     */
    int getId()
    {
        return this.id;
    }

    /**
     *
     * @param id set id
     */
     void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return combination
     */
    @NotNull
    String getCombination()
    {
        return this.combination;
    }
}
