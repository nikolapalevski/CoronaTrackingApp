package com.example.coronatrackingapp.Models;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CountryViewModel<T> extends AndroidViewModel {

    private CountryRepository repository;
    private LiveData<List<Country>> allCountries;

    private LiveData<List<Country>> favouriteCountries;

    public CountryViewModel(@NonNull Application application) {
        super(application);
        repository = new CountryRepository(application);
        allCountries = repository.getAllCountries();

        favouriteCountries =repository.getFavouriteCountries();
    }

    public void insert(T country){
        repository.insert(country);
    }
    public void update(Country country){
        repository.update(country);
    }
    public void delete(Country country){
        repository.delete(country);
    }
    public void deleteAllCountries(){
        repository.deleteAllCountries();
    }
    public LiveData<List<Country>> getAllCountries(){
        return allCountries;
    }
    public LiveData<List<Country>> getFavouriteCountries(){
        return favouriteCountries;
    }


}
//view model factory