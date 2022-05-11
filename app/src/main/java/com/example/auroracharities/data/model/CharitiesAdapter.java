package com.example.auroracharities.data.model;

import android.graphics.Color;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.example.auroracharities.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CharitiesAdapter extends FirestoreRecyclerAdapter<Charities, CharitiesAdapter.CharitiesAdapterVH> {

    private OnItemClickListener listener;
    private StorageReference storageReference;

    public CharitiesAdapter(
            @NonNull FirestoreRecyclerOptions<Charities> options)
    {
        super(options);
        storageReference = FirebaseStorage.getInstance().getReference();
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

        holder.image.setBackgroundColor(Color.WHITE);

        storageReference.child(model.getLogo()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Glide.with(holder.cardView.getContext())
                        .load(uri)
                        .into(holder.image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(holder.cardView.getContext(),"" + exception,  Toast.LENGTH_LONG);
            }
        });

    }

    @NonNull
    @Override
    public CharitiesAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_charity_view_adapter_layout, parent, false);
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
