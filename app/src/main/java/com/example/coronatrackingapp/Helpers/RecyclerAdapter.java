package com.example.coronatrackingapp.Helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coronatrackingapp.R;
import com.example.coronatrackingapp.Utils.OnCountryClickListener;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final OnCountryClickListener onClickListener;

    private final List<String> data;

    public RecyclerAdapter(OnCountryClickListener listener, List<String> data) {
        this.onClickListener = listener;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycle_item_custom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        holder.textViewCountry.setText(data.get(position));
        holder.textViewCountry.setOnClickListener(view -> onClickListener.onCountryClick(data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCountry;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCountry = itemView.findViewById(R.id.textViewAdapterItem);
        }
    }
}
