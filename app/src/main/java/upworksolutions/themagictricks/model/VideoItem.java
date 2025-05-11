package upworksolutions.themagictricks.model;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoItem implements Parcelable {
    private String id;
    private String title;
    private String description;
    private String thumbnail;
    private int categoryId;

    public VideoItem(String id, String title, String description, String thumbnail, int categoryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.categoryId = categoryId;
    }

    protected VideoItem(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        thumbnail = in.readString();
        categoryId = in.readInt();
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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public int getCategoryId() {
        return categoryId;
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
        dest.writeInt(categoryId);
    }
} 