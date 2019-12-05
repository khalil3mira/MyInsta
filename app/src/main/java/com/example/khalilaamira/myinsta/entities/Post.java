package com.example.khalilaamira.myinsta.entities;

/**
 * Created by Khalil Aamira on 22/12/2017.
 */

public class Post {

    private String title, description, image,UID,postingTime;

    public Post() {
    }

    public Post(String title, String description, String image, String UID, String postingTime) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.UID = UID;
        this.postingTime = postingTime;
    }

    public String getPostingTime() {
        return postingTime;
    }

    public void setPostingTime(String postingTime) {
        this.postingTime = postingTime;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
