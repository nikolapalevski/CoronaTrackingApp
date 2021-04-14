package com.example.coronatrackingapp.Models;

import com.google.gson.annotations.SerializedName;

public class Region {

    @SerializedName("confirmed")
    private String confirmed;
    @SerializedName("recovered")
    private String recovered;
    @SerializedName("deaths")
    private String deaths;

    public Region(String confirmed, String recovered, String deaths) {
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
}
