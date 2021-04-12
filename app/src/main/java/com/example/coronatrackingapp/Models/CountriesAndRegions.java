package com.example.coronatrackingapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class CountriesAndRegions {

    @SerializedName("All")
    @Expose
    private Map<String, Map<String,All>> mapCountriesAndRegions;

    public Map<String, Map<String, All>> getMapCountriesAndRegions() {
        return mapCountriesAndRegions;
    }

    public void setMapCountriesAndRegions(Map<String, Map<String, All>> mapCountriesAndRegions) {
        this.mapCountriesAndRegions = mapCountriesAndRegions;
    }
}
