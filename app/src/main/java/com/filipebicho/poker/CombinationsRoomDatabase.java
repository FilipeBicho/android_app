package com.filipebicho.poker;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Room database
 * @author filipe bicho created 02.04.2020
 */
@Database(entities = {TwoCardsCombination.class, ThreeCardsCombination.class, FourCardsCombination.class}, version = 3, exportSchema = false)
public abstract class CombinationsRoomDatabase extends RoomDatabase {

    //----- public instance abstract variable

    public abstract TwoCardsCombinationDao twoCardsCombinationsDao();

    public abstract ThreeCardsCombinationDao threeCardsCombinationsDao();

    public abstract FourCardsCombinationDao fourCardsCombinationsDao();

    //----- static instance variables

    private static volatile CombinationsRoomDatabase INSTANCE;

    //----- static instance methods

    /**
     *
     * @param context App context
     * @return database singleton
     */
    static synchronized CombinationsRoomDatabase getInstance(Context context){
        if (INSTANCE == null)
            INSTANCE = create(context);
        return INSTANCE;
    }

    /**
     *
     * @param context App context
     * @return database singleton
     */
    private static CombinationsRoomDatabase create(final Context context){
        return Room.databaseBuilder(context,CombinationsRoomDatabase.class,"combinationsDatabase.db").allowMainThreadQueries().build();
    }
}
