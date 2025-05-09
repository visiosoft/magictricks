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
import upworksolutions.themagictricks.model.Video;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<Video> videos;
    private OnVideoClickListener listener;

    public interface OnVideoClickListener {
        void onVideoClick(Video video);
    }

    public VideoAdapter(List<Video> videos, OnVideoClickListener listener) {
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
        Video video = videos.get(position);
        holder.bind(video);
    }

    @Override
    public int getItemCount() {
        return videos != null ? videos.size() : 0;
    }

    public void updateVideos(List<Video> newVideos) {
        this.videos = newVideos;
        notifyDataSetChanged();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;
        private TextView title;
        private TextView duration;
        private TextView views;

        VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.video_thumbnail);
            title = itemView.findViewById(R.id.video_title);
            duration = itemView.findViewById(R.id.video_duration);
            views = itemView.findViewById(R.id.video_views);
        }

        void bind(Video video) {
            title.setText(video.getTitle());
            duration.setText(video.getDuration());
            views.setText(video.getViews() + " views");
            // TODO: Load thumbnail using Glide or Picasso
            // For now, use a placeholder
            thumbnail.setImageResource(R.drawable.placeholder_trick);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVideoClick(video);
                }
            });
        }
    }
} 