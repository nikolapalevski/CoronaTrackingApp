package com.example.coronatrackingapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.coronatrackingapp.Helpers.RecyclerAdapterFavouriteCountries;
import com.example.coronatrackingapp.Models.Country;
import com.example.coronatrackingapp.Models.CountryViewModel;
import com.example.coronatrackingapp.R;
import com.example.coronatrackingapp.Utils.Constants;
import com.example.coronatrackingapp.Utils.OnCountryDBClickListener;
import com.example.coronatrackingapp.Utils.OnCountryFavouriteClickListener;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavouriteCountriesTestActivity extends AppCompatActivity implements OnCountryDBClickListener, OnCountryFavouriteClickListener {
    private CountryViewModel countryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favourite_countries);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewFavourites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        RecyclerAdapterFavouriteCountries adapter = new RecyclerAdapterFavouriteCountries(this, this);
        recyclerView.setAdapter(adapter);

        countryViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(CountryViewModel.class);
        countryViewModel.getFavouriteCountries().observe(this, (Observer<List<Country>>) countries -> {
            //update RecyclerView
            adapter.setCountries(countries);
        });
    }

    @Override
    public void onCountryClick(Country country) {
        Intent intent = new Intent(this, SingleCountryActivity.class);
        intent.putExtra(Constants.COUNTRY_EXTRA, country);
        startActivity(intent);
    }

    @Override
    public void onCountryFavouriteClick(Country country) {

        country.setFavourite(false);
        countryViewModel.updateFavourite(country);
        Toast.makeText(this, country.getCountryName() + getString(R.string.removed_country), Toast.LENGTH_SHORT).show();
    }
}