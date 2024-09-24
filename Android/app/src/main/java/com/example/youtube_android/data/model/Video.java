package com.example.youtube_android.data.model;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "video")
public class Video implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String _id;
    private String title;
    private String creator;
    private String category;
    private String description;



    private int likes;
    private int views;
    private int year;
    private Uri uriImg;
    private Uri videoUri;
    private String videoSrcString;
    private Uri creatorImg;
    private User userOfVideo;
    private String videoPath;

    private List<Comment> comments;

    public Video(String title, String creator, String category, String description, int year, Uri img, Uri videoUri) {
        this.title = title;
        this.creator = creator;
        this.category = category;
        this.description = description;
        this.year = year;
        this.uriImg = img;
        this.videoUri = videoUri;
        this.comments = new ArrayList<>(); // Initialize the comments list
    }

    public Video(String title, String creator, String category, String description, int year, User userOfVideo) {
        this.title = title;
        this.creator = creator;
        this.category = category;
        this.description = description;
        this.year = year;
        this.userOfVideo = userOfVideo;
        this.comments = new ArrayList<>(); // Initialize the comments list
    }
    public Video(){}

    protected Video(Parcel in) {
        id = in.readInt();
        title = in.readString();
        creator = in.readString();
        category = in.readString();
        description = in.readString();
        likes = in.readInt();
        year = in.readInt();
        uriImg = in.readParcelable(Uri.class.getClassLoader());
        videoUri = in.readParcelable(Uri.class.getClassLoader());
        videoPath = in.readString();
        if (comments == null) {
            comments = new ArrayList<>(); // Initialize if null
        }
        comments = in.createTypedArrayList(Comment.CREATOR);
    }

    public Video(JsonObject jsonObject, String packageName, Context context) {
        this.title = jsonObject.get("title").getAsString();
        this.creator = jsonObject.get("author").getAsString();
        this.description = jsonObject.get("description").getAsString();
        this.year = jsonObject.get("year").getAsInt();
        this.category = jsonObject.get("category").getAsString();
        String videoSrc = jsonObject.get("src").getAsString();
        String fileNameWithExtension = videoSrc.substring(videoSrc.lastIndexOf("/") + 1); // Extracts "video1.mp4" from "Videos/video1.mp4"
        String fileName = fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf(".")); // Extracts "video1" from "video1.mp4"
        int videoResourceId = context.getResources().getIdentifier(fileName, "raw", context.getPackageName());

        if (videoResourceId != 0) {
            this.videoUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + videoResourceId);
        }
        String imgResourceId = jsonObject.get("img").getAsString();
        int intResourceId = context.getResources().getIdentifier(imgResourceId, "drawable", context.getPackageName());
        this.uriImg = Uri.parse("android.resource://" + context.getPackageName() + "/" + intResourceId);
        this.id = jsonObject.get("id").getAsInt();
        this.likes = jsonObject.get("likes").getAsInt();

    }

    public String getVideoPath() {
        if (videoUri != null) {
            return videoUri.getPath(); // Return path portion of the URI
        }
        return null;
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(creator);
        dest.writeString(category);
        dest.writeString(description);
        dest.writeInt(likes);
        dest.writeInt(year);
        dest.writeParcelable(uriImg, flags);
        dest.writeParcelable(videoUri, flags);
        dest.writeString(videoPath);
        dest.writeTypedList(comments); // Properly write the comments list
    }

    public List<Comment> getComments() {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        return comments;
    }

    public void addComment(Comment comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        this.comments.add(0, comment);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreator() {
        return creator;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public int getLikes() {
        return likes;
    }

    public int getViews() {
        return views;
    }

    public int getYear() {
        return year;
    }

    public Uri getUriImg() {
        return uriImg;
    }

    public Uri getCreatorImg() {
        return creatorImg;
    }

    public Uri getVideoUri() {
        return videoUri;
    }
    public User getUserOfVideo() {
        return userOfVideo;
    }

    public String getVideoSrcString() {
        return videoSrcString;
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setUriImg(Uri uriImg) {
        this.uriImg = uriImg;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setUserOfVideo(User userOfVideo) {
        this.userOfVideo = userOfVideo;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public void setCreatorImg(Uri creatorImg) {
        this.creatorImg = creatorImg;
    }

    public void setVideoSrcString(String videoSrcString) {
        this.videoSrcString = videoSrcString;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", creator='" + creator + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", likes=" + likes +
                ", year=" + year +
                ", uriImg=" + uriImg +
                ", videoUri=" + videoUri +
                ", videoPath='" + videoPath + '\'' +
                ", comments=" + comments +
                '}';
    }
}
