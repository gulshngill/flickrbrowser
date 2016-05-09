package com.example.gulshngill.flickrbrowser;

/**
 * Created by gulshngill on 07/05/2016.
 */
public class Photo {
    private String title;
    private String link;
    private String photoUrl;
    private String description;
    private String author;
    private String authorId;

    public Photo(String title, String link, String photoUrl, String description, String author, String authorId) {
        this.title = title;
        this.link = link;
        this.photoUrl = photoUrl;
        this.description = description;
        this.author = author;
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorId() {
        return authorId;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", authorId='" + authorId + '\'' +
                '}';
    }
}
