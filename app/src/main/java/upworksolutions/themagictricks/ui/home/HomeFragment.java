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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import upworksolutions.themagictricks.R;
import upworksolutions.themagictricks.adapter.CategoryAdapter;
import upworksolutions.themagictricks.adapter.TrickAdapter;
import upworksolutions.themagictricks.adapter.VideoAdapter;
import upworksolutions.themagictricks.databinding.FragmentHomeBinding;
import upworksolutions.themagictricks.model.Category;
import upworksolutions.themagictricks.model.Trick;
import upworksolutions.themagictricks.model.VideoItem;

public class HomeFragment extends Fragment implements 
    CategoryAdapter.OnCategoryClickListener,
    TrickAdapter.OnTrickClickListener,
    VideoAdapter.OnVideoClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private CategoryAdapter categoryAdapter;
    private TrickAdapter trickAdapter;
    private VideoAdapter videoAdapter;

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
    }

    private void setupRecyclerViews() {
        // Setup Categories RecyclerView
        binding.categoryRecyclerView.setLayoutManager(
            new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(new ArrayList<Category>(), this);
        binding.categoryRecyclerView.setAdapter(categoryAdapter);

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
        homeViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.updateCategories(categories);
        });

        homeViewModel.getTrendingTricks().observe(getViewLifecycleOwner(), tricks -> {
            trickAdapter.updateTricks(tricks);
        });

        homeViewModel.getVideos().observe(getViewLifecycleOwner(), videos -> {
            videoAdapter.updateVideos(videos);
        });

        homeViewModel.getDailyTrickTitle().observe(getViewLifecycleOwner(), title -> {
            binding.featuredTrickTitle.setText(title);
        });

        homeViewModel.getDailyTrickImageUrl().observe(getViewLifecycleOwner(), imageUrl -> {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_trick)
                .error(R.drawable.placeholder_trick)
                .centerCrop()
                .into(binding.featuredTrickThumbnail);
        });
    }

    @Override
    public void onCategoryClick(Category category) {
        Toast.makeText(requireContext(), "Selected category: " + category.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTrickClick(Trick trick) {
        Toast.makeText(requireContext(), "Selected trick: " + trick.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVideoClick(VideoItem video) {
        Toast.makeText(requireContext(), "Selected video: " + video.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}