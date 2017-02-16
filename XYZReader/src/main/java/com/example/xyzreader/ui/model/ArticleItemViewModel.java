package com.example.xyzreader.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lars on 15.02.17.
 */

public class ArticleItemViewModel implements Parcelable {

    private String title = "";

    private String author = "";

    private long date = 0L;

    private String thumbImageUrl = "";

    private String body = "";

    private String info = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null) {
            return;
        }
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        if (info == null) {
            return;
        }
        this.info = info;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author == null) {
            return;
        }
        this.author = author;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getThumbImageUrl() {
        return thumbImageUrl;
    }

    public void setThumbImageUrl(String thumbImageUrl) {
        if (thumbImageUrl == null) {
            return;
        }
        this.thumbImageUrl = thumbImageUrl;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        if (body == null) {
            return;
        }
        this.body = body;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.author);
        dest.writeString(this.info);
        dest.writeLong(this.date);
        dest.writeString(this.thumbImageUrl);
        dest.writeString(this.body);
    }

    public ArticleItemViewModel() {
    }

    protected ArticleItemViewModel(Parcel in) {
        this.title = in.readString();
        this.author = in.readString();
        this.info = in.readString();
        this.date = in.readLong();
        this.thumbImageUrl = in.readString();
        this.body = in.readString();
    }

    public static final Parcelable.Creator<ArticleItemViewModel> CREATOR = new Parcelable.Creator<ArticleItemViewModel>() {
        @Override
        public ArticleItemViewModel createFromParcel(Parcel source) {
            return new ArticleItemViewModel(source);
        }

        @Override
        public ArticleItemViewModel[] newArray(int size) {
            return new ArticleItemViewModel[size];
        }
    };
}