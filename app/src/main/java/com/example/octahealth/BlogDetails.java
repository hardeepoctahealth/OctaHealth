package com.example.octahealth;

public class BlogDetails {

    String title,content,date,image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BlogDetails(String title, String content, String date, String image) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.image = image;
    }

    public BlogDetails() {
    }
}
