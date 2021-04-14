package com.example.coronatrackingapp.Helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coronatrackingapp.Models.Country;
import com.example.coronatrackingapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapterFavouriteCountries extends RecyclerView.Adapter<RecyclerAdapterFavouriteCountries.CountryHolder> {

    private List<Country> countries = new ArrayList<>();

    @NonNull
    @Override
    public CountryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item_custom, parent, false);
        return new CountryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryHolder holder, int position) {
        Country currCountry = countries.get(position);
        holder.textViewCountry.setText(currCountry.getCountry());
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
        notifyDataSetChanged();
    }

    public Country getCountryAt(int position) {
        return countries.get(position);
    }


    class CountryHolder extends RecyclerView.ViewHolder {
        private TextView textViewCountry;

        public CountryHolder(@NonNull View itemView) {
            super(itemView);
            textViewCountry = itemView.findViewById(R.id.textViewAdapterItem);
        }
    }
}
