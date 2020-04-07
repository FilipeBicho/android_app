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
public interface ThreeCardsCombinationDao {

    //----- Queries

    //--- insert value

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ThreeCardsCombination combination);

    //--- delete all values

    @Query("DELETE FROM three_cards_combination")
    void deleteAll();

    //--- select all combinations

    @Query("SELECT * FROM three_cards_combination")
    List<ThreeCardsCombination> getAllCombinations();

    //--- select random combinations with limit

    @Query("SELECT * FROM three_cards_combination ORDER BY RANDOM() LIMIT :limit")
    List<ThreeCardsCombination> getRandomCombinationsWithLimit(int limit);

    //--- get count of two cards combinations

    @Query("SELECT COUNT(*) FROM three_cards_combination")
    int getCombinationsCount();


}
