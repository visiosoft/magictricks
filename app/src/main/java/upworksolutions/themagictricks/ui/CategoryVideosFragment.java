package upworksolutions.themagictricks.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import upworksolutions.themagictricks.R;
import upworksolutions.themagictricks.adapter.VideoAdapter;
import upworksolutions.themagictricks.model.Video;
import upworksolutions.themagictricks.model.VideoItem;

public class CategoryVideosFragment extends Fragment implements VideoAdapter.OnVideoClickListener {
    private static final String TAG = "CategoryVideosFragment";
    private static final String ARG_CATEGORY_ID = "category_id";
    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private List<VideoItem> videos;
    private int categoryId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getInt(ARG_CATEGORY_ID);
            Log.d(TAG, "Category ID received: " + categoryId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_videos, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        
        videoAdapter = new VideoAdapter(requireContext(), new ArrayList<>(), true, this);
        recyclerView.setAdapter(videoAdapter);
        
        loadVideos();
        return view;
    }

    private void loadVideos() {
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
            List<Video> allVideos = gson.fromJson(jsonObject.get("videos"), listType);

            if (allVideos != null) {
                videos = new ArrayList<>();
                for (Video video : allVideos) {
                    if (video.getCategoryId() == categoryId) {
                        videos.add(new VideoItem(
                            video.getId(),
                            video.getTitle(),
                            video.getDescription(),
                            video.getThumbnailUrl(),
                            video.getVideoUrl(),
                            video.getCategoryId(),
                            getCategoryName(video.getCategoryId()),
                            video.getViews(),
                            video.getUploadDate()
                        ));
                    }
                }
                videoAdapter.updateVideos(videos);
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

    @Override
    public void onVideoClick(VideoItem video) {
        Log.d(TAG, "Video clicked: " + video.getTitle());
        Bundle args = new Bundle();
        args.putString("videoId", video.getId());
        args.putString("videoTitle", video.getTitle());
        args.putString("videoUrl", video.getVideoUrl());
        args.putString("description", video.getDescription());
        args.putString("thumbnail", video.getThumbnail());
        args.putInt("categoryId", video.getCategoryId());
        args.putString("categoryName", video.getCategoryName());
        args.putLong("views", video.getViews());
        args.putString("uploadDate", video.getUploadDate());
        Navigation.findNavController(requireView())
            .navigate(R.id.action_categoryVideosFragment_to_videoPlayerFragment, args);
    }
} 