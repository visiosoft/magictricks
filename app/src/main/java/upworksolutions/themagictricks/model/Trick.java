package upworksolutions.themagictricks.model;

public class Trick {
    private String id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String videoUrl;
    private String difficulty;
    private int views;
    private String category;

    public Trick(String id, String title, String description, String thumbnailUrl, 
                String videoUrl, String difficulty, int views, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.difficulty = difficulty;
        this.views = views;
        this.category = category;
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

    public String getDifficulty() {
        return difficulty;
    }

    public int getViews() {
        return views;
    }

    public String getCategory() {
        return category;
    }
} 