package com.example.coronatrackingapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.coronatrackingapp.Models.All;
import com.example.coronatrackingapp.Models.MyApi;
import com.example.coronatrackingapp.Models.SingletonRetrofit;
import com.example.coronatrackingapp.R;
import com.example.coronatrackingapp.databinding.ActivityMainBinding;

import java.util.Arrays;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private MyApi myApi;
    private String[] countries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.textViewInfo.setMovementMethod(new ScrollingMovementMethod());

        setupRetrofit();
        loadAllCountries();
    }

    private void setupRetrofit() {
        myApi = SingletonRetrofit.getRetrofit().create(MyApi.class);
    }

    public void searchFunc(View view) {
        String currCountry = binding.editTextCountry.getText().toString();
        binding.textViewInfo.setText("");

        Call<Map<String, All>> call = myApi.getSpecificCountry(currCountry);
        call.enqueue(new Callback<Map<String, All>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, All>> call, @NonNull Response<Map<String, All>> response) {
                Map<String, All> mapRegions = response.body();
                StringBuilder finalResult = new StringBuilder(currCountry + "\n\n");
                for (String keys : mapRegions.keySet()) {
//                    Log.i("KEYS", keys);
//                    Log.i("DATA", map.get(keys).getConfirmed());

                    finalResult.append(keys)
                            .append("\nConfirmed: ").append(mapRegions.get(keys).getConfirmed())
                            .append("\nRecovered: ").append(mapRegions.get(keys).getRecovered())
                            .append("\nDeaths: ").append(mapRegions.get(keys).getDeaths()).append("\n\n");
                }
                checkCountry(finalResult, currCountry);
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, All>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Error loading!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.editTextCountry.setText("");
        hideKeyboard();
    }

    public void checkCountry(StringBuilder finalResult, String currCountry) {
        if (Arrays.asList(countries).contains(currCountry)) {
            binding.textViewInfo.setText(finalResult);
        } else Toast.makeText(MainActivity.this, "Enter Valid Country", Toast.LENGTH_SHORT).show();
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputManager != null;
        inputManager.hideSoftInputFromWindow(binding.editTextCountry.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void loadAllCountries() {

        myApi.getAllCountriesAndRegions().enqueue(new Callback<Map<String, Map<String, All>>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Map<String, All>>> call, @NonNull Response<Map<String, Map<String, All>>> response) {
                Map<String, Map<String, All>> mapAllCountries = response.body();
                countries = mapAllCountries.keySet().toArray(new String[0]);

                //testing for iterating through map
                for (Map.Entry<String, Map<String, All>> drzava : mapAllCountries.entrySet()) {
                    Map<String, All> childMap = drzava.getValue();
                    for (Map.Entry<String, All> region : childMap.entrySet()) {
                        String imeRegion = region.getKey();
                        String childValue = region.getValue().getConfirmed();
                        Log.i("KEY", imeRegion);
                        Log.i("Value", childValue);
                    }
                }
                setUpAutoComplete();
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Map<String, All>>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.error_loading), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void startAllCountriesActivity(View view) {
        if (countries == null) return;
        Intent intent = new Intent(this, AllCountriesActivity.class);
        intent.putExtra("countries", countries);
        startActivity(intent);
    }

    public void setUpAutoComplete() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countries);
        binding.editTextCountry.setAdapter(adapter);
    }

    //TODO:room android jetpack, UI

}