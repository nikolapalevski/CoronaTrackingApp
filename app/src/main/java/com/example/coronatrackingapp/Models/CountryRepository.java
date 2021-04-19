package com.example.coronatrackingapp.Models;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class CountryRepository<T> {
    private CountryDao countryDao;
    private LiveData<List<Country>> allCountries;

    private LiveData<List<Country>> favouriteCountries;

    public CountryRepository(Application application) {
        CountryDatabase database = CountryDatabase.getInstance(application);
        countryDao = database.countryDao();
        allCountries = countryDao.getAllNotes();

        favouriteCountries = countryDao.getAllFavourite();
    }

    public void insert(T country){
        new InsertCountryAsyncTask(countryDao).execute(country);

    }
    public void update(Country country){
        new UpdateCountryAsyncTask(countryDao).execute(country);

    }
    public void delete(Country country){
        new DeleteCountryAsyncTask(countryDao).execute(country);

    }
    public void deleteAllCountries(){
        new DeleteAllCountriesAsyncTask(countryDao).execute();
    }
    public LiveData<List<Country>> getAllCountries(){
        return allCountries;
    }

    public LiveData<List<Country>> getFavouriteCountries(){
        return favouriteCountries;
    }

    private static class InsertCountryAsyncTask<T> extends AsyncTask<T,Void,Void>{
        private CountryDao countryDao;

        private InsertCountryAsyncTask(CountryDao countryDao){
            this.countryDao = countryDao;
        }

        @Override
        protected Void doInBackground(T... params) {
            if (params[0] instanceof Country) {
                countryDao.insert((Country) params[0]);

            } else {
                countryDao.insert((List<Country>) params[0]);
            }
            return null;
        }
    }
    private static class UpdateCountryAsyncTask extends AsyncTask<Country,Void,Void>{
        private CountryDao countryDao;

        private UpdateCountryAsyncTask(CountryDao countryDao){
            this.countryDao = countryDao;
        }

        @Override
        protected Void doInBackground(Country... countries) {
            countryDao.update(countries[0]);
            return null;
        }
    }
    private static class DeleteCountryAsyncTask extends AsyncTask<Country,Void,Void>{
        private CountryDao countryDao;

        private DeleteCountryAsyncTask(CountryDao countryDao){
            this.countryDao = countryDao;
        }

        @Override
        protected Void doInBackground(Country... countries) {
            countryDao.delete(countries[0]);
            return null;
        }
    }
    private static class DeleteAllCountriesAsyncTask extends AsyncTask<Void,Void,Void>{
        private CountryDao countryDao;

        private DeleteAllCountriesAsyncTask(CountryDao countryDao){
            this.countryDao = countryDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            countryDao.deleteAllCountries();
            return null;
        }
    }

}
