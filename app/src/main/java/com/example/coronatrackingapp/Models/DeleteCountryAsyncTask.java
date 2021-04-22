package com.example.coronatrackingapp.Models;

import android.os.AsyncTask;

public class DeleteCountryAsyncTask extends AsyncTask<Country, Void, Void> {
    private CountryDao countryDao;

    DeleteCountryAsyncTask(CountryDao countryDao) {
        this.countryDao = countryDao;
    }

    @Override
    protected Void doInBackground(Country... countries) {
        countryDao.delete(countries[0]);
        return null;
    }
}
