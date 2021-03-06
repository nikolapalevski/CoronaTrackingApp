package com.example.coronatrackingapp.Helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coronatrackingapp.Models.Country;
import com.example.coronatrackingapp.R;
import com.example.coronatrackingapp.Utils.OnCountryDBClickListener;
import com.example.coronatrackingapp.Utils.OnCountryFavouriteClickListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapterFavouriteCountries extends RecyclerView.Adapter<RecyclerAdapterFavouriteCountries.CountryHolder> {

    private List<Country> countries = new ArrayList<>();

    private final OnCountryDBClickListener onClickListener;
    private final OnCountryFavouriteClickListener onCountryFavouriteClickListener;

    public RecyclerAdapterFavouriteCountries(OnCountryDBClickListener listener, OnCountryFavouriteClickListener onCountryFavouriteClickListener) {
        this.onClickListener = listener;
        this.onCountryFavouriteClickListener = onCountryFavouriteClickListener;
    }

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
        holder.textViewCountry.setText(currCountry.getCountryName());
        holder.textViewCountry.setOnClickListener(view -> onClickListener.onCountryClick(currCountry));
        if (currCountry.isFavourite()) {
            holder.imageViewFavourite.setImageResource(R.drawable.ic_favorite_red);
        } else {
            holder.imageViewFavourite.setImageResource(R.drawable.ic_favourite_white);
        }

        holder.imageViewFavourite.setOnClickListener(view -> {

            if (!currCountry.isFavourite()) {
                holder.imageViewFavourite.setImageResource(R.drawable.ic_favorite_red);
            } else {
                holder.imageViewFavourite.setImageResource(R.drawable.ic_favourite_white);
            }
            onCountryFavouriteClickListener.onCountryFavouriteClick(currCountry);


        });
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
        private ImageView imageViewFavourite;

        public CountryHolder(@NonNull View itemView) {
            super(itemView);
            textViewCountry = itemView.findViewById(R.id.textViewAdapterItem);
            imageViewFavourite = itemView.findViewById(R.id.favouriteButton);
        }
    }
}
