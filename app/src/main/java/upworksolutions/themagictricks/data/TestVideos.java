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
            "https://example.com/videos/ambitious_card.mp4",
            "5:30",
            "Intermediate",
            15000
        ));

        videos.add(new VideoItem(
            "2",
            "Coins Across",
            "Master the classic coins across effect",
            "https://example.com/thumbnails/coins_across.jpg",
            "https://example.com/videos/coins_across.mp4",
            "8:15",
            "Advanced",
            12000
        ));

        videos.add(new VideoItem(
            "3",
            "Mind Reading with Cards",
            "Learn to read minds using a deck of cards",
            "https://example.com/thumbnails/mind_reading.jpg",
            "https://example.com/videos/mind_reading.mp4",
            "6:45",
            "Beginner",
            20000
        ));

        videos.add(new VideoItem(
            "4",
            "Ring and String",
            "Classic ring and string routine with modern handling",
            "https://example.com/thumbnails/ring_string.jpg",
            "https://example.com/videos/ring_string.mp4",
            "7:20",
            "Intermediate",
            18000
        ));

        videos.add(new VideoItem(
            "5",
            "Sawing in Half",
            "Learn the classic sawing in half illusion",
            "https://example.com/thumbnails/sawing_half.jpg",
            "https://example.com/videos/sawing_half.mp4",
            "10:15",
            "Advanced",
            25000
        ));

        return videos;
    }
} 