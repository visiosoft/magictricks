package upworksolutions.themagictricks.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import upworksolutions.themagictricks.R;
import upworksolutions.themagictricks.model.Trick;

public class TrickAdapter extends RecyclerView.Adapter<TrickAdapter.TrickViewHolder> {
    private List<Trick> tricks;
    private OnTrickClickListener listener;

    public interface OnTrickClickListener {
        void onTrickClick(Trick trick);
    }

    public TrickAdapter(List<Trick> tricks, OnTrickClickListener listener) {
        this.tricks = tricks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrickViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trick, parent, false);
        return new TrickViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrickViewHolder holder, int position) {
        Trick trick = tricks.get(position);
        holder.bind(trick);
    }

    @Override
    public int getItemCount() {
        return tricks != null ? tricks.size() : 0;
    }

    public void updateTricks(List<Trick> newTricks) {
        this.tricks = newTricks;
        notifyDataSetChanged();
    }

    class TrickViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;
        private TextView title;
        private TextView difficulty;
        private TextView views;

        TrickViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.trick_thumbnail);
            title = itemView.findViewById(R.id.trick_title);
            difficulty = itemView.findViewById(R.id.trick_difficulty);
            views = itemView.findViewById(R.id.trick_views);
        }

        void bind(Trick trick) {
            title.setText(trick.getTitle());
            difficulty.setText(trick.getDifficulty());
            views.setText(trick.getViews() + " views");
            // TODO: Load thumbnail using Glide or Picasso
            // For now, use a placeholder
            thumbnail.setImageResource(R.drawable.placeholder_trick);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTrickClick(trick);
                }
            });
        }
    }
} 