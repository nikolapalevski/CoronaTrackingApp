package com.example.coronatrackingapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.coronatrackingapp.Helpers.RecyclerAdapter;
import com.example.coronatrackingapp.R;
import com.example.coronatrackingapp.Utils.OnCountryClickListener;

import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllCountriesActivity extends AppCompatActivity implements OnCountryClickListener {


    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_countries);
        String [] countries = getIntent().getStringArrayExtra("countries");
        assert countries != null;
        List<String> countriesList = Arrays.asList(countries);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (countries != null) adapter = new RecyclerAdapter(this, countriesList);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onCountryClick(String countryName) {
        Toast.makeText(this, "Clicked on " + countryName, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, SingleCountry.class);
        intent.putExtra("country", countryName);
        startActivity(intent);

    }
}