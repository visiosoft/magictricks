package upworksolutions.themagictricks.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import upworksolutions.themagictricks.R;
import upworksolutions.themagictricks.adapter.VideoAdapter;
import upworksolutions.themagictricks.model.VideoItem;

public class CategoryVideosFragment extends Fragment implements VideoAdapter.OnVideoClickListener {
    private static final String TAG = "CategoryVideosFragment";
    private static final String ARG_CATEGORY_ID = "category_id";
    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private List<VideoItem> videos;
    private FirebaseFirestore db;
    private int categoryId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getInt(ARG_CATEGORY_ID);
            Log.d(TAG, "Category ID: " + categoryId);
        } else {
            Log.e(TAG, "No category ID provided in arguments");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_videos, container, false);
        
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        videos = new ArrayList<>();
        videoAdapter = new VideoAdapter(requireContext(), videos, false, this);
        recyclerView.setAdapter(videoAdapter);
        
        try {
            db = FirebaseFirestore.getInstance();
            Log.d(TAG, "Firebase Firestore initialized successfully");
            loadCategoryVideos();
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Firestore", e);
            Toast.makeText(getContext(), "Error initializing database: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        
        return view;
    }

    private void loadCategoryVideos() {
        Log.d(TAG, "Loading videos for category: " + categoryId);
        db.collection("videos")
            .whereEqualTo("categoryId", categoryId)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                Log.d(TAG, "Successfully retrieved " + queryDocumentSnapshots.size() + " videos");
                videos.clear();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    try {
                        VideoItem video = document.toObject(VideoItem.class);
                        videos.add(video);
                        Log.d(TAG, "Added video: " + video.getTitle());
                    } catch (Exception e) {
                        Log.e(TAG, "Error converting document to VideoItem", e);
                    }
                }
                videoAdapter.updateVideos(videos);
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error loading videos", e);
                Toast.makeText(getContext(), "Error loading videos: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
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