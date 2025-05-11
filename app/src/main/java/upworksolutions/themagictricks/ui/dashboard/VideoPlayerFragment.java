package upworksolutions.themagictricks.ui.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import java.util.List;

import upworksolutions.themagictricks.R;
import upworksolutions.themagictricks.adapter.VideoAdapter;
import upworksolutions.themagictricks.model.VideoItem;

public class VideoPlayerFragment extends Fragment implements VideoAdapter.OnVideoClickListener {
    private YouTubePlayerView youTubePlayerView;
    private TextView videoTitleTextView;
    private TextView videoDescriptionTextView;
    private RecyclerView videosRecyclerView;
    private VideoAdapter videosAdapter;
    private ImageButton whatsappShareButton;
    private ImageButton facebookShareButton;
    private ImageButton twitterShareButton;
    private VideoItem video;
    private List<VideoItem> allVideos;
    private static final String APP_LINK = "https://play.google.com/store/apps/details?id=upworksolutions.themagictricks";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            video = getArguments().getParcelable("video");
        }
        loadAllVideos();
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
        
        getLifecycle().addObserver(youTubePlayerView);
        
        setupVideoPlayer();
        setupVideosList();
        setupShareButtons();
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

    private void setupVideosList() {
        videosAdapter = new VideoAdapter(new ArrayList<>(), this);
        videosRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        videosRecyclerView.setAdapter(videosAdapter);
        updateVideosList();
    }

    private void setupShareButtons() {
        whatsappShareButton.setOnClickListener(v -> shareToWhatsApp());
        facebookShareButton.setOnClickListener(v -> shareToFacebook());
        twitterShareButton.setOnClickListener(v -> shareToTwitter());
    }

    private void shareToWhatsApp() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");
            intent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing magic tricks app: " + APP_LINK);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "WhatsApp is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareToFacebook() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.setPackage("com.facebook.katana");
            intent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing magic tricks app: " + APP_LINK);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Facebook is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareToTwitter() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.setPackage("com.twitter.android");
            intent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing magic tricks app: " + APP_LINK);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Twitter is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateVideoInfo() {
        if (video != null) {
            videoTitleTextView.setText(video.getTitle());
            videoDescriptionTextView.setText(video.getDescription());
        }
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
            Type listType = new TypeToken<ArrayList<VideoItem>>(){}.getType();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            allVideos = gson.fromJson(jsonObject.get("videos"), listType);
        } catch (IOException e) {
            e.printStackTrace();
            allVideos = new ArrayList<>();
        }
    }

    private void updateVideosList() {
        if (allVideos != null) {
            videosAdapter.updateVideos(allVideos);
        }
    }

    @Override
    public void onVideoClick(VideoItem video) {
        this.video = video;
        updateVideoInfo();
        youTubePlayerView.getYouTubePlayerWhenReady(player -> {
            player.loadVideo(video.getId(), 0);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }
} 