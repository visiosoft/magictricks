package upworksolutions.themagictricks.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import upworksolutions.themagictricks.R;
import upworksolutions.themagictricks.adapter.YouTubeVideoAdapter;
import upworksolutions.themagictricks.model.YouTubeVideo;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VideoListFragment extends Fragment implements YouTubeVideoAdapter.OnVideoClickListener {
    private RecyclerView recyclerView;
    private YouTubeVideoAdapter adapter;
    private boolean isGridView = true;
    private ImageButton toggleViewButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);
        
        recyclerView = view.findViewById(R.id.recyclerView);
        toggleViewButton = view.findViewById(R.id.toggleViewButton);
        
        setupRecyclerView();
        loadVideos();
        setupToggleButton();
        
        return view;
    }

    private void setupRecyclerView() {
        adapter = new YouTubeVideoAdapter(requireContext(), new ArrayList<>(), isGridView, this);
        recyclerView.setAdapter(adapter);
        updateLayoutManager();
    }

    private void updateLayoutManager() {
        if (isGridView) {
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        }
        adapter.setViewType(isGridView);
    }

    private void setupToggleButton() {
        toggleViewButton.setOnClickListener(v -> {
            isGridView = !isGridView;
            updateLayoutManager();
            toggleViewButton.setImageResource(isGridView ? R.drawable.ic_list : R.drawable.ic_grid);
        });
        // Set initial icon
        toggleViewButton.setImageResource(isGridView ? R.drawable.ic_list : R.drawable.ic_grid);
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
            Type listType = new TypeToken<ArrayList<YouTubeVideo>>(){}.getType();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            List<YouTubeVideo> videos = gson.fromJson(jsonObject.get("videos"), listType);
            
            adapter.updateVideos(videos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVideoClick(YouTubeVideo video) {
        // Handle video click - you can implement this based on your needs
        // For example, open the video in YouTube app or play it in your app
    }
} 