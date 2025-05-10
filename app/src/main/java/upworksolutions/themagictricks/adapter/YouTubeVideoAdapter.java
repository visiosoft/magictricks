package upworksolutions.themagictricks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import upworksolutions.themagictricks.R;
import upworksolutions.themagictricks.model.YouTubeVideo;

import java.util.List;

public class YouTubeVideoAdapter extends RecyclerView.Adapter<YouTubeVideoAdapter.VideoViewHolder> {
    private List<YouTubeVideo> videos;
    private Context context;
    private boolean isGridView;
    private OnVideoClickListener listener;

    public interface OnVideoClickListener {
        void onVideoClick(YouTubeVideo video);
    }

    public YouTubeVideoAdapter(Context context, List<YouTubeVideo> videos, boolean isGridView, OnVideoClickListener listener) {
        this.context = context;
        this.videos = videos;
        this.isGridView = isGridView;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isGridView ? R.layout.item_video_grid : R.layout.item_video_list;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        YouTubeVideo video = videos.get(position);
        holder.titleTextView.setText(video.getTitle());
        holder.descriptionTextView.setText(video.getDescription());

        // Load thumbnail using Glide
        Glide.with(context)
            .load(video.getThumbnail())
            .centerCrop()
            .into(holder.thumbnailImageView);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onVideoClick(video);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos != null ? videos.size() : 0;
    }

    public void setViewType(boolean isGridView) {
        this.isGridView = isGridView;
        notifyDataSetChanged();
    }

    public void updateVideos(List<YouTubeVideo> newVideos) {
        this.videos = newVideos;
        notifyDataSetChanged();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView;
        TextView titleTextView;
        TextView descriptionTextView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
} 