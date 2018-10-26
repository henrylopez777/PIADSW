package com.example.henrylopez.fettapp.metodos;

public class mNews {
    private String Image;
    private String Title;
    private String Content;

    public mNews(String image, String title, String content) {
        Image = image;
        Title = title;
        Content = content;
    }

    public mNews(){

    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }


}
