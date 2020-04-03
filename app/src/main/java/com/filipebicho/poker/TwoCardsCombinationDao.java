package com.filipebicho.poker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Data access object interface
 * @author filipe bicho created 02.04.2020
 */
@Dao
public interface TwoCardsCombinationDao {

    //----- Queries

    //--- insert value

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(TwoCardsCombination combination);

    //--- delete all values

    @Query("DELETE FROM two_cards_combination")
    void deleteAll();

    //--- select all combinations

    @Query("SELECT * FROM two_cards_combination")
    List<TwoCardsCombination> getAllCombinations();

    //--- select all combinations except the given ones

    @Query("SELECT * FROM two_cards_combination WHERE combination NOT IN (:combinations)")
    List<TwoCardsCombination> getAllRemainingCombinations(String[] combinations);

    //--- get count of two cards combinations

    @Query("SELECT COUNT(*) FROM two_cards_combination")
    int getCombinationsCount();

}
