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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllCountriesDatabaseActivity extends AppCompatActivity implements OnCountryDBClickListener, OnCountryFavouriteClickListener {

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
        countryViewModel.getAllCountries().observe(this, (Observer<List<Country>>) countries -> {
            //update RecyclerView
            adapter.setCountries(countries);
        });


        //for delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                countryViewModel.delete(adapter.getCountryAt(viewHolder.getAdapterPosition()));
                Toast.makeText(AllCountriesDatabaseActivity.this, "Country deleted!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);


    }

    @Override
    public void onCountryClick(Country country) {

        Intent intent = new Intent(this, SingleCountryActivity.class);
        intent.putExtra(Constants.COUNTRY_EXTRA, country.getCountryName());
        intent.putExtra(Constants.CONFIRMED_EXTRA, country.getConfirmed());
        intent.putExtra(Constants.RECOVERED_EXTRA, country.getRecovered());
        intent.putExtra(Constants.DEATHS_EXTRA, country.getDeaths());
        startActivity(intent);

    }

    @Override
    public void onCountryFavouriteClick(Country country) {

        if (!country.isFavourite()) {
            country.setFavourite(true);
            Toast.makeText(this, country.getCountryName() + getString(R.string.added_country), Toast.LENGTH_SHORT).show();
        } else {
            country.setFavourite(false);
            Toast.makeText(this, country.getCountryName() + getString(R.string.removed_country), Toast.LENGTH_SHORT).show();
        }
        countryViewModel.updateFavourite(country);

    }

}