package com.example.coronatrackingapp.Models;

import android.os.AsyncTask;

public class UpdateCountryAsyncTask extends AsyncTask<Country, Void, Void> {
    private CountryDao countryDao;
    private int countryId;

    UpdateCountryAsyncTask(CountryDao countryDao, int countryId) {
        this.countryDao = countryDao;
        this.countryId = countryId;
    }

    @Override
    protected Void doInBackground(Country... countries) {
        for (Country c :
                countries) {
            countryDao.update(c.getConfirmed(), c.getRecovered(), c.getDeaths(), countryId);
        }
        return null;
    }
}