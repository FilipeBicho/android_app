//package com.filipebicho.poker;
//
//import android.app.Application;
//
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LiveData;
//
//import java.util.List;
//
///**
// * Provide data to the UI and survive configuration changes
// * @author filipe bicho created 02.04.2020
// */
//public class TwoCardsCombinationsViewModel extends AndroidViewModel {
//
//    //----- private instance variables
//
//    private TwoCardsCombinationsRepository repository;
//    private LiveData<List<TwoCardsCombinations>> allCombinations;
//
//    //----- public constructor
//
//    /**
//     *
//     * @param application application
//     */
//    public TwoCardsCombinationsViewModel(Application application) {
//        super(application);
//        repository = new TwoCardsCombinationsRepository(application);
//        allCombinations = repository.getAllCombinations();
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
//        repository.insert(combination);
//    }
//}
