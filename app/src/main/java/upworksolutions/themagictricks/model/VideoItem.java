package upworksolutions.themagictricks.model;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoItem implements Parcelable {
    private String id;
    private String title;
    private String description;
    private String thumbnail;
    private String videoUrl;
    private int categoryId;
    private String categoryName;
    private long views;
    private String uploadDate;

    public VideoItem() {
    }

    public VideoItem(String id, String title, String description, String thumbnail, String videoUrl, 
                    int categoryId, String categoryName, long views, String uploadDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.videoUrl = videoUrl;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.views = views;
        this.uploadDate = uploadDate;
    }

    protected VideoItem(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        thumbnail = in.readString();
        videoUrl = in.readString();
        categoryId = in.readInt();
        categoryName = in.readString();
        views = in.readLong();
        uploadDate = in.readString();
    }

    public static final Creator<VideoItem> CREATOR = new Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel in) {
            return new VideoItem(in);
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(thumbnail);
        dest.writeString(videoUrl);
        dest.writeInt(categoryId);
        dest.writeString(categoryName);
        dest.writeLong(views);
        dest.writeString(uploadDate);
    }
} 