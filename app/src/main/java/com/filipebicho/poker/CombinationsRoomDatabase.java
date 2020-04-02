package com.filipebicho.poker;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Class to access Database
 * @author filipe bicho created 02.04.2020
 */
@Database(entities = Combinations.class, version = 1)
public abstract class CombinationsRoomDatabase extends RoomDatabase {

    //----- private static instance variables

    private static volatile CombinationsRoomDatabase combinationsRoomInstance;

    //----- public abstract instance methods

    public abstract CombinationsDao combinationsDao();

    //----- public static instance methods

    /**
     *
     * @param context Application context
     * @return instance of Database using Room.databaseBuilder()
     */
    static CombinationsRoomDatabase getDatabase(final Context context)
    {
        if (combinationsRoomInstance == null)
        {
            synchronized (CombinationsRoomDatabase.class)
            {
                if (combinationsRoomInstance == null)
                {
                    combinationsRoomInstance = Room.databaseBuilder(context.getApplicationContext(),
                            CombinationsRoomDatabase.class, "combinations_database")
                            .build();
                }
            }
        }
        return combinationsRoomInstance;
    }
}
