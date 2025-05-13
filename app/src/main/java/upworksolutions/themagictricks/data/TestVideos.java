package upworksolutions.themagictricks.data;

import java.util.ArrayList;
import java.util.List;

import upworksolutions.themagictricks.model.VideoItem;

public class TestVideos {
    public static List<VideoItem> getTestVideos() {
        List<VideoItem> videos = new ArrayList<>();
        
        videos.add(new VideoItem(
            "1",
            "Card Magic Basics",
            "Learn the fundamentals of card magic with this comprehensive tutorial",
            "https://example.com/thumbnails/card_basics.jpg",
            "https://example.com/videos/card_basics.mp4",
            1,
            "Card Magic",
            1000,
            "2024-03-20"
        ));

        videos.add(new VideoItem(
            "2",
            "Coin Vanish Trick",
            "Master the classic coin vanish with step-by-step instructions",
            "https://example.com/thumbnails/coin_vanish.jpg",
            "https://example.com/videos/coin_vanish.mp4",
            2,
            "Coin Magic",
            800,
            "2024-03-19"
        ));

        videos.add(new VideoItem(
            "3",
            "Rope Magic for Beginners",
            "Easy rope magic tricks that will amaze your audience",
            "https://example.com/thumbnails/rope_magic.jpg",
            "https://example.com/videos/rope_magic.mp4",
            3,
            "Rope Magic",
            1200,
            "2024-03-18"
        ));

        videos.add(new VideoItem(
            "4",
            "Mentalism Techniques",
            "Learn the art of mentalism and mind reading",
            "https://example.com/thumbnails/mentalism.jpg",
            "https://example.com/videos/mentalism.mp4",
            4,
            "Mentalism",
            1500,
            "2024-03-17"
        ));

        videos.add(new VideoItem(
            "5",
            "Street Magic Secrets",
            "Professional street magic tricks revealed",
            "https://example.com/thumbnails/street_magic.jpg",
            "https://example.com/videos/street_magic.mp4",
            5,
            "Street Magic",
            2000,
            "2024-03-16"
        ));

        return videos;
    }
} 