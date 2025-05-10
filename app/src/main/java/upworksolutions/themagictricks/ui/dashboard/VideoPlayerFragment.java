package upworksolutions.themagictricks.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import upworksolutions.themagictricks.R;
import upworksolutions.themagictricks.model.YouTubeVideo;

public class VideoPlayerFragment extends Fragment {
    private YouTubePlayerView youTubePlayerView;
    private TextView videoTitleTextView;
    private TextView videoDescriptionTextView;
    private YouTubeVideo video;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            video = (YouTubeVideo) getArguments().getSerializable("video");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_player, container, false);
        
        youTubePlayerView = view.findViewById(R.id.youtubePlayerView);
        videoTitleTextView = view.findViewById(R.id.videoTitleTextView);
        videoDescriptionTextView = view.findViewById(R.id.videoDescriptionTextView);
        
        getLifecycle().addObserver(youTubePlayerView);
        
        setupVideoPlayer();
        updateVideoInfo();
        
        return view;
    }

    private void setupVideoPlayer() {
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                if (video != null) {
                    youTubePlayer.loadVideo(video.getId(), 0);
                }
            }
        });
    }

    private void updateVideoInfo() {
        if (video != null) {
            videoTitleTextView.setText(video.getTitle());
            videoDescriptionTextView.setText(video.getDescription());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }
} 