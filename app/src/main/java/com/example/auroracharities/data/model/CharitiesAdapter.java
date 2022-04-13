package com.example.auroracharities.data.model;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auroracharities.CharityHomeActivity;
import com.example.auroracharities.PublicMainActivity;
import com.example.auroracharities.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class CharitiesAdapter extends FirestoreRecyclerAdapter<Charities, CharitiesAdapter.CharitiesAdapterVH>{

    private OnItemClickListener listener;

    public CharitiesAdapter(
            @NonNull FirestoreRecyclerOptions<Charities> options)
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
    protected void onBindViewHolder(@NonNull CharitiesAdapterVH holder, int position, @NonNull Charities model) {
        //Log.v("CharitiesAdapter", model.getTitle());
        holder.title.setText(model.getTitle());

        holder.motto.setText(model.getMotto());

        holder.image.setBackgroundResource(R.drawable.a4g_logo_background);
    }

    @NonNull
    @Override
    public CharitiesAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adapter_layout, parent, false);
        return new CharitiesAdapter.CharitiesAdapterVH(view);
    }

    public class CharitiesAdapterVH extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView image;
        private TextView motto;
        private CardView cardView;

        public CharitiesAdapterVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            motto = itemView.findViewById(R.id.motto);
            cardView = itemView.findViewById(R.id.carView);

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
