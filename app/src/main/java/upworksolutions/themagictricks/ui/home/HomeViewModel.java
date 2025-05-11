package upworksolutions.themagictricks.ui.home;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import upworksolutions.themagictricks.R;
import upworksolutions.themagictricks.data.TestVideos;
import upworksolutions.themagictricks.model.Category;
import upworksolutions.themagictricks.model.Trick;
import upworksolutions.themagictricks.model.VideoItem;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<VideoItem>> videos = new MutableLiveData<>();
    private final MutableLiveData<String> dailyTrickTitle = new MutableLiveData<>();
    private final MutableLiveData<String> dailyTrickImageUrl = new MutableLiveData<>();
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<List<Trick>> trendingTricks = new MutableLiveData<>();
    private final MutableLiveData<String> dailyChallenge = new MutableLiveData<>();
    private final MutableLiveData<List<String>> magiciansNearYou = new MutableLiveData<>();
    private final MutableLiveData<List<VideoItem>> userUploads = new MutableLiveData<>();

    public HomeViewModel() {
        // Initialize with default values
        dailyTrickTitle.setValue("Loading...");
        dailyTrickImageUrl.setValue("https://i.ytimg.com/vi/fYC0Yr4i-gs/maxresdefault.jpg");
        categories.setValue(new ArrayList<>());
        trendingTricks.setValue(new ArrayList<>());
        dailyChallenge.setValue("Loading...");
        magiciansNearYou.setValue(new ArrayList<>());
        userUploads.setValue(new ArrayList<>());
    }

    public void loadData(Context context) {
        try {
            // Load YouTube videos
            InputStream is = context.getAssets().open("youtube_videos.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            
            JSONObject jsonObject = new JSONObject(json);
            JSONArray videosArray = jsonObject.getJSONArray("videos");
            List<VideoItem> videoList = new ArrayList<>();
            
            for (int i = 0; i < videosArray.length(); i++) {
                JSONObject videoObj = videosArray.getJSONObject(i);
                VideoItem video = new VideoItem(
                    videoObj.getString("id"),
                    videoObj.getString("title"),
                    videoObj.getString("description"),
                    videoObj.getString("thumbnail"),
                    videoObj.getInt("categoryId")
                );
                videoList.add(video);
            }
            
            videos.setValue(videoList);
            
            // Set the first video as the featured video
            if (!videoList.isEmpty()) {
                VideoItem featuredVideo = videoList.get(0);
                dailyTrickTitle.setValue(featuredVideo.getTitle());
                dailyTrickImageUrl.setValue(featuredVideo.getThumbnail());
            }
            
        } catch (IOException | JSONException e) {
            Log.e("HomeViewModel", "Error loading data", e);
        }
        
        // Load other data from JSON
        loadCategoriesFromJson(context);
        loadTrendingTricksFromJson(context);
    }

    private void loadCategoriesFromJson(Context context) {
        try {
            String jsonString = loadJsonFromAsset(context, "categories.json");
            JSONArray jsonArray = new JSONArray(jsonString);
            List<Category> categoryList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject categoryObj = jsonArray.getJSONObject(i);
                Category category = new Category(
                    categoryObj.getString("id"),
                    categoryObj.getString("name"),
                    categoryObj.getString("icon")
                );
                categoryList.add(category);
            }

            categories.postValue(categoryList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTrendingTricksFromJson(Context context) {
        try {
            String jsonString = loadJsonFromAsset(context, "tricks.json");
            JSONArray jsonArray = new JSONArray(jsonString);
            List<Trick> trickList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject trickObj = jsonArray.getJSONObject(i);
                Trick trick = new Trick(
                    trickObj.getString("id"),
                    trickObj.getString("title"),
                    trickObj.getString("description"),
                    trickObj.getString("thumbnailUrl"),
                    trickObj.getString("videoUrl"),
                    trickObj.getString("difficulty"),
                    trickObj.getInt("views"),
                    trickObj.getString("category")
                );
                trickList.add(trick);
            }

            trendingTricks.postValue(trickList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String loadJsonFromAsset(Context context, String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<List<VideoItem>> getVideos() {
        return videos;
    }

    public LiveData<String> getDailyTrickTitle() {
        return dailyTrickTitle;
    }

    public LiveData<String> getDailyTrickImageUrl() {
        return dailyTrickImageUrl;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<List<Trick>> getTrendingTricks() {
        return trendingTricks;
    }

    public LiveData<String> getDailyChallenge() {
        return dailyChallenge;
    }

    public LiveData<List<String>> getMagiciansNearYou() {
        return magiciansNearYou;
    }

    public LiveData<List<VideoItem>> getUserUploads() {
        return userUploads;
    }

    // Data Classes
    public static class Challenge {
        private String description;
        private int progress;
        private String reward;

        public Challenge(String description, int progress, String reward) {
            this.description = description;
            this.progress = progress;
            this.reward = reward;
        }

        public String getDescription() {
            return description;
        }

        public int getProgress() {
            return progress;
        }

        public String getReward() {
            return reward;
        }
    }
}