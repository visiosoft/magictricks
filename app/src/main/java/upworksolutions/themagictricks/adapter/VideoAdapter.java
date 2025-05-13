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

import java.util.List;

import upworksolutions.themagictricks.R;
import upworksolutions.themagictricks.model.VideoItem;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<VideoItem> videos;
    private Context context;
    private boolean isGridView;
    private OnVideoClickListener listener;

    public interface OnVideoClickListener {
        void onVideoClick(VideoItem video);
    }

    public VideoAdapter(Context context, List<VideoItem> videos, boolean isGridView, OnVideoClickListener listener) {
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
        VideoItem video = videos.get(position);
        holder.bind(video);
    }

    @Override
    public int getItemCount() {
        return videos != null ? videos.size() : 0;
    }

    public void setViewType(boolean isGridView) {
        this.isGridView = isGridView;
        notifyDataSetChanged();
    }

    public void updateVideos(List<VideoItem> newVideos) {
        this.videos = newVideos;
        notifyDataSetChanged();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnailImageView;
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView viewsTextView;
        private TextView uploadDateTextView;

        VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            
            // These views might not exist in older layouts
            try {
                viewsTextView = itemView.findViewById(R.id.viewsTextView);
                uploadDateTextView = itemView.findViewById(R.id.uploadDateTextView);
            } catch (Exception e) {
                // Views not found, that's okay
            }
        }

        void bind(VideoItem video) {
            if (titleTextView != null) {
                titleTextView.setText(video.getTitle());
            }
            
            if (descriptionTextView != null) {
                descriptionTextView.setText(video.getDescription());
            }
            
            if (viewsTextView != null) {
                viewsTextView.setText(formatViews(video.getViews()));
            }
            
            if (uploadDateTextView != null) {
                uploadDateTextView.setText(video.getUploadDate());
            }
            
            // Load thumbnail using Glide
            if (thumbnailImageView != null) {
                Glide.with(context)
                    .load(video.getThumbnail())
                    .placeholder(R.drawable.placeholder_trick)
                    .error(R.drawable.placeholder_trick)
                    .centerCrop()
                    .into(thumbnailImageView);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVideoClick(video);
                }
            });
        }

        private String formatViews(long views) {
            if (views < 1000) {
                return views + " views";
            } else if (views < 1000000) {
                return String.format("%.1fK views", views / 1000.0);
            } else {
                return String.format("%.1fM views", views / 1000000.0);
            }
        }
    }
} 