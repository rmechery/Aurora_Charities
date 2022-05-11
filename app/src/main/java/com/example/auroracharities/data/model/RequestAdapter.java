package com.example.auroracharities.data.model;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBindings;

import com.example.auroracharities.IndividualCharityPageActivity;
import com.example.auroracharities.PublicMainActivity;
import com.example.auroracharities.R;
import com.example.auroracharities.databinding.AlgoliaSearchRowLayoutBinding;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {
    private ArrayList<RequestAlgolia> oldData;
    private int itemLayout;
    private String TAG = "RequestAdapter";
    private OnItemClickListener listener;

    public RequestAdapter(){
        this.oldData = new ArrayList<RequestAlgolia>();
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public RequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          MyViewHolder holder = new MyViewHolder(
                  AlgoliaSearchRowLayoutBinding.inflate(
                          LayoutInflater.from(parent.getContext()), parent, false));
//        RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(requestActi );

        holder.algoliaSearchRowLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null && holder.getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION){
                    Log.v(TAG, "position = " + holder.getAbsoluteAdapterPosition());
                    listener.onItemClick(holder.getAbsoluteAdapterPosition());
                }
            }
        });
        return holder;
    };

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RequestAlgolia modal = oldData.get(position);
        //holder.algoliaSearchRowLayoutBinding.algoliaCharityName.setText(modal.getCharity());
        holder.algoliaSearchRowLayoutBinding.algoliaRequestName.setText(modal.getName());
        holder.algoliaSearchRowLayoutBinding.algoliaRequestCharity.setText(modal.getCharity());
        holder.algoliaSearchRowLayoutBinding.algoliaRequestDescription.setText(modal.getDescription());
        String[] myList = {"A", "B"};
        holder.algoliaSearchRowLayoutBinding.chipGroup.removeAllViews();

        if (modal.getCategoriesTag() != null) {
            for(String i : modal.getCategoriesTag()){
                Chip chip = new Chip(holder.algoliaSearchRowLayoutBinding.chipGroup.getContext());
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0,0,0,0);
                chip.setLayoutParams(layoutParams);
                chip.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                chip.setClickable(false);
                chip.setCheckable(false);
                chip.setText(i);
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#7249A5")));
                holder.algoliaSearchRowLayoutBinding.chipGroup.addView(chip);
            }
        }

        if (modal.getAgeTag() != null) {
            for(String i : modal.getAgeTag()){
                Chip chip = new Chip(holder.algoliaSearchRowLayoutBinding.chipGroup.getContext());
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0,0,0,0);
                chip.setLayoutParams(layoutParams);
                chip.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                chip.setClickable(false);
                chip.setCheckable(false);
                chip.setText(i);
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#3FA67A")));
                holder.algoliaSearchRowLayoutBinding.chipGroup.addView(chip);
            }
        }

        if (modal.getSizeTag() != null) {
            for(String i : modal.getSizeTag()){
                Chip chip = new Chip(holder.algoliaSearchRowLayoutBinding.chipGroup.getContext());
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0,0,0,0);
                chip.setLayoutParams(layoutParams);
                chip.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                chip.setClickable(false);
                chip.setCheckable(false);
                chip.setText(i);
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#AA065A")));
                holder.algoliaSearchRowLayoutBinding.chipGroup.addView(chip);
            }
        }

        if (modal.getConditionTag() != null) {
            for(String i : modal.getConditionTag()){
                Chip chip = new Chip(holder.algoliaSearchRowLayoutBinding.chipGroup.getContext());
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0,0,0,0);
                chip.setLayoutParams(layoutParams);
                chip.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                chip.setClickable(false);
                chip.setCheckable(false);
                chip.setText(i);
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#5196BC")));
                holder.algoliaSearchRowLayoutBinding.chipGroup.addView(chip);
            }
        }

    }

    @Override
    public int getItemCount() {
        return oldData.size();
    }

    public ArrayList<RequestAlgolia> getOldData(){
        return oldData;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final AlgoliaSearchRowLayoutBinding algoliaSearchRowLayoutBinding;

        public MyViewHolder(AlgoliaSearchRowLayoutBinding algoliaSearchRowLayoutBinding) {
            super(algoliaSearchRowLayoutBinding.getRoot());
            this.algoliaSearchRowLayoutBinding = algoliaSearchRowLayoutBinding;

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<RequestAlgolia> newData){
        oldData = newData;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(RequestAdapter.OnItemClickListener listener){
        this.listener = (OnItemClickListener) listener;
    }



}
