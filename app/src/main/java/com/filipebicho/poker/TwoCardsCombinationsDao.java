package com.filipebicho.poker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Data access object interface
 * @author filipe bicho created 02.04.2020
 */
@Dao
public interface TwoCardsCombinationsDao {

    //----- Queries

    //--- insert value

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(TwoCardsCombinations combination);

    //--- delete all values

    @Query("DELETE FROM two_cards_combinations")
    void deleteAll();

    //--- select all combinations

    @Query("SELECT * FROM two_cards_combinations")
    LiveData<List<TwoCardsCombinations>> getAllCombinations();

    //--- select all combinations except the given ones

    @Query("SELECT * FROM two_cards_combinations WHERE combination NOT IN (:combinations)")
    LiveData<List<TwoCardsCombinations>> getAllRemainingCombinations(String[] combinations);

}
