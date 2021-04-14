package com.example.coronatrackingapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.coronatrackingapp.Helpers.RecyclerAdapter;
import com.example.coronatrackingapp.R;
import com.example.coronatrackingapp.Utils.OnCountryClickListener;
import com.example.coronatrackingapp.Utils.OnFavouriteCountryClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllCountriesActivity extends AppCompatActivity implements OnCountryClickListener, OnFavouriteCountryClickListener {


    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<String> listFavouriteCountries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_countries);
        String[] countries = getIntent().getStringArrayExtra("countries");
        assert countries != null;
        List<String> countriesList = Arrays.asList(countries);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (countries != null) adapter = new RecyclerAdapter(this, countriesList, this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onCountryClick(String countryName) {
        Toast.makeText(this, "Clicked on " + countryName, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, SingleCountry.class);
        intent.putExtra("country", countryName);
        startActivity(intent);
    }

    @Override
    public void onFavouriteClick(String countryName) {
        Toast.makeText(this, "Favourite country is: " + countryName, Toast.LENGTH_SHORT).show();
        if (!listFavouriteCountries.contains(countryName)) {
            listFavouriteCountries.add(countryName);
        }
        Toast.makeText(this, listFavouriteCountries.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_country_recycler_view, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}