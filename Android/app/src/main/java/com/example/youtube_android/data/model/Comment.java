package com.example.youtube_android.data.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Comment implements Parcelable {
    private String _id;
    public String text;
    public User author;
    private String videoId;
    private Date date;
    private Uri profileImg;
    private String profileImgString;

    public Comment(String _id, User author, String comment, String videoId, Date date, Uri  userImg, String profileImgString) {
        this._id = _id;
        this.author = author;
        this.text = comment;
        this.videoId = videoId;
        this.date = date;
        this.profileImg = userImg;
        this.profileImgString = profileImgString;
    }
    public Comment(String text, User author, String videoId) {
        this.text = text;
        this.author = author;
        this.videoId = videoId;
    }

    public String get_id() {
        return _id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }


    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Uri getProfileImg() {
        return profileImg;
    }

    public String getProfileImgString() {
        return profileImgString;
    }

    public void setProfileImgString(String profileImgString) {
        this.profileImgString = profileImgString;
    }

    public void setProfileImg(Uri profileImg) {
        this.profileImg = profileImg;
    }
    public String getText() {
        return text;
    }



    public void setText(String text) {
        this.text = text;
    }


    public Comment(String text, User author) {
        this.text = text;
        this.author = author;
    }

    protected Comment(Parcel in) {
        text = in.readString();
        author = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeParcelable(author, flags);
    }
}
