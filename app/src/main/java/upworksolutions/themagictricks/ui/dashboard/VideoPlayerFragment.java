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
import upworksolutions.themagictricks.adapter.YouTubeVideoAdapter;
import upworksolutions.themagictricks.model.YouTubeVideo;

public class VideoPlayerFragment extends Fragment implements YouTubeVideoAdapter.OnVideoClickListener {
    private YouTubePlayerView youTubePlayerView;
    private TextView videoTitleTextView;
    private TextView videoDescriptionTextView;
    private RecyclerView videosRecyclerView;
    private YouTubeVideoAdapter videosAdapter;
    private ImageButton whatsappShareButton;
    private ImageButton facebookShareButton;
    private ImageButton twitterShareButton;
    private YouTubeVideo video;
    private List<YouTubeVideo> allVideos;
    private static final String APP_LINK = "https://play.google.com/store/apps/details?id=upworksolutions.themagictricks";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            video = (YouTubeVideo) getArguments().getSerializable("video");
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
        videosAdapter = new YouTubeVideoAdapter(requireContext(), new ArrayList<>(), false, this);
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
            // If Facebook app is not installed, open in browser
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sharer/sharer.php?u=" + APP_LINK));
            startActivity(browserIntent);
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
            // If Twitter app is not installed, open in browser
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=Check out this amazing magic tricks app: " + APP_LINK));
            startActivity(browserIntent);
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
            Type listType = new TypeToken<ArrayList<YouTubeVideo>>(){}.getType();
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
    public void onVideoClick(YouTubeVideo video) {
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