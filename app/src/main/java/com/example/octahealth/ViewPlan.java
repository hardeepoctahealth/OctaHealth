package com.example.octahealth;

public class ViewPlan {

    String title,link,buttonname;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getButtonname() {
        return buttonname;
    }

    public void setButtonname(String buttonname) {
        this.buttonname = buttonname;
    }

    public ViewPlan() {
    }

    public ViewPlan(String title, String link, String buttonname) {
        this.title = title;
        this.link = link;
        this.buttonname = buttonname;
    }
}
