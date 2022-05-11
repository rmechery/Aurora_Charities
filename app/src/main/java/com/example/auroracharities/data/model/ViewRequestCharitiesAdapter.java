package com.example.auroracharities.data.model;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auroracharities.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class ViewRequestCharitiesAdapter extends FirestoreRecyclerAdapter<EditRequest, ViewRequestCharitiesAdapter.ViewRequestCharitiesAdapterVH> {

    private OnItemClickListener listener;

    public ViewRequestCharitiesAdapter(
            @NonNull FirestoreRecyclerOptions<EditRequest> options)
    {
        super(options);
    }

    public void onError(FirebaseFirestoreException e) {
        // Called when there is an error getting a query snapshot. You may want to update
        // your UI to display an error message to the user.
        // ...
        Log.wtf("AHHH", "I dunno.");
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewRequestCharitiesAdapterVH holder, int position, @NonNull EditRequest model) {
        //Log.v("CharitiesAdapter", model.getTitle());
        //holder.title.setText(model.getTitle());
        holder.name.setText(model.getName());
        holder.charity.setText(model.getCharity());
        holder.description.setText(model.getDescription());
    }

    @NonNull
    @Override
    public ViewRequestCharitiesAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_request_adapter_layout, parent, false);
        return new ViewRequestCharitiesAdapter.ViewRequestCharitiesAdapterVH(view);
    }

    public class ViewRequestCharitiesAdapterVH extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView charity;
        private TextView description;
        private CardView cardView;
        private Button editBtn;

        public ViewRequestCharitiesAdapterVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.viewRequestName);
            charity = itemView.findViewById(R.id.viewRequestCharity);
            description = itemView.findViewById(R.id.viewRequestDescription);
            cardView = itemView.findViewById(R.id.viewRequestCardView);
            editBtn = itemView.findViewById(R.id.viewRequestEditButton);

            editBtn.setOnClickListener(new View.OnClickListener() {
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

