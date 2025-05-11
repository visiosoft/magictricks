package upworksolutions.themagictricks.data;

import java.util.ArrayList;
import java.util.List;

import upworksolutions.themagictricks.R;
import upworksolutions.themagictricks.model.VideoItem;

public class TestVideos {
    public static List<VideoItem> getTestVideos() {
        List<VideoItem> videos = new ArrayList<>();
        
        videos.add(new VideoItem(
            "1",
            "Ambitious Card Routine",
            "Learn the classic ambitious card routine with a modern twist",
            "https://static3.depositphotos.com/1003326/174/i/450/depositphotos_1745520-stock-photo-playing-card-trick.jpg",
            1
        ));

        videos.add(new VideoItem(
            "2",
            "Coins Across",
            "Master the classic coins across effect",
            "https://example.com/thumbnails/coins_across.jpg",
            2
        ));

        videos.add(new VideoItem(
            "3",
            "Mind Reading with Cards",
            "Learn to read minds using a deck of cards",
            "https://example.com/thumbnails/mind_reading.jpg",
            2
        ));

        videos.add(new VideoItem(
            "4",
            "Ring and String",
            "Classic ring and string routine with modern handling",
            "https://example.com/thumbnails/ring_string.jpg",
            2
        ));

        videos.add(new VideoItem(
            "5",
            "Sawing in Half",
            "Learn the classic sawing in half illusion",
            "https://example.com/thumbnails/sawing_half.jpg",
            3
        ));

        return videos;
    }
} 