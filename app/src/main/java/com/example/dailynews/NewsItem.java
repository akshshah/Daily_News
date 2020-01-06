package com.example.dailynews;

public class NewsItem {
    private String title;
    private String desc;
    private String link;
    private String date;
    private String imgUrl;

    public NewsItem(String title, String desc, String link, String date, String imgUrl) {
        this.title = title;
        this.desc = desc;
        this.link = link;
        this.date = date;
        this.imgUrl = imgUrl;
    }

    public NewsItem() {
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", link='" + link + '\'' +
                ", date='" + date + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
