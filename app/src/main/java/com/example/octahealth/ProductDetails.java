package com.example.octahealth;

import java.util.ArrayList;

public class ProductDetails {

    String title,content,image,actualprice,discountedprice,id;


    public ProductDetails(String title, String content, String image, String actualprice, String discountedprice, String id) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.actualprice = actualprice;
        this.discountedprice = discountedprice;
        this.id = id;
    }

    public String getActualprice() {
        return actualprice;
    }

    public void setActualprice(String actualprice) {
        this.actualprice = actualprice;
    }

    public String getDiscountedprice() {
        return discountedprice;
    }

    public void setDiscountedprice(String discountedprice) {
        this.discountedprice = discountedprice;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ProductDetails() {
    }

}
