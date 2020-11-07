package com.example.octahealth;

public class CarouselItem {

    String title,image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CarouselItem() {
    }

    public CarouselItem(String title, String image) {
        this.title = title;
        this.image = image;
    }
}
