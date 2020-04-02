//package com.filipebicho.poker;
//
//import android.app.Application;
//
//import androidx.lifecycle.LiveData;
//
//import java.util.List;
//
///**
// * abstracts access to multiple data sources
// * @author filipe bicho created 02.04.2020
// */
//class TwoCardsCombinationsRepository {
//
//    //----- private instance variables
//
//    private TwoCardsCombinationsDao twoCardsCombinationsDao;
//    private LiveData<List<TwoCardsCombinations>> allCombinations;
//
//    //----- public constructor
//
//    /**
//     *
//     * @param application application
//     */
//    TwoCardsCombinationsRepository(Application application) {
//        CombinationsRoomDatabase db = CombinationsRoomDatabase.getDatabase(application);
//        twoCardsCombinationsDao = db.twoCardsCombinationsDao();
//        allCombinations = twoCardsCombinationsDao.getAllCombinations();
//    }
//
//    //----- public instance methods
//
//    /**
//     *
//     * @return all two cards combinations
//     */
//    LiveData<List<TwoCardsCombinations>> getAllCombinations()
//    {
//        return allCombinations;
//    }
//
//    /**
//     *
//     * @param combination insert combination into table
//     */
//    void insert(TwoCardsCombinations combination)
//    {
//        CombinationsRoomDatabase.databaseWriteExecutor.execute(() -> {
//            twoCardsCombinationsDao.insert(combination);
//        }) ;
//    }
//}
