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
public interface FourCardsCombinationDao {

    //----- Queries

    //--- insert value

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FourCardsCombination combination);

    //--- delete all values

    @Query("DELETE FROM four_cards_combination")
    void deleteAll();

    //--- select all combinations

    @Query("SELECT * FROM four_cards_combination")
    List<FourCardsCombination> getAllCombinations();

    //--- select random combinations with limit

    @Query("SELECT * FROM four_cards_combination ORDER BY RANDOM() LIMIT :limit")
    List<FourCardsCombination> getRandomCombinationsWithLimit(int limit);

    //--- get count of two cards combinations

    @Query("SELECT COUNT(*) FROM four_cards_combination")
    int getCombinationsCount();


}
