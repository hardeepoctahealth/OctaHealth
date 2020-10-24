package com.example.octahealth;

public class OfferDetails {

    String title,image,code;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public OfferDetails() {
    }

    public OfferDetails(String title, String image, String code) {
        this.title = title;
        this.image = image;
        this.code = code;
    }
}
