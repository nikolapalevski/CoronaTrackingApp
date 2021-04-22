package com.example.coronatrackingapp.Models;

import android.os.AsyncTask;

public class DeleteAllCountriesAsyncTask extends AsyncTask<Void, Void, Void> {
    private CountryDao countryDao;

    DeleteAllCountriesAsyncTask(CountryDao countryDao) {
        this.countryDao = countryDao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        countryDao.deleteAllCountries();
        return null;
    }
}