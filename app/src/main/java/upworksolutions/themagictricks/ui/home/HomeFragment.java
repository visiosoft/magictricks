package upworksolutions.themagictricks.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.material.progressindicator.CircularProgressIndicator;

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

    private static final int VIDEO_CLICKS_BEFORE_AD = 3;
    private static final int AD_LOAD_TIMEOUT_MS = 5000; // 5 seconds timeout
    private static final boolean USE_TEST_ADS = true; // Set to false for production
    
    // Ad unit IDs
    private static final String BANNER_AD_UNIT_ID = USE_TEST_ADS 
        ? "ca-app-pub-3940256099942544/6300978111"  // Test banner ad unit ID
        : "ca-app-pub-9773068853653447/1453749099"; // Real banner ad unit ID
        
    private static final String INTERSTITIAL_AD_UNIT_ID = USE_TEST_ADS 
        ? "ca-app-pub-3940256099942544/1033173712"  // Test interstitial ad unit ID
        : "ca-app-pub-9773068853653447/2798620351"; // Real interstitial ad unit ID
    
    private int videoClickCount = 0;
    private boolean isLoadingAd = false;
    private VideoItem pendingVideoNavigation = null;
    private Handler adTimeoutHandler = new Handler(Looper.getMainLooper());
    private CircularProgressIndicator loadingIndicator;
    
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private TrickAdapter trickAdapter;
    private VideoAdapter videoAdapter;
    private MagicCategoryAdapter categoryAdapter;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        
        // Initialize the Mobile Ads SDK
        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // SDK initialization complete
                String adStatus = USE_TEST_ADS ? "Test" : "Production";
                Toast.makeText(requireContext(), "AdMob SDK initialized (" + adStatus + " mode)", 
                    Toast.LENGTH_SHORT).show();
            }
        });
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
        
        // Initialize loading indicator
        loadingIndicator = new CircularProgressIndicator(requireContext());
        loadingIndicator.setIndeterminate(true);
        loadingIndicator.setVisibility(View.GONE);
        ((ViewGroup) view).addView(loadingIndicator);
        
        // Set up Watch Now button click listener
        binding.learnNowButton.setText("Watch Now");
        binding.learnNowButton.setOnClickListener(v -> {
            // Get the first video from the list
            List<VideoItem> videos = homeViewModel.getVideos().getValue();
            if (videos != null && !videos.isEmpty()) {
                VideoItem video = videos.get(0);
                navigateToVideoPlayer(video);
            }
        });
        loadBannerAd();
        loadInterstitialAd();
    }

    private void setupRecyclerViews() {
        // Setup videos recycler view
        binding.videosRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), 
            LinearLayoutManager.HORIZONTAL, false));
        videoAdapter = new VideoAdapter(requireContext(), new ArrayList<>(), false, this);
        binding.videosRecyclerView.setAdapter(videoAdapter);

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

    private void loadBannerAd() {
        mAdView = binding.adView;
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        
        // Set up ad listener to show loading status
        mAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                Toast.makeText(requireContext(), "Banner ad loaded successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                Toast.makeText(requireContext(), "Banner ad failed to load: " + loadAdError.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoadingIndicator() {
        if (loadingIndicator != null) {
            loadingIndicator.setVisibility(View.VISIBLE);
            loadingIndicator.bringToFront();
        }
    }

    private void hideLoadingIndicator() {
        if (loadingIndicator != null) {
            loadingIndicator.setVisibility(View.GONE);
        }
    }

    private void startAdLoadTimeout() {
        adTimeoutHandler.postDelayed(() -> {
            if (isLoadingAd) {
                isLoadingAd = false;
                hideLoadingIndicator();
                Toast.makeText(requireContext(), "Ad loading timed out", Toast.LENGTH_SHORT).show();
                // Navigate directly if we have a pending video
                if (pendingVideoNavigation != null) {
                    navigateToVideoPlayer(pendingVideoNavigation);
                    pendingVideoNavigation = null;
                }
            }
        }, AD_LOAD_TIMEOUT_MS);
    }

    private void cancelAdLoadTimeout() {
        adTimeoutHandler.removeCallbacksAndMessages(null);
    }

    private void loadInterstitialAd() {
        if (isLoadingAd) {
            return;
        }
        
        isLoadingAd = true;
        showLoadingIndicator();
        startAdLoadTimeout();
        
        AdRequest adRequest = new AdRequest.Builder().build();
        
        InterstitialAd.load(requireContext(), INTERSTITIAL_AD_UNIT_ID, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        cancelAdLoadTimeout();
                        mInterstitialAd = interstitialAd;
                        isLoadingAd = false;
                        hideLoadingIndicator();
                        Toast.makeText(requireContext(), "Interstitial ad loaded successfully", 
                            Toast.LENGTH_SHORT).show();
                        
                        // If we have a pending video, show the ad immediately
                        if (pendingVideoNavigation != null) {
                            mInterstitialAd.show(requireActivity());
                        }
                        
                        // Set up the full screen content callback
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Toast.makeText(requireContext(), "Interstitial ad dismissed", 
                                    Toast.LENGTH_SHORT).show();
                                // Navigate to video player after ad is dismissed
                                if (pendingVideoNavigation != null) {
                                    navigateToVideoPlayer(pendingVideoNavigation);
                                    pendingVideoNavigation = null;
                                }
                                // Load the next ad
                                loadInterstitialAd();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                cancelAdLoadTimeout();
                                hideLoadingIndicator();
                                Toast.makeText(requireContext(), "Interstitial ad failed to show: " + 
                                    adError.getMessage(), Toast.LENGTH_SHORT).show();
                                // If ad fails to show, navigate directly
                                if (pendingVideoNavigation != null) {
                                    navigateToVideoPlayer(pendingVideoNavigation);
                                    pendingVideoNavigation = null;
                                }
                                mInterstitialAd = null;
                                isLoadingAd = false;
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                Toast.makeText(requireContext(), "Interstitial ad shown successfully", 
                                    Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        cancelAdLoadTimeout();
                        hideLoadingIndicator();
                        Toast.makeText(requireContext(), "Interstitial ad failed to load: " + 
                            loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
                        // If ad fails to load, navigate directly
                        if (pendingVideoNavigation != null) {
                            navigateToVideoPlayer(pendingVideoNavigation);
                            pendingVideoNavigation = null;
                        }
                        mInterstitialAd = null;
                        isLoadingAd = false;
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
        videoClickCount++;
        
        // Store the video for navigation after ad
        pendingVideoNavigation = video;
        
        // Show interstitial ad after every 3rd video click
        if (videoClickCount % VIDEO_CLICKS_BEFORE_AD == 0) {
            if (mInterstitialAd != null) {
                // If ad is already loaded, show it immediately
                showLoadingIndicator();
                mInterstitialAd.show(requireActivity());
            } else {
                // If no ad is loaded, try to load one
                showLoadingIndicator();
                loadInterstitialAd();
            }
        } else {
            // If not showing ad, navigate immediately
            navigateToVideoPlayer(video);
        }
    }

    private void navigateToVideoPlayer(VideoItem video) {
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
            .navigate(R.id.action_homeFragment_to_videoPlayerFragment, args);
    }

    private void showInterstitialAd() {
        if (mInterstitialAd != null) {
            Toast.makeText(requireContext(), "Showing interstitial ad", Toast.LENGTH_SHORT).show();
            mInterstitialAd.show(requireActivity());
        } else {
            Toast.makeText(requireContext(), "Interstitial ad not ready, navigating directly", Toast.LENGTH_SHORT).show();
            // If ad is not ready, navigate directly
            if (pendingVideoNavigation != null) {
                navigateToVideoPlayer(pendingVideoNavigation);
                pendingVideoNavigation = null;
            }
            // Try to load a new ad for next time
            loadInterstitialAd();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cancelAdLoadTimeout();
        if (mAdView != null) {
            mAdView.destroy();
        }
        binding = null;
    }
}