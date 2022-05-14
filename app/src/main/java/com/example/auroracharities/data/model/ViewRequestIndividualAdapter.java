package com.example.auroracharities.data.model;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auroracharities.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class ViewRequestIndividualAdapter extends FirestoreRecyclerAdapter<EditRequest, ViewRequestIndividualAdapter.ViewRequestIndividualAdapterVH> {

    private OnItemClickListener listener;

    public ViewRequestIndividualAdapter(
            @NonNull FirestoreRecyclerOptions<EditRequest> options)
    {
        super(options);
    }

    public void onError(FirebaseFirestoreException e) {
        Log.wtf("AHHH", "I dunno.");
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewRequestIndividualAdapterVH holder, int position, @NonNull EditRequest model) {
        holder.name.setText(model.getName());
        holder.description.setText(model.getDescription());
        holder.charity.setText(model.getCharity());
        holder.chipGroup.removeAllViewsInLayout();
        if (model.getCategoriesTag() != null) {
            for(String i : model.getCategoriesTag()){
                Chip chip = new Chip(holder.name.getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0,0,0,0);
                chip.setLayoutParams(layoutParams);
                chip.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                chip.setClickable(false);
                chip.setCheckable(false);
                chip.setText(i);
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#7249A5")));
                holder.chipGroup.addView(chip);
            }
        }

        if (model.getAgeTag() != null) {
            for(String i : model.getAgeTag()){
                Chip chip = new Chip(holder.name.getContext());
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0,0,0,0);
                chip.setLayoutParams(layoutParams);
                chip.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                chip.setClickable(false);
                chip.setCheckable(false);
                chip.setText(i);
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#3FA67A")));
                holder.chipGroup.addView(chip);
            }
        }

        if (model.getSizeTag() != null) {
            for(String i : model.getSizeTag()){
                Chip chip = new Chip(holder.name.getContext());
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0,0,0,0);
                chip.setLayoutParams(layoutParams);
                chip.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                chip.setClickable(false);
                chip.setCheckable(false);
                chip.setText(i);
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#AA065A")));
                holder.chipGroup.addView(chip);
            }
        }

        if (model.getConditionTag() != null) {
            for(String i : model.getConditionTag()){
                Chip chip = new Chip(holder.name.getContext());
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0,0,0,0);
                chip.setLayoutParams(layoutParams);
                chip.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                chip.setClickable(false);
                chip.setCheckable(false);
                chip.setText(i);
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#5196BC")));
                holder.chipGroup.addView(chip);
            }
        }
    }

    @NonNull
    @Override
    public ViewRequestIndividualAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_request_individual_adapter_layout, parent, false);
        return new ViewRequestIndividualAdapter.ViewRequestIndividualAdapterVH(view);
    }

    public class ViewRequestIndividualAdapterVH extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView description;
        private TextView charity;
        private CardView cardView;
        private ChipGroup chipGroup;

        public ViewRequestIndividualAdapterVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.viewRequestIndividualName);
            description = itemView.findViewById(R.id.viewRequestIndividualDescription);
            charity = itemView.findViewById(R.id.viewRequestIndividualCharity);
            cardView = itemView.findViewById(R.id.viewRequestIndividualCardView);
            chipGroup = itemView.findViewById(R.id.chipGroupIndividual);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}

