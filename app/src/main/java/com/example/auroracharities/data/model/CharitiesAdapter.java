package com.example.auroracharities.data.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.InputStream;

public class CharitiesAdapter extends FirestoreRecyclerAdapter<Charities, CharitiesAdapter.CharitiesAdapterVH> {

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

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView=imageView;
            //Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...",Toast.LENGTH_SHORT).show();
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage=BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull CharitiesAdapterVH holder, int position, @NonNull Charities model) {
        //Log.v("CharitiesAdapter", model.getTitle());
        holder.title.setText(model.getTitle());

        holder.motto.setText(model.getMotto());

        holder.image.setBackgroundColor(Color.WHITE);

        new DownloadImageFromInternet(holder.image).execute(model.getLogo());
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
