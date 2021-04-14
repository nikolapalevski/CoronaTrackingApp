package com.example.coronatrackingapp.Activities;

import android.os.Bundle;
import android.widget.Toast;

import com.example.coronatrackingapp.Helpers.RecyclerAdapterFavouriteCountries;
import com.example.coronatrackingapp.Models.Country;
import com.example.coronatrackingapp.Models.CountryViewModel;
import com.example.coronatrackingapp.R;
import com.example.coronatrackingapp.Utils.OnFavouriteCountryClickListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavouriteCountriesActivity extends AppCompatActivity implements OnFavouriteCountryClickListener {

    private CountryViewModel countryViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_countries);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewFavourites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        RecyclerAdapterFavouriteCountries adapter = new RecyclerAdapterFavouriteCountries();
        recyclerView.setAdapter(adapter);

        countryViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(CountryViewModel.class);
        countryViewModel.getAllCountries().observe(this, new Observer<List<Country>>() {
            @Override
            public void onChanged(List<Country> countries) {
                //update RecyclerView
                adapter.setCountries(countries);
            }
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
                Toast.makeText(FavouriteCountriesActivity.this, "Country deleted!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        Country country = new Country("MK");
        countryViewModel.insert(country);

    }

    @Override
    public void onFavouriteClick(String countryName) {
        Country country = new Country(countryName);
        countryViewModel.insert(country);
    }
}