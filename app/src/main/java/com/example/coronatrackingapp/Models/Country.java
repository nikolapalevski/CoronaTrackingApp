package com.example.coronatrackingapp.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity
public class Country implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @SerializedName("country")
    private String countryName;
    @SerializedName("confirmed")
    private String confirmed;
    @SerializedName("recovered")
    private String recovered;
    @SerializedName("deaths")
    private String deaths;

    private Boolean isFavourite = false;

    @Ignore
    public Country() {
    }

    public Country(String confirmed, String recovered, String deaths) {
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.deaths = deaths;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public String toString() {
        return "Country{" +
                ", countryName='" + countryName + '\'' +
                ", confirmed='" + confirmed + '\'' +
                ", recovered='" + recovered + '\'' +
                ", deaths='" + deaths + '\'' +
                ", isFavourite=" + isFavourite +
                '}';
    }
}
