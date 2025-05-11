package upworksolutions.themagictricks.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import upworksolutions.themagictricks.R;
import upworksolutions.themagictricks.adapter.MagicCategoryAdapter;
import upworksolutions.themagictricks.adapter.TrickAdapter;
import upworksolutions.themagictricks.adapter.VideoAdapter;
import upworksolutions.themagictricks.databinding.FragmentHomeBinding;
import upworksolutions.themagictricks.model.MagicCategory;
import upworksolutions.themagictricks.model.Trick;
import upworksolutions.themagictricks.model.VideoItem;
import upworksolutions.themagictricks.utils.CategoryLoader;

public class HomeFragment extends Fragment implements 
    TrickAdapter.OnTrickClickListener,
    VideoAdapter.OnVideoClickListener,
    MagicCategoryAdapter.OnCategoryClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private TrickAdapter trickAdapter;
    private VideoAdapter videoAdapter;
    private MagicCategoryAdapter categoryAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerViews();
        observeViewModel();
        homeViewModel.loadData(requireContext());
        
        // Set up Watch Now button click listener
        binding.learnNowButton.setText("Watch Now");
        binding.learnNowButton.setOnClickListener(v -> {
            // Get the first video from the list
            List<VideoItem> videos = homeViewModel.getVideos().getValue();
            if (videos != null && !videos.isEmpty()) {
                VideoItem video = videos.get(0);
                // Navigate to video player
                Bundle args = new Bundle();
                args.putParcelable("video", video);
                Navigation.findNavController(view)
                    .navigate(R.id.action_homeFragment_to_videoPlayerFragment, args);
            }
        });
    }

    private void setupRecyclerViews() {
        // Setup Categories RecyclerView with GridLayout
        binding.categoriesRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        List<MagicCategory> categories = CategoryLoader.loadCategories(requireContext());
        categoryAdapter = new MagicCategoryAdapter(categories, this);
        binding.categoriesRecyclerView.setAdapter(categoryAdapter);

        // Setup Trending Tricks RecyclerView
        binding.trendingTricksRecycler.setLayoutManager(
            new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        trickAdapter = new TrickAdapter(new ArrayList<>(), this);
        binding.trendingTricksRecycler.setAdapter(trickAdapter);

        // Setup Videos RecyclerView
        binding.videosRecyclerView.setLayoutManager(
            new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        videoAdapter = new VideoAdapter(new ArrayList<>(), this);
        binding.videosRecyclerView.setAdapter(videoAdapter);
    }

    private void observeViewModel() {
        homeViewModel.getTrendingTricks().observe(getViewLifecycleOwner(), tricks -> {
            if (tricks != null) {
                trickAdapter.updateTricks(tricks);
            }
        });

        homeViewModel.getVideos().observe(getViewLifecycleOwner(), videos -> {
            if (videos != null) {
                videoAdapter.updateVideos(videos);
            }
        });

        homeViewModel.getDailyTrickTitle().observe(getViewLifecycleOwner(), title -> {
            if (title != null) {
                binding.featuredTrickTitle.setText(title);
            }
        });

        homeViewModel.getDailyTrickImageUrl().observe(getViewLifecycleOwner(), imageUrl -> {
            if (imageUrl != null) {
                Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_trick)
                    .error(R.drawable.placeholder_trick)
                    .centerCrop()
                    .into(binding.featuredTrickThumbnail);
            }
        });
    }

    @Override
    public void onCategoryClick(MagicCategory category) {
        Bundle args = new Bundle();
        args.putInt("category_id", category.getId());
        Navigation.findNavController(requireView())
                .navigate(R.id.action_homeFragment_to_categoryVideosFragment, args);
    }

    @Override
    public void onTrickClick(Trick trick) {
        Toast.makeText(requireContext(), "Selected trick: " + trick.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVideoClick(VideoItem video) {
        // Navigate to video player with the selected video
        Bundle args = new Bundle();
        args.putParcelable("video", video);
        Navigation.findNavController(requireView())
            .navigate(R.id.action_homeFragment_to_videoPlayerFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}