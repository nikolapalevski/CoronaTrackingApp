package com.example.coronatrackingapp.Models;

import android.os.AsyncTask;

public class UpdateFavouriteAsyncTask extends AsyncTask<Country, Void, Void> {
    private CountryDao countryDao;

    UpdateFavouriteAsyncTask(CountryDao countryDao) {
        this.countryDao = countryDao;
    }

    @Override
    protected Void doInBackground(Country... countries) {
        countryDao.updateFavourite(countries[0]);
        return null;
    }
}