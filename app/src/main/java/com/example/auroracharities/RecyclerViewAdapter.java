package com.example.auroracharities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auroracharities.data.model.Charities;

import java.text.BreakIterator;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    private List<Charities> charitiesList;
    private ClickListener<Charities> clickListener;

    RecyclerViewAdapter(List<Charities> charitiesList){
        this.charitiesList = charitiesList;
    }

    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adapter_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.MyViewHolder holder, final int position) {

        final Charities charity = charitiesList.get(position);

        holder.title.setText(charity.getTitle());
        holder.image.setBackgroundResource(charity.getImage());
        holder.motto.setText(charity.getMotto());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(charity);
            }
        });


    }

    @Override
    public int getItemCount() {
        return charitiesList.size();
    }

    public void setOnItemClickListener(ClickListener<Charities> charityClickListener) {
        this.clickListener = charityClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView title;
        private ImageView image;
        private TextView motto;
        private CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            motto = itemView.findViewById(R.id.motto);
            cardView = itemView.findViewById(R.id.carView);
        }
    }
}

interface ClickListener<T> {
    void onItemClick(T data);
}