package upworksolutions.themagictricks.model;

public class Video {
    private String id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String videoUrl;
    private String duration;
    private String difficulty;
    private int views;

    public Video(String id, String title, String description, String thumbnailUrl, 
                String videoUrl, String duration, String difficulty, int views) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.duration = duration;
        this.difficulty = difficulty;
        this.views = views;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getDuration() {
        return duration;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getViews() {
        return views;
    }
} 