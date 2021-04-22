package com.example.coronatrackingapp.Models;

import android.os.AsyncTask;

public class InsertCountryAsyncTask extends AsyncTask<Country, Void, Void> {
    private CountryDao countryDao;

    InsertCountryAsyncTask(CountryDao countryDao) {
        this.countryDao = countryDao;
    }

    @Override
    protected Void doInBackground(Country... params) {

        countryDao.insert(params[0]);

        return null;
    }
}