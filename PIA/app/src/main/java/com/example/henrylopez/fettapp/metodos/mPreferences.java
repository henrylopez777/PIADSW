package com.example.henrylopez.fettapp.metodos;

public class mPreferences {

    private String Desc;
    private String Image;

    public mPreferences(String desc, String image) {
        Desc = desc;
        Image = image;
    }

    public mPreferences(String image) {
        this.Image = image;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public mPreferences(){

    }
}
