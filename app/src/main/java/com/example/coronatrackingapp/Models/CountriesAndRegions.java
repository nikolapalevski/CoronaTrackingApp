package com.example.coronatrackingapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class CountriesAndRegions {

    @SerializedName("All")
    @Expose
    private Map<String, Map<String, Country>> mapCountriesAndRegions;

    public Map<String, Map<String, Country>> getMapCountriesAndRegions() {
        return mapCountriesAndRegions;
    }

    public void setMapCountriesAndRegions(Map<String, Map<String, Country>> mapCountriesAndRegions) {
        this.mapCountriesAndRegions = mapCountriesAndRegions;
    }
}
