package com.example.and_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.and_project.data.Event;
import com.example.and_project.data.FirebaseRepository;

import java.util.ArrayList;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private ArrayList<Event> events;
    final private OnListItemClickListener listener;
    final private FirebaseRepository repository;

    EventListAdapter(ArrayList<Event> events, OnListItemClickListener listener){
        this.events = events;
        this.listener = listener;
        repository = FirebaseRepository.getInstance();
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.event_list_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.title.setText(events.get(position).getTitle());
        viewHolder.time.setText(events.get(position).getDateTime());
        // There seems to be an error when it reloads the saved positions for events
        // and images are shifted and end up on the wrong event.
        // Removing the images is kind of a hack
        if (!events.get(position).hasImage())
            viewHolder.image.setImageDrawable(null);

        if (events.get(position).hasImage()) {
            // Images should probably be references in firestore on the events objects
            // That was I wouldn't have to keep downloading them like this
            repository.getImage(events.get(position).getId()).addOnCompleteListener(value -> {
                if (value.isSuccessful()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(value.getResult(), 0, value.getResult().length, options);
                    viewHolder.image.setImageBitmap(bitmap);
                }
            });
        }
    }

    public int getItemCount() {
        return events.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView time;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.rvName);
            time = itemView.findViewById(R.id.rvTime);
            image = itemView.findViewById(R.id.rvImage);
            image.setClipToOutline(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onListItemClick(getAdapterPosition());
        }
    }

    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
