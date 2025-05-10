package com.upworksolutions.themagictricks.model;

public class YouTubeVideo {
    private String id;
    private String title;
    private String description;
    private String thumbnail;
    private String category;

    public YouTubeVideo(String id, String title, String description, String thumbnail, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.category = category;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getThumbnail() { return thumbnail; }
    public String getCategory() { return category; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
    public void setCategory(String category) { this.category = category; }
} 