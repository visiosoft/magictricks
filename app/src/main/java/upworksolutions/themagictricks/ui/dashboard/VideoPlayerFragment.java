package upworksolutions.themagictricks.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import upworksolutions.themagictricks.R;
import upworksolutions.themagictricks.adapter.VideoAdapter;
import upworksolutions.themagictricks.model.Video;
import upworksolutions.themagictricks.model.VideoItem;

public class VideoPlayerFragment extends Fragment implements VideoAdapter.OnVideoClickListener {
    private static final String TAG = "VideoPlayerFragment";
    private YouTubePlayerView youTubePlayerView;
    private TextView videoTitleTextView;
    private TextView videoDescriptionTextView;
    private RecyclerView videosRecyclerView;
    private VideoAdapter videosAdapter;
    private ImageButton whatsappShareButton;
    private ImageButton facebookShareButton;
    private ImageButton twitterShareButton;
    private ProgressBar loadingIndicator;
    private VideoItem video;
    private List<VideoItem> allVideos;
    private static final String APP_LINK = "https://play.google.com/store/apps/details?id=upworksolutions.themagictricks";
    private static final int MAX_RANDOM_VIDEOS = 5;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadAllVideos();
        if (getArguments() != null) {
            String videoId = getArguments().getString("videoId");
            String videoTitle = getArguments().getString("videoTitle");
            String videoUrl = getArguments().getString("videoUrl");
            int categoryId = getArguments().getInt("categoryId", 0);
            String categoryName = getArguments().getString("categoryName", "");
            String description = getArguments().getString("description", "");
            String thumbnail = getArguments().getString("thumbnail", "");
            long views = getArguments().getLong("views", 0);
            String uploadDate = getArguments().getString("uploadDate", "");

            if (videoId != null && videoTitle != null && videoUrl != null) {
                video = new VideoItem(
                    videoId,
                    videoTitle,
                    description,
                    thumbnail,
                    videoUrl,
                    categoryId,
                    categoryName,
                    views,
                    uploadDate
                );
                Log.d(TAG, "Created video with category ID: " + categoryId);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_player, container, false);
        
        youTubePlayerView = view.findViewById(R.id.youtubePlayerView);
        videoTitleTextView = view.findViewById(R.id.videoTitleTextView);
        videoDescriptionTextView = view.findViewById(R.id.videoDescriptionTextView);
        videosRecyclerView = view.findViewById(R.id.relatedVideosRecyclerView);
        whatsappShareButton = view.findViewById(R.id.whatsappShareButton);
        facebookShareButton = view.findViewById(R.id.facebookShareButton);
        twitterShareButton = view.findViewById(R.id.twitterShareButton);
        loadingIndicator = view.findViewById(R.id.loadingIndicator);
        
        getLifecycle().addObserver(youTubePlayerView);
        
        setupVideoPlayer();
        setupVideosList();
        setupShareButtons();
        updateVideoInfo();
        
        return view;
    }

    private void setupVideoPlayer() {
        showLoading();
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                if (video != null) {
                    youTubePlayer.loadVideo(video.getId(), 0);
                    hideLoading();
                }
            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, 
                com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState state) {
                if (state == com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState.ENDED) {
                    hideLoading();
                }
            }
        });
    }

    private void setupVideosList() {
        videosRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        videosAdapter = new VideoAdapter(requireContext(), new ArrayList<>(), false, this);
        videosRecyclerView.setAdapter(videosAdapter);
    }

    private void loadAllVideos() {
        try {
            InputStream is = requireContext().getAssets().open("youtube_videos.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            Type listType = new TypeToken<ArrayList<Video>>(){}.getType();
            List<Video> videos = gson.fromJson(jsonObject.get("videos"), listType);

            if (videos != null) {
                allVideos = new ArrayList<>();
                for (Video v : videos) {
                    allVideos.add(new VideoItem(
                        v.getId(),
                        v.getTitle(),
                        v.getDescription(),
                        v.getThumbnailUrl(),
                        v.getVideoUrl(),
                        v.getCategoryId(),
                        getCategoryName(v.getCategoryId()),
                        v.getViews(),
                        v.getUploadDate()
                    ));
                }
                Log.d(TAG, "Loaded " + allVideos.size() + " videos");
            }
        } catch (IOException e) {
            Log.e(TAG, "Error loading videos", e);
        }
    }

    private String getCategoryName(int categoryId) {
        switch (categoryId) {
            case 1:
                return "Card Magic";
            case 2:
                return "Coin Magic";
            case 3:
                return "Rope Magic";
            case 4:
                return "Mentalism";
            case 5:
                return "Street Magic";
            default:
                return "Other";
        }
    }

    private void updateVideoInfo() {
        if (video != null) {
            videoTitleTextView.setText(video.getTitle());
            videoDescriptionTextView.setText(video.getDescription());
            
            // Update related videos
            List<VideoItem> randomVideos = getRandomVideos();
            Log.d(TAG, "Selected " + randomVideos.size() + " random videos");
            videosAdapter.updateVideos(randomVideos);
        }
    }

    private List<VideoItem> getRandomVideos() {
        List<VideoItem> randomVideos = new ArrayList<>();
        if (allVideos != null && video != null) {
            // Create a copy of all videos
            List<VideoItem> availableVideos = new ArrayList<>(allVideos);
            // Remove the current video
            availableVideos.removeIf(v -> v.getId().equals(video.getId()));
            
            // Shuffle the list
            Collections.shuffle(availableVideos);
            
            // Get up to MAX_RANDOM_VIDEOS videos
            int count = Math.min(MAX_RANDOM_VIDEOS, availableVideos.size());
            randomVideos.addAll(availableVideos.subList(0, count));
        }
        return randomVideos;
    }

    private void setupShareButtons() {
        whatsappShareButton.setOnClickListener(v -> shareVideo("whatsapp"));
        facebookShareButton.setOnClickListener(v -> shareVideo("facebook"));
        twitterShareButton.setOnClickListener(v -> shareVideo("twitter"));
    }

    private void shareVideo(String platform) {
        String shareText = "Check out this amazing magic trick: " + video.getTitle() + "\n" + APP_LINK;
        // Implement platform-specific sharing
        Toast.makeText(requireContext(), "Sharing to " + platform, Toast.LENGTH_SHORT).show();
    }

    private void showLoading() {
        loadingIndicator.setVisibility(View.VISIBLE);
        Animation fadeIn = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in);
        loadingIndicator.startAnimation(fadeIn);
    }

    private void hideLoading() {
        Animation fadeOut = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_out);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                loadingIndicator.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        loadingIndicator.startAnimation(fadeOut);
    }

    @Override
    public void onVideoClick(VideoItem video) {
        this.video = video;
        updateVideoInfo();
        setupVideoPlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }
} 