package com.hang.myselfcommunity.dto;

public class Publish {
    private String title;
    private String description;
    private String tag;

    public Publish() {
    }

    public Publish(String title, String description, String tag) {
        this.title = title;
        this.description = description;
        this.tag = tag;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Publish{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
