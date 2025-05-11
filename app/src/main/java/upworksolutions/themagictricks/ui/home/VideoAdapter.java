package upworksolutions.themagictricks.ui.home;

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
    private OnVideoClickListener listener;

    public interface OnVideoClickListener {
        void onVideoClick(VideoItem video);
    }

    public VideoAdapter(List<VideoItem> videos, OnVideoClickListener listener) {
        this.videos = videos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
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

    public void updateVideos(List<VideoItem> newVideos) {
        this.videos = newVideos;
        notifyDataSetChanged();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;
        private TextView title;
        private TextView description;

        VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.video_thumbnail);
            title = itemView.findViewById(R.id.video_title);
            description = itemView.findViewById(R.id.video_description);
        }

        void bind(VideoItem video) {
            title.setText(video.getTitle());
            description.setText(video.getDescription());
            
            // Load thumbnail using Glide
            Glide.with(itemView.getContext())
                .load(video.getThumbnail())
                .placeholder(R.drawable.placeholder_trick)
                .error(R.drawable.placeholder_trick)
                .centerCrop()
                .into(thumbnail);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVideoClick(video);
                }
            });
        }
    }
} 